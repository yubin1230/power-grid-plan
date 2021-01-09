package com.power.grid.plan;


import java.io.Serializable;


/**
 * 接口返回数据基础包装类
 * 作者（@author wangchun8 部门： 技术发展部-中台研发部-交易平台组 ）
 * 版本（@version 1.0）
 * 创建、开发日期（@date 2020/6/28 14:36）
 **/
public class BaseResponse<T> implements Serializable {
    private static final long serialVersionUID = -7546258468396924924L;
    private String requestNo;
    private int code;
    private String message;
    private T data;

    public BaseResponse() {
    }

    public BaseResponse(String requestNo, int code, String message, T data) {
        this.requestNo = requestNo;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static BaseResponse<String> success(String requestNo) {
        return new BaseResponse<>(requestNo, ResponseCodeEnum.DEFAULT_SUCCESS.getCode(), ResponseCodeEnum.DEFAULT_SUCCESS.getMessage(), null);
    }

    public static <T> BaseResponse<T> success(String requestNo, T data) {
        return new BaseResponse<>(requestNo, ResponseCodeEnum.DEFAULT_SUCCESS.getCode(), ResponseCodeEnum.DEFAULT_SUCCESS.getMessage(), data);
    }

    public static <T> BaseResponse<T> success(String requestNo, String message, T data) {
        return new BaseResponse<>(requestNo, ResponseCodeEnum.DEFAULT_SUCCESS.getCode(), message, data);
    }

    public static <T> BaseResponse<T> repeatSuccess(String requestNo, T data) {
        return new BaseResponse<>(requestNo, ResponseCodeEnum.REPEAT_SUCCESS.getCode(), ResponseCodeEnum.REPEAT_SUCCESS.getMessage(), data);
    }

    public static <T> BaseResponse<T> error(String requestNo, ResponseCodeEnum messageCode) {
        return new BaseResponse<>(requestNo, messageCode.getCode(), messageCode.getMessage(), null);
    }

    public static <T> BaseResponse<T> error(String requestNo, Integer code, String errMsg) {
        return new BaseResponse<>(requestNo, code, errMsg, null);
    }

    public static <T> BaseResponse<T> error(String requestNo, Integer code, String errMsg, T data) {
        return new BaseResponse<>(requestNo, code, errMsg, data);
    }

    public String getRequestNo() {
        return requestNo;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return this.code == ResponseCodeEnum.DEFAULT_SUCCESS.getCode() || this.code == ResponseCodeEnum.REPEAT_SUCCESS.getCode();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"requestNo\":\"")
                .append(requestNo).append('\"');
        sb.append(",\"code\":")
                .append(code);
        sb.append(",\"message\":\"")
                .append(message).append('\"');
        sb.append(",\"data\":\"")
                .append(data).append('\"');
        sb.append('}');
        return sb.toString();
    }
}
