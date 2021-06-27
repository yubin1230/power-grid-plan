package com.power.grid.plan.dto.bo;

import lombok.Builder;
import lombok.Data;

/**
 * 节点业务类
 * @author yubin
 * @date 2020/11/30 23:21
 */
@Data
@Builder
public class NodeBo {

    /**
     * 节点id
     */
    private Long id;

    /**
    * 节点编码
    */
    private String nodeNo;

    /**
     * 经度
     */
    private Double longitude;

    /**
     * 纬度
     */
    private Double latitude;


    public NodeBo() {
    }

    public NodeBo(Long id, Double longitude, Double latitude) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public NodeBo(Double longitude, Double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public NodeBo(Long id, String nodeNo, Double longitude, Double latitude) {
        this.id = id;
        this.nodeNo = nodeNo;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    @Override
    public String toString() {
        return "NodeBo{" +
                "id=" + id +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                '}';
    }
}
