package com.power.grid.plan.util;

import com.power.grid.plan.ResponseCodeEnum;
import com.power.grid.plan.dto.bo.*;
import com.power.grid.plan.exception.BizException;
import org.apache.commons.collections.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 线面计算类
 * @author yubin
 * @date 2021/6/6 23:04
 */

public class RoadPointUtil {

    public static NodeBo getInsinuationRoad(CalculateContextBo calculateContextBo, NodeBo nodeBo) {
        List<RoadBo> roadBoList = calculateContextBo.getRoadBoList();
        Map<String, NodeBo> nodeBoMap = calculateContextBo.getNodeBoMap();
        List<PointBo> pointBoList = new ArrayList<>();
        //获取每一段路径距离
        for (RoadBo roadBo : roadBoList) {
            NodeBo start = nodeBoMap.get(String.valueOf(roadBo.getStartNodeId()));
            NodeBo end = nodeBoMap.get(String.valueOf(roadBo.getEndNodeId()));
            if (Objects.isNull(start) || Objects.isNull(end)) {
                continue;
            }
            PointBo pointBo = MapUtil.pointToLine(start, end, nodeBo);
            pointBo.setRoadBo(roadBo);
            pointBoList.add(pointBo);
        }
        pointBoList = pointBoList.stream().sorted(Comparator.comparingDouble(PointBo::getDistance)).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(pointBoList)) {
            throw new BizException(ResponseCodeEnum.DEFAULT_ERROR, "无有效路段信息");
        }
        //最短垂直坐标
        PointBo pointBo = pointBoList.get(0);
        RoadBo roadBo = pointBo.getRoadBo();

        //新增最短节点
        NodeBo minNodeBo = NodeBo.builder().id(-1L).longitude(pointBo.getLongitude()).latitude(pointBo.getLatitude()).build();
        //添加两个个新路段
        NodeBo start = nodeBoMap.get(String.valueOf(roadBo.getStartNodeId()));
        NodeBo end = nodeBoMap.get(String.valueOf(roadBo.getEndNodeId()));
        double distanceStart = MapUtil.getDistance(start, minNodeBo);
        double distanceEnd = MapUtil.getDistance(minNodeBo, end);
        //环网柜在起点
        if (distanceStart == 0) {
            return start;
        }
        //环网柜在终点
        if (distanceEnd == 0) {
            return end;
        }
        //将原有路段分2段
        //删除原有路段
        roadBoList.remove(roadBo);

        RoadBo toStart = RoadBo.builder().id(-1L).roadNo(roadBo.getRoadNo()).startNodeId(roadBo.getStartNodeId()).endNodeId(minNodeBo.getId()).price(roadBo.getPrice()).distance(distanceStart).build();
        RoadBo toEnd = RoadBo.builder().id(-2L).roadNo(roadBo.getRoadNo()).startNodeId(minNodeBo.getId()).endNodeId(roadBo.getEndNodeId()).price(roadBo.getPrice()).distance(distanceEnd).build();
        nodeBoMap.put(String.valueOf(minNodeBo.getId()), minNodeBo);
        roadBoList.add(toStart);
        roadBoList.add(toEnd);
        return minNodeBo;
    }
}
