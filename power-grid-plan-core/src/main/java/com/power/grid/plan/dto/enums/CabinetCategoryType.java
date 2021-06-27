package com.power.grid.plan.dto.enums;

import com.power.grid.plan.ResponseCodeEnum;
import com.power.grid.plan.exception.BizException;

/**
 * 环网柜类别
 * @author yubin
 * @date 2021/6/6 11:54
 */
public enum CabinetCategoryType {

    EXIST_LINE_EXIST_CATEGORY(0, "现有线路现有环网柜"),
    EXIST_LINE_PEDAL_PLAN_CATEGORY(1, "现有线路最短线路垂足规划环网柜"),
    EXIST_LINE_PLAN_CATEGORY(2, "现有线路规划环网柜"),
    PLAN_LINE_PLAN_CATEGORY(3, "规划线路规划环网柜");

    private int code;
    private String desc;


    public static CabinetCategoryType getCabinetCategoryTypeMap(int code) {
        CabinetCategoryType[] cabinetCategoryTypes = CabinetCategoryType.values();
        for (CabinetCategoryType cabinetCategoryType : cabinetCategoryTypes) {
            if (cabinetCategoryType.getCode() == code) return cabinetCategoryType;
        }
        throw new BizException(ResponseCodeEnum.DEFAULT_ERROR,"环网柜类别不存在");
    }

    private CabinetCategoryType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }


    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
