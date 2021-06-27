package com.power.grid.plan.util;

import com.power.grid.plan.dto.bo.LineBo;
import com.power.grid.plan.dto.bo.NodeBo;
import com.power.grid.plan.dto.bo.PointBo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 线面计算类
 * @author yubin
 * @date 2021/6/6 23:04
 */
public class LinePointUtil {

    public static List<PointBo> getPedalSimplexAndDistance(List<LineBo> lineBoList, NodeBo nodeBo) {
        List<PointBo> pointBoList = new ArrayList<>();
        lineBoList.forEach(lineBo -> {
            String[] pointStr = lineBo.getLinePoints().split(";");
            for (int i = 0; i < pointStr.length - 1; i++) {
                String[] startStr = pointStr[i].split(",");
                String[] endStr = pointStr[i].split(",");
                NodeBo start = new NodeBo(Double.parseDouble(startStr[0]), Double.parseDouble(startStr[1]));
                NodeBo end = new NodeBo(Double.parseDouble(endStr[0]), Double.parseDouble(endStr[1]));
                PointBo pointBo=MapUtil.pointToLine(start, end, nodeBo);
                pointBo.setLineBo(lineBo);
                pointBoList.add(pointBo);
            }
        });
        return pointBoList.stream().sorted(Comparator.comparingDouble(PointBo::getDistance)).collect(Collectors.toList());
    }
}
