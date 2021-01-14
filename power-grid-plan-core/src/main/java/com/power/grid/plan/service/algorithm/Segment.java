package com.power.grid.plan.service.algorithm;

import com.power.grid.plan.dto.bo.RoadHandleBo;
import com.power.grid.plan.dto.bo.WalkBo;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 分段概率匹配算法
 * @author yubin
 * @date 2021/1/14 0:48
 */
@Component
public class Segment {

    /**
     * 信息素浓度的影响因子
     */
    private double alpha = 1;

    /**
     * 设置为
     */
    private double beta = 1;

    public WalkBo calculate(RoadHandleBo roadHandleBo, Set<Long> noProcessedIds) {
        Map<Long, Double> probability = roadHandleBo.getProbability();
        Map<Long, Double> sumPrice = roadHandleBo.getSumPrice();
        List<Long> keyList = new ArrayList<>(probability.keySet());

        //无下一节点或下一节点在已行走路线中
        if (noProcessedIds.containsAll(probability.keySet())) {
            return new WalkBo(true);
        }


        //删除已经处理过的节点
        Set<Long> keySet = new HashSet<>(keyList);
        noProcessedIds.forEach(no -> {
            if (keySet.contains(no)) {
                keyList.remove(no);
            }
        });
        Map<Long, Double> initProbabilityMap = new HashMap<>();

        double sumProbability = 0.0;
        for (Long nodeId : keyList) {
            double n = 1.0 / sumPrice.get(nodeId);
            n = n * beta;
            double t = probability.get(nodeId);
            t = t * alpha;
            double handleProbability = n * t;
            sumProbability += handleProbability;
            initProbabilityMap.put(nodeId, handleProbability);
        }

        Map<Long, Double> handleProbabilityMap = new LinkedHashMap<>();

        for (Map.Entry<Long, Double> entry : initProbabilityMap.entrySet()) {
            handleProbabilityMap.put(entry.getKey(), entry.getValue() / sumProbability);
        }
        Random random = new Random();
        double index = random.nextDouble();
        double start = 0.0;
        Long wale = keyList.get(0);
        for (Map.Entry<Long, Double> entry : handleProbabilityMap.entrySet()) {
            if (index >= start && index < entry.getValue() + start) {
                wale = entry.getKey();
                break;
            }
            start += entry.getValue();
        }
        return new WalkBo(wale);
    }

}
