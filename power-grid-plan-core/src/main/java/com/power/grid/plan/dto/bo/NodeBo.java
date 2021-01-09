package com.power.grid.plan.dto.bo;

import lombok.Data;

/**
 * 节点业务类
 * @author yubin
 * @date 2020/11/30 23:21
 */
@Data
public class NodeBo {

    /**
     * 节点id
     */
    private long id;

    /**
     * 经度
     */
    private double longitude;

    /**
     * 纬度
     */
    private double latitude;


    public NodeBo() {
    }

    public NodeBo(long id, double longitude, double latitude) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
