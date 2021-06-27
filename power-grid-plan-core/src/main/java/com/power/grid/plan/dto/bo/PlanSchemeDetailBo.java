package com.power.grid.plan.dto.bo;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
public class PlanSchemeDetailBo {

    private String caseNo;

    private String roadNo;

    private String casePoints;

    private Double pathLength;

    private Double cost;

    private Integer roadIndex;

    private Integer layWay;

    private Integer cableType;
}