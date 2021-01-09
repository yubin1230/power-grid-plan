package com.power.grid.plan.enums;

/**
 * 库存异常枚举
 *
 * @author wangyucheng
 * @date 2020-08-26 14:37
 */

public enum PowerExceptionEnum {

    PARAM_NOT_NULL(-1001, "%s 不能为空"),
    CONFIG_ERROR(-1001, "%s 配置错误"),
    ;
    /**
     * 错误码
     */
    private Integer code;
    /**
     * 错误信息
     */
    private String msg;

    /**
     *
     * @param code
     * @param msg
     */
    PowerExceptionEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
