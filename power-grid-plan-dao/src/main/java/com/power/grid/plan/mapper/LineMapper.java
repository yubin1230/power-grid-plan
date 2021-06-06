package com.power.grid.plan.mapper;

import com.power.grid.plan.pojo.LinePo;

import java.util.List;

public interface LineMapper {

    List<LinePo> selectListByCondition(LinePo po);
}