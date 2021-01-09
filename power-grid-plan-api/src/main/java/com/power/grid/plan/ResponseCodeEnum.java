package com.power.grid.plan;

/**
 * 接口操作结果状态码基础类
 * 作者（@author wangchun8 部门： 技术发展部-中台研发部-交易平台组 ）
 * 版本（@version 1.0）
 * 创建、开发日期（@date 2020/6/28 14:41）
 **/
public enum ResponseCodeEnum {
    DEFAULT(-1, "未知错误，请重试"),
    DEFAULT_SUCCESS(200, "成功"),
    DEFAULT_ERROR(500, "系统异常"),
    REPEAT_SUCCESS(201, "重复处理"),
    ;

    int code;
    String message;

    ResponseCodeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static ResponseCodeEnum parse(int code) {
        for (ResponseCodeEnum value : ResponseCodeEnum.values()) {
            if (value.code == code) {
                return value;
            }
        }
        return DEFAULT;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
