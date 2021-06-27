package com.power.grid.plan.util;

import lombok.Data;

/**
 * 百度地图返回
 * @author yubin
 * @date 2021/6/22 23:31
 */
@Data
public class BMapResponse {

    private int status;

    private BMapResult[] result;
}
