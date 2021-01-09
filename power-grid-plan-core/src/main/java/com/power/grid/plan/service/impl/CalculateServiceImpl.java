package com.power.grid.plan.service.impl;

import com.power.grid.plan.dto.bo.HandleBo;
import com.power.grid.plan.dto.bo.RoadBo;
import com.power.grid.plan.dto.bo.RoadHandleBo;
import com.power.grid.plan.exception.DeadCircleException;
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
        Map<Long, List<RoadBo>> startNodeMap = roadBoList.stream().collect(Collectors.groupingBy(RoadBo::getStartNodeId));
        Map<Long, List<RoadBo>> endNodeMap = roadBoList.stream().collect(Collectors.groupingBy(RoadBo::getEndNodeId));
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
        Double sumPrice = 0.0;
        processedIds.add(start);
        while (true) {
            RoadHandleBo roadHandleBo = roadHandleBoMap.get(start);
            start = roulette(roadHandleBo, processedIds);
            sumPrice += roadHandleBo.getSumPrice().get(start);
            processedIds.add(start);
            if (start.equals(end)) {
                break;
            }
        }
        handleBo.setHandlePath(processedIds);
        handleBo.setSumPrice(sumPrice);
        LOG.info("获取到最佳路径：{}", handleBo);
        return handleBo;
    }

    private Long roulette(RoadHandleBo roadHandleBo, LinkedList<Long> processedIds) {
        Map<Long, Double> probability = roadHandleBo.getProbability();
        Map<Long, Double> sumPrice = roadHandleBo.getSumPrice();
        List<Long> keyList = new ArrayList<>(probability.keySet());
        //轮盘赌，current为当前城市标号
        double sum = 0;
        Long nodeId = keyList.get(0);
        double a = sum_single_possible(roadHandleBo, processedIds);
        while (sum < loop / keyList.size()) {
            Random random = new Random();
            int index = random.nextInt(keyList.size());
            nodeId = keyList.get(index);
            if (!processedIds.contains(nodeId)) {
                //假如该数不在数组里，则令概率增加
                double n = 1.0 / sumPrice.get(nodeId);
                n = Math.pow(n, beta);
                double t = probability.get(nodeId);
                t = Math.pow(t, alpha);
                sum = sum + n * t * 1.0 / a;
            }
        }
        return nodeId;

    }

    private double sum_single_possible(RoadHandleBo roadHandleBo, LinkedList<Long> processedIds) {
        //根据公式计算概率
        Map<Long, Double> probability = roadHandleBo.getProbability();
        Map<Long, Double> sumPrice = roadHandleBo.getSumPrice();
        double sum = 0;
        for (Long nodeId : probability.keySet()) {
            if (!processedIds.contains(nodeId)) {
                double a = 1.0 / sumPrice.get(nodeId);
                a = Math.pow(a, beta);
                double b = probability.get(nodeId);
                b = Math.pow(b, alpha);
                sum = sum + a * b;
            }
        }
        //死循环异常
        if (sum == 0) {
            throw new DeadCircleException("计算死循环，重新计算");
        }
        return sum;
    }


    @Override
    public void releasePheromone(Map<Long, RoadHandleBo> roadHandleBoMap, HandleBo handleBo) {

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
