package com.power.grid.plan.dto.bo;

import lombok.Data;

/**
 * 行走对象
 * @author yubin
 * @date 2021/1/11 22:51
 */
@Data
public class WalkBo {

    /**
    * 下一节点
    */
    private Long next;

    /**
    * 是否死循环
    */
    private boolean isDead=false;

    public WalkBo(Long next) {
        this.next = next;
    }

    public WalkBo(boolean isDead) {
        this.isDead = isDead;
    }

    public WalkBo() {
    }
}
