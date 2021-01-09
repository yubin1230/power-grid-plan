package com.power.grid.plan.service;


import com.power.grid.plan.dto.bo.NodeBo;
import com.power.grid.plan.dto.bo.RoadBo;

import java.util.List;

/**
 * 路段服务类
 * @author yubin
 * @date 2020/11/30 23:27
 */
public interface InitService {

    /**
     * 初始化道路
     * @return java.util.List<com.neo.domain.bo.RoadBo>
     */
    List<RoadBo> initRoadInfo();

    /**
     * 初始化节点信息
     * @return java.util.List<com.power.grid.plan.dto.bo.NodeBo>
     */
    List<NodeBo> initNodeInfo();

}
