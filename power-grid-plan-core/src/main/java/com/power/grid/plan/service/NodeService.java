package com.power.grid.plan.service;

import com.power.grid.plan.dto.bo.NodeBo;

import java.util.List;

/**
 * 节点业务类
 * @author yubin
 * @date 2021/6/26 23:38
 */
public interface NodeService {

     List<NodeBo> select(String gridNo);
}
