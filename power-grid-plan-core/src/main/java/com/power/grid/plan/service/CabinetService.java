package com.power.grid.plan.service;

import com.power.grid.plan.dto.bo.CabinetBo;
import com.power.grid.plan.dto.bo.ContextBo;
import com.power.grid.plan.dto.bo.LineBo;
import com.power.grid.plan.dto.bo.RequirementParamBo;

import java.util.List;

/**
 * 查询环网柜
 * @author yubin
 * @date 2021/6/6 11:47
 */
public interface CabinetService {


    /**
    * 查询符合条件的环网柜
    * @param lineBoList 线路集合
    * @param contextBo 业务上下文
    * @return java.util.List<com.power.grid.plan.dto.bo.CabinetBo>
    */
    List<CabinetBo> queryCabinet(List<LineBo> lineBoList, ContextBo contextBo);

    /**
     * 查询L2网格下的环网柜
     * @param gridNo 网格编码
     * @return java.util.List<com.power.grid.plan.dto.bo.LineBo>
     */
    List<CabinetBo> queryCabinetListByGridNo(String gridNo);

    /**
     * 查询线路下的环网柜
     * @param lineNo 线路编码
     * @return java.util.List<com.power.grid.plan.dto.bo.LineBo>
     */
    List<CabinetBo> queryCabinetListByLineNo(String lineNo);
}
