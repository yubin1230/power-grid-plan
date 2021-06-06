package com.power.grid.plan.service;

import com.power.grid.plan.dto.bo.CabinetBo;
import com.power.grid.plan.dto.bo.FillRequirementBo;
import com.power.grid.plan.dto.bo.GridL2Bo;
import com.power.grid.plan.dto.bo.LineBo;

import java.util.List;


/**
 * 显示地图业务类
 * @author yubin
 * @date 2021/5/30 11:58
 */
public interface GridL2Service {


    /**
    * 根据需求变化查询L2网格信息
    * @param bo 用户填报需求
    * @return com.power.grid.plan.dto.bo.GridL2Bo
    */
    GridL2Bo queryGridL2(FillRequirementBo bo);

}
