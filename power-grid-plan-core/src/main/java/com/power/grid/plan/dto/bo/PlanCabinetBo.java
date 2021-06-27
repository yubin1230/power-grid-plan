package com.power.grid.plan.dto.bo;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class PlanCabinetBo {
    private Long id;

    private String cabinetNo;

    private String caseNo;

    private String stationName;

    private String lineNo;

    private String boxName;

    private Integer voltage;

    private Integer outLine;

    private Integer type;

    private Integer controlType;

    private Double longitude;

    private Double latitude;

    private String gridNo;
}