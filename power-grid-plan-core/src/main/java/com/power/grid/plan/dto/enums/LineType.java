package com.power.grid.plan.dto.enums;

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
