package com.power.grid.plan;

import java.io.Serializable;
import java.util.List;

/**
 * page公共类
 * 作者（@author jinpeng6 部门： 技术发展部-中台研发部-交易平台组 ）
 * 版本（@version 1.0）
 * 创建、开发日期（@date 2020/8/12 15:55）
 **/
public class Page<T> implements Serializable {
    private static final long serialVersionUID = -7525190265598014595L;


    /**
     * 第几页
     */
    private Integer currentPage = 1;

    /**
     * 展示多少条数据
     */
    private Integer pageSize = 100;

    private Integer pages;
    private Integer total;
    private List<T> content;

    public Page() {
    }


    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"currentPage\":")
                .append(currentPage);
        sb.append(",\"pageSize\":")
                .append(pageSize);
        sb.append(",\"pages\":")
                .append(pages);
        sb.append(",\"total\":")
                .append(total);
        sb.append(",\"content\":")
                .append(content);
        sb.append('}');
        return sb.toString();
    }
}

