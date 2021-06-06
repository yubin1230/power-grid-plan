package com.power.grid.plan.dto.bo;

import lombok.Data;

/**
 * 点业务类
 * @author yubin
 * @date 2021/6/6 20:56
 */
@Data
public class PointBo {
    private Double longitude;
    private Double latitude;
    private Double distance;

    public PointBo(Double longitude, Double latitude, Double distance) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.distance = distance;
    }
}
