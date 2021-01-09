package com.power.grid.plan.exception;

import com.power.grid.plan.enums.PowerExceptionEnum;

/**
 * 异常类
 *
 * @author wangyucheng on 2020/8/20 2:17 下午
 */
public class BizException extends RuntimeException {
    private Integer code;
    private String msg;


    public BizException(PowerExceptionEnum stockExceptionEnum, String param) {
        super(String.format(stockExceptionEnum.getMsg(), param));
        this.code = stockExceptionEnum.getCode();
        this.msg = String.format(stockExceptionEnum.getMsg(), param);
    }



    public static void checkArgument(boolean expression, PowerExceptionEnum stockExceptionEnum, String param) {
        if (expression) {
            throw new BizException(stockExceptionEnum, param);
        }
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
