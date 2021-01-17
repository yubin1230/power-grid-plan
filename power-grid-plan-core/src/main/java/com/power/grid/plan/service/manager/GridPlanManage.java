package com.power.grid.plan.service.manager;


import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.power.grid.plan.Constants;
import com.power.grid.plan.dto.bo.HandleBo;
import com.power.grid.plan.dto.bo.NodeBo;
import com.power.grid.plan.dto.bo.RoadBo;
import com.power.grid.plan.dto.bo.RoadHandleBo;
import com.power.grid.plan.service.CalculateService;
import com.power.grid.plan.service.coordinate.CoordinateCenter;
import com.power.grid.plan.service.coordinate.CoordinateDistance;
import com.power.grid.plan.service.thread.AntCalculateTask;
import com.power.grid.plan.util.BaseDataInit;
import com.power.grid.plan.service.coordinate.LuceneSpatial;
import lombok.Data;
import org.apache.commons.collections.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 网格计划服务
 * @author yubin
 * @date 2020/12/5 15:54
 */
@Component
@Data
public class GridPlanManage {

    private static final Logger LOG = LogManager.getLogger(GridPlanManage.class);



    @Resource
    private BaseDataInit baseDataInit;

    @Resource
    private LuceneSpatial luceneSpatial;

    @Resource
    private CalculateService calculateService;

    @Resource(name = "executorService")
    private ExecutorService executorService;

    public List<HandleBo> calculate(Long start, Long end) throws IOException, InterruptedException, ExecutionException {

        return calculateBestPath(start, end);
    }

    public List<HandleBo> calculateBestPath(Long start, Long end) throws IOException, InterruptedException, ExecutionException {
        List<HandleBo> handleBoList=Collections.synchronizedList(new ArrayList<>());;
        Set<HandleBo> handleBoSet=new HashSet<>();
        List<RoadBo> roadBoList = getRoadBoList(start, end);
        //初始化概率
        Map<Long, RoadHandleBo> roadHandleBoMap = calculateService.initProbability(roadBoList);
        AntCalculateManage antCalculateManage=new AntCalculateManage(roadHandleBoMap,handleBoList, start, end, Constants.ANT_NUM);
        antCalculateManage.setCalculateService(calculateService);
        List<AntCalculateTask> antCalculateTaskList = IntStream.range(0, Constants.LOOP).mapToObj(s ->new AntCalculateTask(antCalculateManage)).collect(Collectors.toList());
        List<Future<List<HandleBo>>> futureList = executorService.invokeAll(antCalculateTaskList, 30, TimeUnit.MINUTES);

//        antCalculateManage.initAntCalculateManage(roadHandleBoMap, start, end,  ANT_NUM);
//        handleBoList = new AntCalculateTask(antCalculateManage).call();
        for (int i = 0; i < Constants.LOOP; i++) {
            Future<List<HandleBo>> future = futureList.get(i);
            if (future.isCancelled() || Objects.isNull(future.get())) {
                continue;
            }

            if (CollectionUtils.isNotEmpty(future.get())) {
                handleBoSet.addAll(future.get());
                if(handleBoSet.size()>3){
                    handleBoList=handleBoSet.stream().sorted(Comparator.comparing(HandleBo::getSumPrice)).collect(Collectors.toList()).subList(0,1);
                }else{
                    handleBoList=handleBoSet.stream().sorted(Comparator.comparing(HandleBo::getSumPrice)).collect(Collectors.toList());
                }

            }
        }
        return handleBoList;
    }

    public List<RoadBo> getRoadBoList(Long start, Long end) throws IOException {
        //加载所有路段信息
        List<RoadBo> roadBoList = baseDataInit.getRoadBoList();

        //所有节点信息
        List<NodeBo> nodeBoList = baseDataInit.getNodeBoList();
        Map<String, NodeBo> nodeBoMap = nodeBoList.stream().collect(Collectors.toMap(item -> String.valueOf(item.getId()), item -> item));

        List<NodeBo> nodeStartEnd = Lists.newArrayList(nodeBoMap.get(String.valueOf(start)), nodeBoMap.get(String.valueOf(end)));

        //中心点坐标
        NodeBo nodeBo = CoordinateCenter.GetCenterPoint(nodeStartEnd);

        double radius = CoordinateDistance.GetDistance(nodeBoMap.get(String.valueOf(start)), nodeBoMap.get(String.valueOf(end))) / (2 * 1000) + Constants.RADIUS_ADD;

        //查询end节点，
        List<NodeBo> searchList = luceneSpatial.search(nodeBo, radius, nodeBoList.size());
        HashSet<Long> matchNodeIdList = Sets.newHashSet(searchList.stream().map(NodeBo::getId).collect(Collectors.toSet()));
        //包含开始节点和结束节点
        return roadBoList.parallelStream().filter(roadBo -> matchNodeIdList.contains(roadBo.getStartNodeId()) || matchNodeIdList.contains(roadBo.getEndNodeId())).collect(Collectors.toList());
    }

}
