package com.power.grid.plan.service.impl;

import com.power.grid.plan.dto.bo.RequirementParamBo;
import com.power.grid.plan.mapper.RequirementParamMapper;
import com.power.grid.plan.pojo.RequirementParamPo;
import com.power.grid.plan.service.InitParamService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 初始化参数
 * @author yubin
 * @date 2021/5/31 2:21
 */
@Service
public class InitParamServiceImpl implements InitParamService {

    @Resource
    private RequirementParamMapper requirementParamMapper;

    @Override
    public void initRequirementParam(RequirementParamBo bo) {
        RequirementParamPo po = new RequirementParamPo();
        BeanUtils.copyProperties(bo, po);
        po.setCreateTime(new Date());
        requirementParamMapper.insert(po);
    }

    @Override
    public RequirementParamBo selectOne(String needNo) {
        RequirementParamBo bo=new RequirementParamBo();
        RequirementParamPo po=requirementParamMapper.selectOne(needNo);
        BeanUtils.copyProperties(po,bo);
        return bo;
    }
}
