package com.power.grid.plan.service;

import com.power.grid.plan.dto.bo.CabinetBo;
import com.power.grid.plan.dto.bo.ContextBo;
import com.power.grid.plan.dto.bo.LineBo;
import com.power.grid.plan.dto.enums.LineType;

import java.util.List;

/**
 * 线路业务类
 * @author yubin
 * @date 2021/6/6 11:48
 */
public interface LineService {


    /**
     * 查询符合条件的线路
     * @param gridNo    网格编码
     * @param contextBo 业务上下文
     * @return java.util.List<com.power.grid.plan.dto.bo.LineBo>
     */
    List<LineBo> queryLine(String gridNo, ContextBo contextBo);

    /**
     * 查询L2网格下的线路
     * @param gridNo 网格编码
     * @return java.util.List<com.power.grid.plan.dto.bo.LineBo>
     */
    List<LineBo> queryLineList(String gridNo);

    
    /**
    * 现状接线新建环网柜
    * @param gridNo 网格编码
    * @param contextBo  业务上下文
    * @return java.util.List<com.power.grid.plan.dto.bo.CabinetBo>
    */
    List<CabinetBo> newCabinetExistLine(String gridNo, ContextBo contextBo);

    /**
    * 变电站新出目标接线新建环网柜
    * @param gridNo  网格编码
    * @param contextBo  业务上下文
    * @return java.util.List<com.power.grid.plan.dto.bo.CabinetBo>
    */
    List<CabinetBo> newCabinetPlanLine(String gridNo, ContextBo contextBo);

}
