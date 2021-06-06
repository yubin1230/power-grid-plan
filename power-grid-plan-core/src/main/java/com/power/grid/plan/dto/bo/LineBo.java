package com.power.grid.plan.dto.bo;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class LineBo {
    private Integer id;

    private String lineNo;

    private Integer lineType;

    private String linePoints;

    private Integer linePower;

    private Integer linePowerUsed;

    private Integer planLinePowerUsed;

    private String stationName;

    private Integer overload;

    private String busNo;

    private String flName;

    private Integer overallDesirability;

    private String gridNo;
}