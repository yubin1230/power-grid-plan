package com.power.grid.plan.mapper;

import com.power.grid.plan.pojo.FillRequirementPo;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * @author yubin
 * @version 1.0
 * @date 2021-05-16
 */
public interface FillRequirementMapper {

    int insert(FillRequirementPo record);

    FillRequirementPo selectOne(@Param("needNo") String needNo);

    List<FillRequirementPo> selectList(FillRequirementPo po);

    int updateOne(FillRequirementPo record);
}