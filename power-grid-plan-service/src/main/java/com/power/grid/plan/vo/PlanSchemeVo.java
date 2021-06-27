package com.power.grid.plan.vo;

import com.power.grid.plan.BaseRequest;
import com.power.grid.plan.dto.bo.PlanSchemeDetailBo;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class PlanSchemeVo extends BaseRequest {


    private String caseNo;

    private String caseName;

    private CabinetVo cabinetVo;

    private Double caseCost;

    private Double caseLength;

    private String needNo;

    private Integer isChoose;

    private String casePoints;

}