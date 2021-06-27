package com.power.grid.plan.vo;

import lombok.Data;


/**
 * 处理业务类
 * @author yubin
 * @date 2020/12/5 10:40
 */
@Data
public class CalculateVo {


    /**
     * 需求编码
     */
    private String needNo;

    /**
     * 方案名称
     */
    private String caseName;


    /**
     * 区域 1：A+ 2: A 3:B
     */
    private String area;

    /**
     * 总距离
     */
    private Double sumDistance;

    /**
     * 总造价
     */
    private Double sumPrice;

    /**
     * 网格编码
     */
    private String gridNo;

    /**
     * 校验结果
     */
    private boolean result = true;
}
