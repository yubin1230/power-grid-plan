package com.power.grid.plan.util;

import com.power.grid.plan.dto.bo.HandleBo;
import com.power.grid.plan.dto.bo.RoadHandleBo;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 删除回路工具类
 *
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
        double bestPrice = getSubPrice(roadHandleBoMap, bestPath);
        HandleBo bestBo = new HandleBo();
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

    public static void arrivePath(Map<Long, RoadHandleBo> roadHandleBoMap, List<Long> initPath, List<List<Long>> pathList, Long start, Long end, int loop) {
        RoadHandleBo roadHandleBo = roadHandleBoMap.get(start);
        Set<Long> nodeSet = roadHandleBo.getSumPrice().keySet();
        for (Long node : nodeSet) {

            int curLoop = loop;

            List<Long> path = new ArrayList<>(initPath);

            if (path.contains(node)) {
                System.out.println("死循环路径" + path);
                continue;
            }
            if (path.contains(end)) {
                System.out.println("正确路径：" + path);
                pathList.add(path);
                continue;
            }

            if (curLoop == 0) {
                continue;
            }
            path.add(node);
            arrivePath(roadHandleBoMap, path, pathList, node, end, --curLoop);
        }
    }



}

