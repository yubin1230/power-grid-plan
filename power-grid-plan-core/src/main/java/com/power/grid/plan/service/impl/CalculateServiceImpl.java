package com.power.grid.plan.service.impl;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.power.grid.plan.dto.bo.HandleBo;
import com.power.grid.plan.dto.bo.RoadBo;
import com.power.grid.plan.dto.bo.RoadHandleBo;
import com.power.grid.plan.dto.bo.WalkBo;
import com.power.grid.plan.exception.DeadCircleException;
import com.power.grid.plan.exception.UnableArriveException;
import com.power.grid.plan.service.CalculateService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

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


    /**
     * 信息素浓度的影响因子
     */
    private int alpha = 1;

    /**
     * 设置为5
     */
    private int beta = 5;

    /**
     * 挥发因子
     */
    private double rho = 0.9;//信息素挥发因子

    /**
     * 循环银子，根据选择数量，最少循环15次，次数越多，路径准确性越高
     */
    private int loop = 15;


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
            double sumPrice = v.stream().mapToDouble(n -> n.getDistance() * n.getPrice()).sum();
            Map<Long, Double> probabilityMap = new HashMap<>();
            Map<Long, Double> sumPriceMap = new HashMap<>();
            v.forEach(n -> {
                if (k.equals(n.getStartNodeId())) {
                    probabilityMap.put(n.getEndNodeId(), n.getDistance() * n.getPrice() / sumPrice);
                    sumPriceMap.put(n.getEndNodeId(), n.getDistance() * n.getPrice());
                } else {
                    probabilityMap.put(n.getStartNodeId(), n.getDistance() * n.getPrice() / sumPrice);
                    sumPriceMap.put(n.getStartNodeId(), n.getDistance() * n.getPrice());
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

    @Override
    public HandleBo handle(Long start, Long end, Map<Long, RoadHandleBo> roadHandleBoMap) {
        HandleBo handleBo = new HandleBo();
        LinkedList<Long> processedIds = new LinkedList<>();
        Set<Long> deadIds = new HashSet<>();
        Double sumPrice = 0.0;
        processedIds.add(start);
        Long walk = start;
        while (true) {
            RoadHandleBo roadHandleBo = roadHandleBoMap.get(walk);
            WalkBo walkBo = roulette(roadHandleBo, processedIds, deadIds);
            if (walkBo.isDead()) {
                processedIds.remove(walk);
                deadIds.add(walk);
                walk = processedIds.getLast();
                //减去与上一个距离
                sumPrice -= roadHandleBo.getSumPrice().get(walk);
            } else {
                walk = walkBo.getNext();
                sumPrice += roadHandleBo.getSumPrice().get(walk);
                processedIds.add(walk);
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

    private WalkBo roulette(RoadHandleBo roadHandleBo, LinkedList<Long> processedIds, Set<Long> deadIds) {
        Map<Long, Double> probability = roadHandleBo.getProbability();
        Map<Long, Double> sumPrice = roadHandleBo.getSumPrice();
        List<Long> keyList = new ArrayList<>(probability.keySet());

        //无下一节点或下一节点在已行走路线中
        Set<Long> noProcessedIds = Sets.newHashSet(processedIds);
        noProcessedIds.addAll(deadIds);
        if (noProcessedIds.containsAll(probability.keySet())) {
            return new WalkBo(true);
        }


        //删除已经处理过的节点
        noProcessedIds.forEach(no -> {
            if (keyList.contains(no)) {
                keyList.remove(no);
            }
        });

        //轮盘赌
        double sum = 0;
        Long nodeId = keyList.get(0);
        //轮盘赌初始因子
        double a = sum_single_possible(probability, sumPrice, keyList);

        int maxSum = loop / keyList.size();
        while (sum < maxSum) {
            Random random = new Random();
            int index = random.nextInt(keyList.size());
            nodeId = keyList.get(index);
            double n = 1.0 / sumPrice.get(nodeId);
            n = Math.pow(n, beta);
            double t = probability.get(nodeId);
            t = Math.pow(t, alpha);
            sum = sum + n * t * 1.0 / a;

        }
        return new WalkBo(nodeId);

    }

    private double sum_single_possible(Map<Long, Double> probability, Map<Long, Double> sumPrice, List<Long> keyList) {
        //根据公式计算概率
        double sum = 0;
        for (Long nodeId : keyList) {
            double a = 1.0 / sumPrice.get(nodeId);
            a = Math.pow(a, beta);
            double b = probability.get(nodeId);
            b = Math.pow(b, alpha);
            sum = sum + a * b;

        }
        return sum;
    }


    @Override
    public  void releasePheromone(Map<Long, RoadHandleBo> roadHandleBoMap, HandleBo handleBo) {

        LinkedList<Long> handlePath = handleBo.getHandlePath();

        Double sumPrice = handleBo.getSumPrice();

        for (int i = 0; i < handlePath.size() - 1; i++) {
            RoadHandleBo roadHandleBo = roadHandleBoMap.get(handlePath.get(i));
            Map<Long, Double> probability = roadHandleBo.getProbability();
            Double probabilityNode = probability.get(handlePath.get(i + 1)) + 1.0 / sumPrice;
            probability.put(handlePath.get(i + 1), probabilityNode);
        }
    }

    @Override
    public void volatilizePheromone(Map<Long, RoadHandleBo> roadHandleBoMap) {
        roadHandleBoMap.values().forEach(bo -> {
            Map<Long, Double> probability = bo.getProbability();
            probability.forEach((k, v) -> {
                probability.put(k, v * rho);
            });
        });
    }
}
