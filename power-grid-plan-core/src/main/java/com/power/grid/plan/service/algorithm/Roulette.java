package com.power.grid.plan.service.algorithm;

import com.google.common.collect.Sets;
import com.power.grid.plan.Constants;
import com.power.grid.plan.dto.bo.RoadHandleBo;
import com.power.grid.plan.dto.bo.WalkBo;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 轮盘赌算法
 * @author yubin
 * @date 2021/1/14 1:15
 */
@Component
public class Roulette {


    public WalkBo calculate(RoadHandleBo roadHandleBo, Set<Long> deadIds, Set<Long> processedIdSet) {
        Map<Long, Double> probability = roadHandleBo.getProbability();
        Map<Long, Double> sumPrice = roadHandleBo.getSumPrice();
        List<Long> keyList = new ArrayList<>(probability.keySet());

        //无下一节点或下一节点在已行走路线中

        Iterator<Long> iterator = keyList.iterator();
        while (iterator.hasNext()) {
            Long next = iterator.next();
            if (deadIds.contains(next)) {
                iterator.remove();
            }
            if (processedIdSet.contains(next)) {
                iterator.remove();
            }
        }
        if (keyList.size() == 0) {
            return new WalkBo(true);
        }
        //轮盘赌
        double sum = 0;

        Long nodeId = keyList.get(0);
        //轮盘赌初始因子
        double a = sum_single_possible(probability, sumPrice, keyList);

        int maxSum = Constants.ROULETTE_FACTOR / keyList.size();
        if (keyList.size() == 1) {
            return new WalkBo(nodeId);
        }
        while (sum < maxSum) {
            Random random = new Random();
            int index = random.nextInt(keyList.size());
            nodeId = keyList.get(index);
            double n = sumPrice.get(nodeId);
            n = Math.pow(n, Constants.BETA);
            double t = probability.get(nodeId);
            t = Math.pow(t, Constants.ALPHA);
            sum = sum + n * t * 1.0 / a;
        }
        return new WalkBo(nodeId);

    }

    private double sum_single_possible(Map<Long, Double> probability, Map<Long, Double> sumPrice, List<Long> keyList) {
        //根据公式计算概率
        double sum = 0;
        for (Long nodeId : keyList) {
            double a = sumPrice.get(nodeId);
            a = Math.pow(a, Constants.BETA);
            double b = probability.get(nodeId);
            b = Math.pow(b, Constants.ALPHA);
            sum = sum + a * b;

        }
        return sum;
    }
}
