package com.power.grid.plan;


import java.io.Serializable;

/**
 * 接口入参基础起包装类
 * 作者（@author wangchun8 部门： 技术发展部-中台研发部-交易平台组 ）
 * 版本（@version 1.0）
 * 创建、开发日期（@date 2020/6/28 14:36）
 **/
public class BaseRequest implements Serializable {

    private static final long serialVersionUID = -3434049849262683880L;

    protected String requestNo;

    public BaseRequest() {
    }

    public BaseRequest(String requestNo) {
        this.requestNo = requestNo;
    }

    public String getRequestNo() {
        return requestNo;
    }

    public void setRequestNo(String requestNo) {
        this.requestNo = requestNo;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"requestNo\":\"")
                .append(requestNo).append('\"');
        sb.append('}');
        return sb.toString();
    }
}
