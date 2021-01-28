package com.power.grid.plan.dto.bo;

import lombok.Data;

import java.util.Map;

/**
 * A 星价格距离数据
 * @author yubin
 * @date 2020/12/1 0:02
 */
@Data
public class AStarRoadHandleBo {

    /**
    * 当前节点id
    */
    private Long nodeId;

    /**
    * 当前节点至其他节点距离
    */
    private Map<Long,Double> distance;

    /**
    * 至目的路段总造价
    */
    private Map<Long,Double> sumPrice;
}
