package com.power.grid.plan.vo;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class LineVo {
    private Integer id;

    private String lineNo;

    private Integer lineType;

    private String lineTypeName;

    private String linePoints;

    private Integer linePower;

    private Integer linePowerUsed;

    private Integer planLinePowerUsed;

    private String stationName;

    private Integer overload;

    private String overloadType;

    private String busNo;

    private String flName;

    private Integer overallDesirability;

    private String gridNo;
}