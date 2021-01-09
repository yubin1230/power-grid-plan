package com.power.grid.plan.dto.bo;

import lombok.Data;

import java.util.Map;

/**
 * 概率业务类
 * @author yubin
 * @date 2020/12/1 0:02
 */
@Data
public class RoadHandleBo {

    /**
    * 当前节点id
    */
    private Long nodeId;

    /**
    * 当前节点至其他节点概率
    */
    private Map<Long,Double> probability;

    /**
    * 至目的路段总造价
    */
    private Map<Long,Double> sumPrice;
}
