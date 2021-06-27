package com.power.grid.plan.dto.enums;

import com.power.grid.plan.ResponseCodeEnum;
import com.power.grid.plan.exception.BizException;

/**
 * 线路类型
 * @author yubin
 * @date 2021/6/6 11:54
 */
public enum LineType {

    ALREADY_EXISTING(0, "已有"),
    PLAN(1, "规划");

    private int code;
    private String desc;


    public static LineType getLineTypeMap(int code) {
        LineType[] lineTypes = LineType.values();
        for (LineType lineType : lineTypes) {
            if (lineType.getCode() == code) return lineType;
        }
        throw new BizException(ResponseCodeEnum.DEFAULT_ERROR,"区域类型不存在");
    }

    private LineType(int code, String desc) {
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
