package com.power.grid.plan.service.algorithm;

import com.power.grid.plan.dto.bo.RoadHandleBo;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 判断是否到达
 * @author yubin
 * @date 2021/1/14 18:36
 */
@Component
public class Arrive {

    public void arrive(Map<Long, RoadHandleBo> roadHandleBoMap,Long start, Long end) {

        LinkedList<Long> initPath=new LinkedList<>();

        arrivePath(roadHandleBoMap,initPath,start,end);
    }

    private void arrivePath(Map<Long, RoadHandleBo> roadHandleBoMap, LinkedList<Long> initPath, Long start, Long end) {
        RoadHandleBo roadHandleBo = roadHandleBoMap.get(start);
        Set<Long> nodeSet = roadHandleBo.getProbability().keySet();
        for (Long node : nodeSet) {
            LinkedList<Long> path = new LinkedList<>(initPath);
            if (path.contains(node)) {
                System.out.println("死循环路径"+path);
                continue;
            }
            if (path.contains(end)){
                System.out.println("正确路径："+path);
                continue;
            }
                path.add(node);
            arrivePath(roadHandleBoMap, path, node, end);
        }
    }


}
