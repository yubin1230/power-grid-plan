package com.power.grid.plan.service.impl;

import com.power.grid.plan.dto.bo.RoadBo;
import com.power.grid.plan.mapper.RoadMapper;
import com.power.grid.plan.pojo.RoadPo;
import com.power.grid.plan.service.RoadService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 路段查询
 * @author yubin
 * @date 2021/6/26 23:39
 */
@Service
public class RoadServiceImpl implements RoadService {

    @Resource
    private RoadMapper roadMapper;

    @Override
    public List<RoadBo> select(String gridNo) {
        List<RoadPo> roadPoList = roadMapper.select(gridNo);
        return roadPoList.stream().map(nodePo -> {
            RoadBo nodeBo = RoadBo.builder().build();
            BeanUtils.copyProperties(nodePo, nodeBo);
            return nodeBo;
        }).collect(Collectors.toList());
    }
}
