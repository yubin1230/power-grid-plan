package com.power.grid.plan.dto.bo;

import lombok.Builder;
import lombok.Data;

/**
 * 点业务类
 * @author yubin
 * @date 2021/6/6 20:56
 */
@Data
@Builder
public class PointBo {
    private Double longitude;
    private Double latitude;
    private Double distance;
    private RoadBo roadBo;
    private LineBo lineBo;

    public PointBo(Double longitude, Double latitude, Double distance) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.distance = distance;
    }

    public PointBo(Double longitude, Double latitude, Double distance, RoadBo roadBo, LineBo lineBo) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.distance = distance;
        this.roadBo = roadBo;
        this.lineBo = lineBo;

    }
}
