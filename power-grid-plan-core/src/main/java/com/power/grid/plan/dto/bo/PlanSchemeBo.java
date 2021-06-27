package com.power.grid.plan.dto.bo;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PlanSchemeBo {

    private Integer id;

    private String caseNo;

    private String caseName;

    private String cabinetNo;

    private int cabinetType;

    private Double caseCost;

    private Double caseLength;

    private String needNo;

    private Integer isChoose;

    private List<PlanSchemeDetailBo> planSchemeDetailBoList;

}