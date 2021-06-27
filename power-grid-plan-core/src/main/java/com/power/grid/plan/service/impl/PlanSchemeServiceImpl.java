package com.power.grid.plan.service.impl;

import com.power.grid.plan.dto.bo.PlanSchemeBo;
import com.power.grid.plan.dto.bo.PlanSchemeDetailBo;
import com.power.grid.plan.mapper.PlanSchemeDetailMapper;
import com.power.grid.plan.mapper.PlanSchemeMapper;
import com.power.grid.plan.pojo.PlanSchemeDetailPo;
import com.power.grid.plan.pojo.PlanSchemePo;
import com.power.grid.plan.service.PlanSchemeService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 规划方案业务类
 * @author yubin
 * @date 2021/6/26 11:45
 */
@Service
public class PlanSchemeServiceImpl implements PlanSchemeService {

    @Resource
    private PlanSchemeMapper planSchemeMapper;

    @Resource
    private PlanSchemeDetailMapper planSchemeDetailMapper;

    @Override
    @Transactional
    public int delete(String caseNo) {
        int num = planSchemeMapper.delete(caseNo);
        planSchemeDetailMapper.delete(caseNo);
        return num;
    }

    @Override
    @Transactional
    public int insert(PlanSchemeBo planSchemeBo) {
        PlanSchemePo planSchemePo = new PlanSchemePo();
        BeanUtils.copyProperties(planSchemeBo, planSchemePo);
        int num = planSchemeMapper.insert(planSchemePo);
        List<PlanSchemeDetailPo> planSchemeDetailPoList = planSchemeBo.getPlanSchemeDetailBoList().stream().map(planSchemeDetailBo -> {
            PlanSchemeDetailPo planSchemeDetailPo = new PlanSchemeDetailPo();
            BeanUtils.copyProperties(planSchemeDetailBo, planSchemeDetailPo);
            return planSchemeDetailPo;
        }).collect(Collectors.toList());
        planSchemeDetailMapper.insert(planSchemeDetailPoList);
        return num;
    }

    @Override
    public List<PlanSchemeBo> select(String needNo) {
        List<PlanSchemePo> planSchemePoList = planSchemeMapper.select(needNo);
        return planSchemePoList.stream().map(planSchemePo -> {
            PlanSchemeBo bo = PlanSchemeBo.builder().build();
            BeanUtils.copyProperties(planSchemePo, bo);
            List<PlanSchemeDetailPo> planSchemeDetailPoList = planSchemeDetailMapper.select(planSchemePo.getCaseNo());
            List<PlanSchemeDetailBo> planSchemeDetailBoList = planSchemeDetailPoList.stream().map(planSchemeDetailPo -> {
                PlanSchemeDetailBo planSchemeDetailBo = PlanSchemeDetailBo.builder().build();
                BeanUtils.copyProperties(planSchemeDetailPo, planSchemeDetailBo);
                return planSchemeDetailBo;
            }).collect(Collectors.toList());
            bo.setPlanSchemeDetailBoList(planSchemeDetailBoList);
            return bo;
        }).collect(Collectors.toList());
    }
}
