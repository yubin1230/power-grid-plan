package com.power.grid.plan.service;

import com.power.grid.plan.dto.bo.RequirementParamBo;

/**
 * 初始化参数
 * @author yubin
 * @date 2021/5/31 2:19
 */
public interface InitParamService {

    void initRequirementParam(RequirementParamBo bo);

    RequirementParamBo selectOne(String needNo);
}
