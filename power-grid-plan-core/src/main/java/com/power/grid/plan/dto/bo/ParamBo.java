package com.power.grid.plan.dto.bo;

import lombok.Builder;
import lombok.Data;

/**
 * 初始化参数
 * @author yubin
 * @date 2021/6/20 23:36
 */
@Data
@Builder
public class ParamBo {

    /**
    * 折算系数
    */
    private Integer conversionRatio;


    /**
    * 最大容量
    */
    private Integer maximumCapacity;

    /**
    * 自动化覆盖率要求
    */
    private Integer automationRatio;

    /**
    * 电缆接入阈值
    */
    private Integer lineThreshold;

    /**
    * 线路调整系数
    */
    private Double lineThresholdRatio;

    /**
    * 环网柜负荷
    */
    private Double load;

    /**
    * 环网柜负荷调整系数
    */
    private Double loadRatio;

    /**
    * 环网柜负荷容量
    */
    private Integer capacity;

    /**
     * 环网柜负荷调整系数
     */
    private Double capacityRatio;

    /**
    * 用户数
    */
    private Integer users;

    /**
    * 馈线节点数量
    */
    private Integer nodeNum;


}
