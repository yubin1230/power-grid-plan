package com.power.grid.plan.util;

import com.power.grid.plan.dto.bo.HandleBo;
import com.power.grid.plan.dto.bo.RoadHandleBo;

import java.util.*;

/**
 * 删除回路工具类
 * @author yubin
 * @date 2021/1/17 23:13
 */
public class RemoveLoopRoad {

    public static HandleBo removeLoopRoad(Map<Long, RoadHandleBo> roadHandleBoMap, HandleBo handleBo) {

        LinkedList<Long> handlePath = handleBo.getHandlePath();
        LinkedList<Long> bestPath = new LinkedList<>();

        for (int i = 0; i < handlePath.size() - 1; i++) {
            RoadHandleBo roadHandleBo = roadHandleBoMap.get(handlePath.get(i));
            Map<Long, Double> sumPrice = new HashMap<>(roadHandleBo.getSumPrice());
            bestPath.add(handlePath.get(i));
            //下一个节点只有一个，无需判断
            if (sumPrice.size() == 1) {
                continue;
            }
            sumPrice.remove(handlePath.get(i + 1));
            int b_index = -1;
            for (Map.Entry<Long, Double> entry : sumPrice.entrySet()) {
                Long nodeId = entry.getKey();
                Double price = entry.getValue();
                int a_index = -1;
                for (int j = i + 2; j < handlePath.size(); j++) {
                    if (nodeId.equals(handlePath.get(j))) {
                        if (price < getSubPrice(roadHandleBoMap, handlePath.subList(i, j + 1))) {
                            a_index = j;
                            break;
                        }
                    }
                }
                if (a_index > b_index) {
                    b_index = a_index;
                }
            }
            if (b_index > i) {
                i = b_index - 1;
            }
        }
        bestPath.add(handlePath.getLast());
        double bestPrice=getSubPrice(roadHandleBoMap,bestPath);
        HandleBo bestBo=new HandleBo();
        bestBo.setSumPrice(bestPrice);
        bestBo.setHandlePath(bestPath);
        return bestBo;
    }

    private static Double getSubPrice(Map<Long, RoadHandleBo> roadHandleBoMap, List<Long> pathList) {
        double price = 0.0;
        for (int i = 0; i < pathList.size() - 1; i++) {
            RoadHandleBo roadHandleBo = roadHandleBoMap.get(pathList.get(i));
            price += roadHandleBo.getSumPrice().get(pathList.get(i + 1));
        }
        return price;
    }


    public static void main(String[] args) {

        Map<Long, RoadHandleBo> roadHandleBoMap = getRoadHandleBoMap();
        LinkedList<Long> path = new LinkedList<>(Arrays.asList(1L, 2L, 5L, 6L, 7L, 3L, 4L, 8L));
        HandleBo handleBo = new HandleBo();
        handleBo.setHandlePath(path);
        handleBo.setSumPrice(8.0);
        HandleBo bestBo = removeLoopRoad(roadHandleBoMap, handleBo);
        System.out.println(bestBo);
    }


    public static Map<Long, RoadHandleBo> getRoadHandleBoMap() {
        Map<Long, RoadHandleBo> roadHandleBoMap = new HashMap<>();
        RoadHandleBo bo1 = new RoadHandleBo();
        bo1.setNodeId(1L);
        Map<Long, Double> sumPrice1 = new HashMap<>();
        sumPrice1.put(2L, 1.0);
        bo1.setSumPrice(sumPrice1);

        RoadHandleBo bo2 = new RoadHandleBo();
        bo2.setNodeId(2L);
        Map<Long, Double> sumPrice2 = new HashMap<>();
        sumPrice2.put(3L, 1.0);
        sumPrice2.put(5L, 1.0);
        sumPrice2.put(8L, 10.0);
        bo2.setSumPrice(sumPrice2);

        RoadHandleBo bo3 = new RoadHandleBo();
        bo3.setNodeId(3L);
        Map<Long, Double> sumPrice3 = new HashMap<>();
        sumPrice3.put(4L, 1.0);
        sumPrice3.put(8L, 1.0);
        bo3.setSumPrice(sumPrice3);

        RoadHandleBo bo4 = new RoadHandleBo();
        bo4.setNodeId(4L);
        Map<Long, Double> sumPrice4 = new HashMap<>();
        sumPrice4.put(8L, 1.0);
        bo4.setSumPrice(sumPrice4);

        RoadHandleBo bo5 = new RoadHandleBo();
        bo5.setNodeId(5L);
        Map<Long, Double> sumPrice5 = new HashMap<>();
        sumPrice5.put(6L, 1.0);
        bo5.setSumPrice(sumPrice5);

        RoadHandleBo bo6 = new RoadHandleBo();
        bo6.setNodeId(6L);
        Map<Long, Double> sumPrice6 = new HashMap<>();
        sumPrice6.put(7L, 1.0);
        bo6.setSumPrice(sumPrice6);

        RoadHandleBo bo7 = new RoadHandleBo();
        bo7.setNodeId(7L);
        Map<Long, Double> sumPrice7 = new HashMap<>();
        sumPrice7.put(3L, 1.0);
        bo7.setSumPrice(sumPrice7);

        RoadHandleBo bo8 = new RoadHandleBo();
        bo8.setNodeId(8L);
        Map<Long, Double> sumPrice8 = new HashMap<>();
        bo8.setSumPrice(sumPrice8);

        roadHandleBoMap.put(1L, bo1);
        roadHandleBoMap.put(2L, bo2);
        roadHandleBoMap.put(3L, bo3);
        roadHandleBoMap.put(4L, bo4);
        roadHandleBoMap.put(5L, bo5);
        roadHandleBoMap.put(6L, bo6);
        roadHandleBoMap.put(7L, bo7);
        roadHandleBoMap.put(8L, bo8);
        return roadHandleBoMap;
    }

}
