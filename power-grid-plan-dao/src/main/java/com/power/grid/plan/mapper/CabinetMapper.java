package com.power.grid.plan.mapper;

import com.power.grid.plan.pojo.CabinetPo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CabinetMapper {

    int insert(CabinetPo record);

    List<CabinetPo> selectListByCondition(CabinetPo po);

    CabinetPo selectOne(CabinetPo record);

    List<CabinetPo> selectListByLineNoList(@Param("lineNoList") List<String> lineNoList);
}