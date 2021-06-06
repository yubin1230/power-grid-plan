package com.power.grid.plan.service.astar;

import com.power.grid.plan.dto.bo.*;
import com.power.grid.plan.exception.UnableArriveException;
import com.power.grid.plan.service.coordinate.CoordinateDistance;

import java.util.*;

/**
 * A星算法
 * @author yubin
 * @date 2021/1/25 0:52
 */
public class AStar {

    Queue<AStarNodeBo> openList = new PriorityQueue<>(); // 优先队列(升序)
    List<AStarNodeBo> closeList = new ArrayList<>();

    /**
     * 开始算法
     */
    public HandleBo start(AStarMapInfo mapInfo) {
        // clean
        openList.clear();
        closeList.clear();
        // 开始搜索
        openList.add(mapInfo.getStart());
        return moveNodes(mapInfo);
    }

    /**
     * 移动当前结点
     */
    private HandleBo moveNodes(AStarMapInfo mapInfo) {
        while (!openList.isEmpty()) {
            AStarNodeBo current = openList.poll();
            closeList.add(current);
            addNeighborNodeInOpen(mapInfo, current);
            if (isCoordinateInClose(mapInfo.getEnd().getNodeBo().getId())) {
                return buildHandleBo(mapInfo.getEnd());
            }
        }
        throw new UnableArriveException("无法到达");
    }

    /**
     * 在二维数组中绘制路径
     */
    private HandleBo buildHandleBo(AStarNodeBo end) {
        HandleBo handleBo = new HandleBo();
        handleBo.setSumPrice(end.getG());
        LinkedList<Long> path = new LinkedList<>();
        while (end != null) {
            Long nodeId = end.getNodeBo().getId();
            path.add(nodeId);
            end = end.getParent();
        }
        Collections.reverse(path);
        handleBo.setHandlePath(path);
        return handleBo;
    }

    /**
     * 添加所有邻结点到open表
     */
    private void addNeighborNodeInOpen(AStarMapInfo mapInfo, AStarNodeBo current) {

        Map<Long, AStarRoadHandleBo> roadHandleBoMap = mapInfo.getRoadHandleBoMap();
        Long nodeId = current.getNodeBo().getId();
        AStarRoadHandleBo roadHandleBo = roadHandleBoMap.get(nodeId);
        Map<Long, Double> distance = roadHandleBo.getDistance();
        distance.forEach((key, value) -> addNeighborNodeInOpen(mapInfo, current, key, value));
    }

    /**
     * 添加一个邻结点到open表
     */
    private void addNeighborNodeInOpen(AStarMapInfo mapInfo, AStarNodeBo current, Long nodeId, Double value) {
        if (canAddNodeToOpen(nodeId)) {
            AStarNodeBo end = mapInfo.getEnd();
            double G = current.getG() + value; // 计算邻结点的G值
            AStarNodeBo child = findNodeInOpen(nodeId);
            NodeBo nodeBo = mapInfo.getNodeBoMap().get(nodeId);
            if (child == null) {
                double H = calcH(end.getNodeBo(), nodeBo, mapInfo.getHFactor()); // 计算H值
                if (nodeId.equals(end.getNodeBo().getId())) {
                    child = end;
                    child.setParent(current);
                    child.setG(G);
                    child.setH(H);
                } else {
                    child = new AStarNodeBo(nodeBo, current, G, H);
                }
                openList.add(child);
            } else if (child.getG() > G) {
                child.setG(G);
                child.setParent(current);
                openList.add(child);
            }
        }
    }

    /**
     * 计算H的估值：“曼哈顿”法，坐标分别取差值相加
     */
    private double calcH(NodeBo end, NodeBo nodeBo, double hFactor) {
        return (CoordinateDistance.GetDistance(end.getLongitude(), end.getLatitude(), nodeBo.getLongitude(), end.getLatitude()) / 1000
                + CoordinateDistance.GetDistance(end.getLongitude(), end.getLatitude(), end.getLongitude(), nodeBo.getLatitude()) / 1000) * hFactor;
    }

    /**
     * 从Open列表中查找结点
     */
    private AStarNodeBo findNodeInOpen(Long nodeId) {
        if (Objects.isNull(nodeId) || openList.isEmpty()) return null;
        for (AStarNodeBo node : openList) {
            if (nodeId.equals(node.getNodeBo().getId())) {
                return node;
            }
        }
        return null;
    }

    /**
     * 判断结点能否放入Open列表
     */
    private boolean canAddNodeToOpen(Long nodeId) {
        // 判断结点是否存在close表
        if (isCoordinateInClose(nodeId)) return false;

        return true;
    }

    /**
     * 判断坐标是否在close表中
     */
    private boolean isCoordinateInClose(Long nodeId) {
        if (closeList.isEmpty()) return false;
        for (AStarNodeBo node : closeList) {
            if (nodeId.equals(node.getNodeBo().getId())) {
                return true;
            }
        }
        return false;
    }
}
