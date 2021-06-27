package com.power.grid.plan.service;

import com.power.grid.plan.dto.bo.CabinetBo;
import com.power.grid.plan.dto.bo.CabinetContextBo;
import com.power.grid.plan.dto.bo.LineBo;
import com.power.grid.plan.dto.bo.PlanCabinetBo;
import com.power.grid.plan.dto.enums.CabinetCategoryType;

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
    * @param cabinetContextBo 业务上下文
    * @return java.util.List<com.power.grid.plan.dto.bo.CabinetBo>
    */
    List<CabinetBo> queryCabinet(List<LineBo> lineBoList, CabinetContextBo cabinetContextBo);

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

    /**
    * 保存规划环网柜
    * @param planCabinetBo 规划环网柜
    * @return int
    */
    int savePlanCabinet(PlanCabinetBo planCabinetBo);

    /**
    * 根据编码查询环网柜
    * @param cabinetNo 环网柜编码
    * @param type 环网柜类型
    * @return com.power.grid.plan.dto.bo.CabinetBo
    */
    CabinetBo queryCabinetListByNo(String cabinetNo, CabinetCategoryType type);
}
