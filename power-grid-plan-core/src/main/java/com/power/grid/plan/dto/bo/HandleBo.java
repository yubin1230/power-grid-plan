package com.power.grid.plan.dto.bo;

import lombok.Data;

import java.util.LinkedList;
import java.util.Objects;

/**
 * 处理业务类
 * @author yubin
 * @date 2020/12/5 10:40
 */
@Data
public class HandleBo {

    /**
    * 行走路径
    */
    private LinkedList<Long> handlePath;

    /**
    * 总造价
    */
    private Double sumPrice;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HandleBo handleBo = (HandleBo) o;
        return Objects.equals(handlePath, handleBo.handlePath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(handlePath, sumPrice);
    }

    public HandleBo() {
        this.handlePath = new LinkedList<>();
        this.sumPrice = Double.MAX_VALUE;
    }
}
