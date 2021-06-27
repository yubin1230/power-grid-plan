package com.power.grid.plan.mapper;

import com.power.grid.plan.pojo.RoadPo;

import java.util.List;

public interface RoadMapper {


    List<RoadPo> select(String gridNo);

}