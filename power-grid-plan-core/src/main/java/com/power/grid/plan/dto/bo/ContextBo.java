package com.power.grid.plan.dto.bo;

import lombok.Data;

import java.util.List;

/**
 * 业务上下文
 * @author yubin
 * @date 2021/6/6 12:34
 */
@Data
public class ContextBo {

    private GridL2Bo gridL2Bo;

    private FillRequirementBo fillRequirementBo;

    private RequirementParamBo requirementParamBo;

    private List<LineBo> lineBoList;

    private List<CabinetBo> cabinetBoList;
}
