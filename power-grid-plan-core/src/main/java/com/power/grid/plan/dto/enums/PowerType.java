package com.power.grid.plan.dto.enums;

import com.power.grid.plan.ResponseCodeEnum;
import com.power.grid.plan.exception.BizException;

/**
 * 区域类型
 * @author yubin
 * @date 2021/6/6 11:54
 */
public enum PowerType {

    INDUSTRY(0, "工业"),
    BUSINESS(1, "商业"),
    RESIDENT(2, "居民");

    private int code;
    private String desc;

    private PowerType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static PowerType getPowerTypeMap(int code) {
        PowerType[] areaTypes = PowerType.values();
        for (PowerType areaType : areaTypes) {
            if (areaType.getCode() == code) return areaType;
        }
        throw new BizException(ResponseCodeEnum.DEFAULT_ERROR,"用电类型不存在");
    }


    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
