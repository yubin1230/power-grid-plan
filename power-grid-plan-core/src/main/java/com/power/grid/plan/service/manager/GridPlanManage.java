package com.power.grid.plan.service.manager;


import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
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

    /**
     * 蚂蚁数量
     */
    private static final int ANT_NUM = 50;

    /**
     * 循环次数
     */
    private static final int LOOP = 100;

    /**
     * 返回路径数量
     */
    private static final int PATH_NUM = 3;

    /**
     * 以直线距离1/2为半径，向外延伸距离，单位KM
     */
    private static final double RADIUS_ADD = 0.5;

    @Resource
    private BaseDataInit baseDataInit;

    @Resource
    private LuceneSpatial luceneSpatial;

    @Resource
    private CalculateService calculateService;

    @Resource
    private AntCalculateManage antCalculateManage;

    @Resource(name = "executorService")
    private ExecutorService executorService;

    public List<HandleBo> calculate(Long start, Long end) throws IOException, InterruptedException, ExecutionException {

        List<HandleBo> handleBoList = new ArrayList<>();

        for (int i = 0; i < PATH_NUM; i++) {
            HandleBo bo = calculateBestPath(start, end, handleBoList);
            handleBoList.add(bo);
        }

        return handleBoList;
    }

    public HandleBo calculateBestPath(Long start, Long end, List<HandleBo> handleBoList) throws IOException, InterruptedException, ExecutionException {
        HandleBo bo = new HandleBo();
        List<RoadBo> roadBoList = getRoadBoList(start, end);
        //初始化概率
        Map<Long, RoadHandleBo> roadHandleBoMap = calculateService.initProbability(roadBoList);
        List<AntCalculateTask> antCalculateTaskList = IntStream.range(0, LOOP).mapToObj(s -> {
            antCalculateManage.initAntCalculateManage(handleBoList, roadHandleBoMap, start, end, new HandleBo(), ANT_NUM);
            return new AntCalculateTask(antCalculateManage);
        }).collect(Collectors.toList());

        List<Future<HandleBo>> futureList = executorService.invokeAll(antCalculateTaskList, 10, TimeUnit.MINUTES);

//        antCalculateManage.initAntCalculateManage(handleBoList, roadHandleBoMap, start, end, new HandleBo(), ANT_NUM);
//        bo = new AntCalculateTask(antCalculateManage).call();
        for (Future<HandleBo> future : futureList) {
            if (future.isCancelled() || Objects.isNull(future.get())) {
                continue;
            }
            if (future.get().getSumPrice() < bo.getSumPrice()) {
                bo = future.get();
            }
        }
        return bo;
    }

    private List<RoadBo> getRoadBoList(Long start, Long end) throws IOException {
        //加载所有路段信息
        List<RoadBo> roadBoList = baseDataInit.getRoadBoList();

        //所有节点信息
        List<NodeBo> nodeBoList = baseDataInit.getNodeBoList();
        Map<String, NodeBo> nodeBoMap = nodeBoList.stream().collect(Collectors.toMap(item -> String.valueOf(item.getId()), item -> item));

        List<NodeBo> nodeStartEnd = Lists.newArrayList(nodeBoMap.get(String.valueOf(start)), nodeBoMap.get(String.valueOf(end)));

        //中心点坐标
        NodeBo nodeBo = CoordinateCenter.GetCenterPoint(nodeStartEnd);

        double radius = CoordinateDistance.GetDistance(nodeBoMap.get(String.valueOf(start)), nodeBoMap.get(String.valueOf(end))) / (2 * 1000) + RADIUS_ADD;

        //查询end节点，
        List<NodeBo> searchList = luceneSpatial.search(nodeBo, radius, nodeBoList.size());
        HashSet<Long> matchNodeIdList = Sets.newHashSet(searchList.stream().map(NodeBo::getId).collect(Collectors.toSet()));
        //包含开始节点和结束节点
        return roadBoList.parallelStream().filter(roadBo -> matchNodeIdList.contains(roadBo.getStartNodeId()) || matchNodeIdList.contains(roadBo.getEndNodeId())).collect(Collectors.toList());
    }

}
