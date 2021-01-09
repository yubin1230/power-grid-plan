package com.power.grid.plan.dto.vo;

import lombok.Data;


/**
 * 处理业务类
 * @author yubin
 * @date 2020/12/5 10:40
 */
@Data
public class HandleVo {

    /**
    * 行走路径
    */
    private String handlePath;

    /**
    * 总造价
    */
    private Double sumPrice;
}
