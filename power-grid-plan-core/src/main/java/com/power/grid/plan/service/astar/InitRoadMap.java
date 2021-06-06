package com.power.grid.plan.service.astar;

import com.power.grid.plan.dto.bo.AStarRoadHandleBo;
import com.power.grid.plan.dto.bo.RoadBo;
import com.power.grid.plan.dto.bo.RoadHandleBo;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * A 星算法初始化路径
 * @author yubin
 * @date 2021/1/28 22:36
 */
@Component
public class InitRoadMap {

    public Map<Long, AStarRoadHandleBo> initRoadMap(List<RoadBo> roadBoList) {
        Map<Long, AStarRoadHandleBo> roadHandleBoMap = new ConcurrentHashMap<>();
        Map<Long, List<RoadBo>> startNodeMap = roadBoList.parallelStream().collect(Collectors.groupingBy(RoadBo::getStartNodeId));
        Map<Long, List<RoadBo>> endNodeMap = roadBoList.parallelStream().collect(Collectors.groupingBy(RoadBo::getEndNodeId));
        Map<Long, List<RoadBo>> nodeMap = new HashMap<>();
        Set<Long> nodeIdSet = new HashSet<>();
        nodeIdSet.addAll(startNodeMap.keySet());
        nodeIdSet.addAll(endNodeMap.keySet());
        nodeIdSet.forEach(nodeId -> {
            List<RoadBo> node = new ArrayList<>();
            List<RoadBo> startNode = startNodeMap.get(nodeId);
            List<RoadBo> endNode = endNodeMap.get(nodeId);
            if (!CollectionUtils.isEmpty(startNode)) {
                node.addAll(startNode);
            }
            if (!CollectionUtils.isEmpty(endNode)) {
                node.addAll(endNode);
            }
            nodeMap.put(nodeId, node);
        });
        nodeMap.forEach((k, v) -> {
            Map<Long, Double> probabilityMap = new HashMap<>();
            Map<Long, Double> sumPriceMap = new HashMap<>();
            v.forEach(n -> {
                BigDecimal d = new BigDecimal(Double.toString(n.getDistance()));
                BigDecimal p = new BigDecimal(Double.toString(n.getPrice()));
                double price = d.multiply(p).doubleValue();
                if (k.equals(n.getStartNodeId())) {
                    probabilityMap.put(n.getEndNodeId(), n.getDistance());
                    sumPriceMap.put(n.getEndNodeId(), price);
                } else {
                    probabilityMap.put(n.getStartNodeId(), n.getDistance());
                    sumPriceMap.put(n.getStartNodeId(), price);
                }
            });
            AStarRoadHandleBo ro = new AStarRoadHandleBo();
            ro.setNodeId(k);
            ro.setDistance(probabilityMap);
            ro.setSumPrice(sumPriceMap);
            roadHandleBoMap.put(k, ro);
        });

        return roadHandleBoMap;
    }
}
