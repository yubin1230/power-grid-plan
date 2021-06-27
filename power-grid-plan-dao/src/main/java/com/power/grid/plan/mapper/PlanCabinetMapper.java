package com.power.grid.plan.mapper;

import com.power.grid.plan.pojo.PlanCabinetPo;

public interface PlanCabinetMapper {

    int delete(String caseNo);

    int insert(PlanCabinetPo record);

    PlanCabinetPo select(String cabinet_no);
}