package com.power.grid.plan.service;

import com.power.grid.plan.dto.bo.FillRequirementBo;

import java.util.List;

/**
 * 填报需求业务类
 *
 * @author yubin
 * @version 1.0
 * @date 2021-05-16
 */
public interface FillRequirementService {

    public void insert(FillRequirementBo bo);

    public FillRequirementBo selectOne(String needNo);

    public List<FillRequirementBo> selectList(FillRequirementBo bo);

    public void updateOne(FillRequirementBo bo);
}
