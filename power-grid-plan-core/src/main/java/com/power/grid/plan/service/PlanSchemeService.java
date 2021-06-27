package com.power.grid.plan.service;

import com.power.grid.plan.dto.bo.PlanSchemeBo;

import java.util.List;

/**
 * 方案规划业务类
 * @author yubin
 * @date 2021/6/26 11:43
 */
public interface PlanSchemeService {

    int delete(String caseNo);

    int insert(PlanSchemeBo planSchemeBo);

    List<PlanSchemeBo> select(String needNo);
}
