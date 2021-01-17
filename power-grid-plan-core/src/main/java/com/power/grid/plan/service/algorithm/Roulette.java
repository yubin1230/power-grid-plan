package com.power.grid.plan.service.algorithm;

import com.google.common.collect.Sets;
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

    /**
     * 信息素浓度的影响因子
     */
    private int alpha = 1;

    /**
     * 设置为5
     */
    private int beta = 5;
    /**
     * 循环银子，根据选择数量，最少循环10次，次数越多，路径准确性越高
     */
    private int loop = 15;

    public WalkBo calculate(RoadHandleBo roadHandleBo, Set<Long> deadIds, Set<Long> processedIdSet) {
//        long startTime = System.currentTimeMillis();
//        long startTime1 = System.currentTimeMillis() * 1000;
//        long startNanoTime = System.nanoTime(); // 纳秒
//        long start = startTime1 + (startNanoTime - startTime1 / 1000000 * 1000000) / 1000;
        Map<Long, Double> probability = roadHandleBo.getProbability();
        Map<Long, Double> sumPrice = roadHandleBo.getSumPrice();
        List<Long> keyList = new ArrayList<>(probability.keySet());

        //无下一节点或下一节点在已行走路线中
//        if (deadIds.containsAll(probability.keySet())) {
//            return new WalkBo(true);
//        }
//        if(processedIdSet.containsAll(probability.keySet())){
//            return new WalkBo(true);
//        }

//        long endTime1 = System.currentTimeMillis() * 1000;
//        long endNanoTime = System.nanoTime(); // 纳秒
//        long end = endTime1 + (endNanoTime - endTime1 / 1000000 * 1000000) / 1000;
//        long endTime = System.currentTimeMillis();
//        System.out.println(Thread.currentThread().getName() + "判断死亡节点耗时：" + (end - start)  + "微秒");
//        startTime = System.currentTimeMillis();
//         startTime1 = System.currentTimeMillis() * 1000;
//         startNanoTime = System.nanoTime(); // 纳秒
//         start = startTime1 + (startNanoTime - startTime1 / 1000000 * 1000000) / 1000;
        //删除已经处理过的节点
//        Set<Long> keySet = new HashSet<>(keyList);
//        noProcessedIds.forEach(no -> {
//            if (keySet.contains(no)) {
//                keyList.remove(no);
//            }
//        });
        Iterator<Long> iterator=keyList.iterator();
        while(iterator.hasNext()){
            Long next=iterator.next();
            if(deadIds.contains(next)){
                iterator.remove();
            }
            if(processedIdSet.contains(next)){
                iterator.remove();
            }
        }
        if(keyList.size()==0){
            return new WalkBo(true);
        }
//        endTime = System.currentTimeMillis();
//         endTime1 = System.currentTimeMillis() * 1000;
//         endNanoTime = System.nanoTime(); // 纳秒
//         end = endTime1 + (endNanoTime - endTime1 / 1000000 * 1000000) / 1000;
//        System.out.println(Thread.currentThread().getName() + "删除无效节点耗时：" + (end - start)  + "微秒");
//        startTime1 = System.currentTimeMillis() * 1000;
//        startNanoTime = System.nanoTime(); // 纳秒
//        start = startTime1 + (startNanoTime - startTime1 / 1000000 * 1000000) / 1000;
//        startTime = System.currentTimeMillis();
        //轮盘赌
        double sum = 0;

        Long nodeId = keyList.get(0);
        //轮盘赌初始因子
        double a = sum_single_possible(probability, sumPrice, keyList);

//        endTime = System.currentTimeMillis();
//        endTime1 = System.currentTimeMillis() * 1000;
//        endNanoTime = System.nanoTime(); // 纳秒
//        end = endTime1 + (endNanoTime - endTime1 / 1000000 * 1000000) / 1000;
//        System.out.println(Thread.currentThread().getName() + "初始节点因子耗时：" + (end - start)  + "微秒");
//        startTime1 = System.currentTimeMillis() * 1000;
//        startNanoTime = System.nanoTime(); // 纳秒
//        start = startTime1 + (startNanoTime - startTime1 / 1000000 * 1000000) / 1000;
//        startTime = System.currentTimeMillis();
        int maxSum = loop / keyList.size();
        if(keyList.size()==1){
            return new WalkBo(nodeId);
        }
        while (sum < maxSum) {
            Random random = new Random();
            int index = random.nextInt(keyList.size());
            nodeId = keyList.get(index);
            double n = 1.0 / sumPrice.get(nodeId);
            n = Math.pow(n, beta);
            double t = probability.get(nodeId);
            t = Math.pow(t, alpha);
            sum = sum + n * t * 1.0 / a;
//            i++;
        }

//        endTime = System.currentTimeMillis();
//        endTime1 = System.currentTimeMillis() * 1000;
//        endNanoTime = System.nanoTime(); // 纳秒
//        end = endTime1 + (endNanoTime - endTime1 / 1000000 * 1000000) / 1000;
//        System.out.println(Thread.currentThread().getName() + "轮盘赌耗时：" + (end - start)  + "微秒");
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
}
