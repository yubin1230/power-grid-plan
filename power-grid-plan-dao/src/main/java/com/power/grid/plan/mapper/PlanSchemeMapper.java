package com.power.grid.plan.mapper;

import com.power.grid.plan.pojo.PlanSchemePo;

import java.util.List;

public interface PlanSchemeMapper {

    int delete(String  caseNo);

    int insert(PlanSchemePo planSchemePo);

    List<PlanSchemePo> select(String  needNo);

}