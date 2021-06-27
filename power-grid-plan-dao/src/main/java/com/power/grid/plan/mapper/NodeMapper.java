package com.power.grid.plan.mapper;

import com.power.grid.plan.pojo.NodePo;

import java.util.List;

public interface NodeMapper {


    List<NodePo> select(String gridNo);

}