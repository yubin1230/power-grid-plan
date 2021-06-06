package com.power.grid.plan.dto.enums;

import com.power.grid.plan.ResponseCodeEnum;
import com.power.grid.plan.exception.BizException;

/**
 * 区域类型
 * @author yubin
 * @date 2021/6/6 11:54
 */
public enum AreaType {

    A1(0, "A+"),
    A2(1, "A"),
    B(2, "B");

    private int code;
    private String desc;

    private AreaType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static AreaType getAreaTypeMap(int code) {
        AreaType[] areaTypes = AreaType.values();
        for (AreaType areaType : areaTypes) {
            if (areaType.getCode() == code) return areaType;
        }
        throw new BizException(ResponseCodeEnum.DEFAULT_ERROR,"区域类型不存在");
    }


    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
