package com.power.grid.plan.service.impl;

import com.power.grid.plan.Constants;
import com.power.grid.plan.dto.bo.HandleBo;
import com.power.grid.plan.dto.bo.RoadBo;
import com.power.grid.plan.dto.bo.RoadHandleBo;
import com.power.grid.plan.dto.bo.WalkBo;
import com.power.grid.plan.exception.UnableArriveException;
import com.power.grid.plan.service.CalculateService;
import com.power.grid.plan.service.algorithm.Roulette;
import com.power.grid.plan.service.algorithm.Segment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 计算实现类
 * @author yubin
 * @date 2020/12/1 0:10
 */
@Service
public class CalculateServiceImpl implements CalculateService {

    private static final Logger LOG = LogManager.getLogger(CalculateServiceImpl.class);


    @Resource
    private Segment segment;

    @Resource
    private Roulette roulette;




    @Override
    public Map<Long, RoadHandleBo> initProbability(List<RoadBo> roadBoList) {
        Map<Long, RoadHandleBo> roadHandleBoMap = new HashMap<>();
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
//            double sumPrice = v.stream().mapToDouble(n -> n.getDistance() * n.getPrice()).sum();
            Map<Long, Double> probabilityMap = new HashMap<>();
            Map<Long, Double> sumPriceMap = new HashMap<>();
            v.forEach(n -> {
//                BigDecimal d = new BigDecimal(Double.toString(n.getDistance()));
//                BigDecimal p = new BigDecimal(Double.toString(n.getPrice()));
//                double price = d.multiply(p).doubleValue();
                double price = n.getDistance();
                if (k.equals(n.getStartNodeId())) {
                    probabilityMap.put(n.getEndNodeId(), 1.0 / v.size());
                    sumPriceMap.put(n.getEndNodeId(), price);
                } else {
                    probabilityMap.put(n.getStartNodeId(), 1.0 / v.size());
                    sumPriceMap.put(n.getStartNodeId(), price);
                }
            });
            RoadHandleBo ro = new RoadHandleBo();
            ro.setNodeId(k);
            ro.setProbability(probabilityMap);
            ro.setSumPrice(sumPriceMap);
            roadHandleBoMap.put(k, ro);
        });

        return roadHandleBoMap;
    }

    public static void main(String[] args) {
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            double index = random.nextDouble();
            System.out.println(index);
        }

    }

    @Override
    public HandleBo handle(Long start, Long end, Map<Long, RoadHandleBo> roadHandleBoMap, Set<Long> deadIds1) {
        HandleBo handleBo = new HandleBo();
        LinkedList<Long> processedIds = new LinkedList<>();
        Set<Long> processedIdSet = new HashSet<>();
        Set<Long> deadIds = new HashSet<>();
        Double sumPrice = 0.0;
        processedIds.add(start);
        processedIdSet.add(start);
        Long walk = start;
        while (true) {

            RoadHandleBo roadHandleBo = roadHandleBoMap.get(walk);

            WalkBo walkBo = roulette.calculate(roadHandleBo, deadIds, processedIdSet);

            if (walkBo.isDead()) {

                processedIds.remove(walk);
                processedIdSet.remove(walk);

                deadIds.add(walk);

                walk = processedIds.getLast();
                //减去与上一个距离
                sumPrice -= roadHandleBo.getSumPrice().get(walk);

            } else {
                walk = walkBo.getNext();
                sumPrice += roadHandleBo.getSumPrice().get(walk);
                processedIds.add(walk);
                processedIdSet.add(walk);
            }
            if (walk.equals(start) && deadIds.containsAll(roadHandleBoMap.get(start).getProbability().keySet())) {
                throw new UnableArriveException("开始、结束节点无法到达");
            }

            if (walk.equals(end)) {
                break;
            }
        }
        handleBo.setHandlePath(processedIds);
        handleBo.setSumPrice(sumPrice);
        return handleBo;
    }


    @Override
    public void releasePheromone(Map<Long, RoadHandleBo> roadHandleBoMap, List<HandleBo> handleBoList) {

        for (int i = 0; i < handleBoList.size(); i++) {

            LinkedList<Long> handlePath = handleBoList.get(i).getHandlePath();

            Double sumPrice = handleBoList.get(i).getSumPrice();

            for (int j = 0; j < handlePath.size() - 1; j++) {
                RoadHandleBo roadHandleBo = roadHandleBoMap.get(handlePath.get(j));
                if (Objects.isNull(roadHandleBo)) {
                    System.out.println(handlePath.get(j));
                    System.out.println(roadHandleBo);
                    continue;
                }
                Map<Long, Double> probability = roadHandleBo.getProbability();
                Double probabilityNode = probability.get(handlePath.get(j + 1)) + 1 / (sumPrice) * Constants.PHEROMONE_RELEASING_UNIT;
                probability.put(handlePath.get(j + 1), probabilityNode);
            }
        }


    }

    @Override
    public void volatilizePheromone(Map<Long, RoadHandleBo> roadHandleBoMap) {
        roadHandleBoMap.values().forEach(bo -> {
            Map<Long, Double> probability = bo.getProbability();
            probability.forEach((k, v) -> {
                probability.put(k, v * Constants.RHO);
            });
        });
    }


}
