package com.power.grid.plan.vo;

import com.power.grid.plan.BaseRequest;
import lombok.Data;



/**
 * 计算视图
 * @author yubin
 * @date 2021/6/24 1:53
 */
@Data
public class CalculateRequest extends BaseRequest {

    private Integer[] cabinetType;
    private String needNo;
}
