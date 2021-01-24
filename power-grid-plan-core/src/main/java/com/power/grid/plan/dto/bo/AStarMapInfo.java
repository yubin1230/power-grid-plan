package com.power.grid.plan.dto.bo;

import lombok.Data;

import java.util.Map;

/**
 * A星地图
 * @author yubin
 * @date 2021/1/24 23:25
 */
@Data
public class AStarMapInfo {
    private Map<Long, RoadHandleBo> roadHandleBoMap; // 二维数组的地图
    private Map<Long, NodeBo> nodeBoMap;
    private AStarNodeBo start; // 起始结点
    private AStarNodeBo end; // 最终结点
}
