package com.power.grid.plan.mapper;


import com.power.grid.plan.pojo.RequirementParamPo;

public interface RequirementParamMapper {

    int insert(RequirementParamPo po);

    RequirementParamPo selectOne(String needNo);
}