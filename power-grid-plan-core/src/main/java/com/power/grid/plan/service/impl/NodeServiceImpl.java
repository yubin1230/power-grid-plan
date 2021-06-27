package com.power.grid.plan.service.impl;

import com.power.grid.plan.dto.bo.NodeBo;
import com.power.grid.plan.mapper.NodeMapper;
import com.power.grid.plan.pojo.NodePo;
import com.power.grid.plan.service.NodeService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 节点业务类
 * @author yubin
 * @date 2021/6/26 23:40
 */
@Service
public class NodeServiceImpl implements NodeService {

    @Resource
    private NodeMapper nodeMapper;

    @Override
    public List<NodeBo> select(String gridNo) {
        List<NodePo> nodePoList = nodeMapper.select(gridNo);
        return nodePoList.stream().map(nodePo -> {
            NodeBo nodeBo = NodeBo.builder().build();
            BeanUtils.copyProperties(nodePo, nodeBo);
            return nodeBo;
        }).collect(Collectors.toList());
    }
}
