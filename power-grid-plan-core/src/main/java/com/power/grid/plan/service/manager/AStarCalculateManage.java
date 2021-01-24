package com.power.grid.plan.service.manager;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.power.grid.plan.Constants;
import com.power.grid.plan.dto.bo.*;
import com.power.grid.plan.service.CalculateService;
import com.power.grid.plan.service.astar.AStar;
import com.power.grid.plan.service.coordinate.CoordinateCenter;
import com.power.grid.plan.service.coordinate.CoordinateDistance;
import com.power.grid.plan.service.coordinate.LuceneSpatial;
import com.power.grid.plan.service.thread.AntCalculateTask;
import com.power.grid.plan.util.BaseDataInit;
import com.power.grid.plan.util.RemoveLoopRoad;
import lombok.Data;
import org.apache.commons.collections.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 蚂蚁计算管理类
 * @author yubin
 * @date 2021/1/10 13:27
 */
@Data
public class AStarCalculateManage {


    private static final Logger LOG = LogManager.getLogger(GridPlanManage.class);


    @Resource
    private BaseDataInit baseDataInit;

    @Resource
    private LuceneSpatial luceneSpatial;

    @Resource
    private CalculateService calculateService;

    public List<HandleBo> calculate(Long start, Long end) throws IOException, InterruptedException, ExecutionException {

        List<RoadBo> roadBoList = getRoadBoList(start, end);
        //初始化概率
        Map<Long, RoadHandleBo> roadHandleBoMap = calculateService.initProbability(roadBoList);
        List<NodeBo> nodeBoList = baseDataInit.getNodeBoList();

        Map<Long, NodeBo> nodeBoMap = nodeBoList.stream().collect(Collectors.toMap(NodeBo::getId, node -> node, (key1, key2) -> key1));


        AStarMapInfo mapInfo = new AStarMapInfo();
        mapInfo.setRoadHandleBoMap(roadHandleBoMap);
        mapInfo.setNodeBoMap(nodeBoMap);
        mapInfo.setStart(new AStarNodeBo(nodeBoMap.get(start)));
        mapInfo.setEnd(new AStarNodeBo(nodeBoMap.get(end)));

        AStar aStar = new AStar();
        aStar.start(mapInfo);
        return null;
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