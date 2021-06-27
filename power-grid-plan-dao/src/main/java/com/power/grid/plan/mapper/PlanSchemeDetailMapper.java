package com.power.grid.plan.mapper;

import com.power.grid.plan.pojo.PlanSchemeDetailPo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PlanSchemeDetailMapper {

    int delete(String  caseNo);

    int insert(@Param("records") List<PlanSchemeDetailPo> records);

    List<PlanSchemeDetailPo> select(String  caseNo);
}