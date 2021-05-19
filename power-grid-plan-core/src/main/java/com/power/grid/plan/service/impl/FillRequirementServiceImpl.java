package com.power.grid.plan.service.impl;

import com.google.common.collect.Lists;
import com.power.grid.plan.dto.bo.FillRequirementBo;
import com.power.grid.plan.mapper.FillRequirementMapper;
import com.power.grid.plan.pojo.FillRequirementPo;
import com.power.grid.plan.service.FillRequirementService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 填报需求实现
 *
 * @author yubin
 * @date 2021/05/17 0:10
 */
@Service
public class FillRequirementServiceImpl implements FillRequirementService {

    @Resource
    private FillRequirementMapper mapper;

    @Override
    public void insert(FillRequirementBo bo) {
        FillRequirementPo po = new FillRequirementPo();
        BeanUtils.copyProperties(bo, po);
        po.setCreateTime(new Date());
        po.setUpdateTime(new Date());
        mapper.insert(po);
    }

    @Override
    public FillRequirementBo selectOne(String needNo) {
        FillRequirementPo po = mapper.selectOne(needNo);
        if (Objects.nonNull(po)) {
            FillRequirementBo bo = new FillRequirementBo();
            BeanUtils.copyProperties(po, bo);
            return bo;
        }
        return null;
    }

    @Override
    public List<FillRequirementBo> selectList(FillRequirementBo bo) {
        FillRequirementPo po = new FillRequirementPo();
        BeanUtils.copyProperties(bo, po);
        List<FillRequirementPo> poList = mapper.selectList(po);
        if (CollectionUtils.isNotEmpty(poList)) {
            return poList.stream().map(x -> {
                FillRequirementBo boTemp = new FillRequirementBo();
                BeanUtils.copyProperties(x, boTemp);
                return boTemp;
            }).collect(Collectors.toList());
        }
        return Lists.newArrayList();
    }

    @Override
    public void updateOne(FillRequirementBo bo) {
        FillRequirementPo po = new FillRequirementPo();
        BeanUtils.copyProperties(bo, po);
        mapper.updateOne(po);
    }
}
