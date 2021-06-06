package com.power.grid.plan.exception;

import com.power.grid.plan.ResponseCodeEnum;

/**
 * 异常类
 *
 * @author wangyucheng on 2020/8/20 2:17 下午
 */
public class BizException extends RuntimeException {
    private Integer code;
    private String msg;


    public BizException(ResponseCodeEnum responseCodeEnum, String param) {
        super(String.format(responseCodeEnum.getMessage(), param));
        this.code = responseCodeEnum.getCode();
        this.msg = String.format("%s:%s",responseCodeEnum.getMessage(), param);
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
