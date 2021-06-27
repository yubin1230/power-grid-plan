package com.power.grid.plan.service.manager;

import com.google.common.collect.Lists;
import com.power.grid.plan.BaseResponse;
import com.power.grid.plan.dto.bo.*;
import com.power.grid.plan.service.*;
import com.power.grid.plan.util.RequirementParamParseUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 上下文加载类
 * @author yubin
 * @date 2021/6/21 23:39
 */
@Component
public class CabinetChoiceManage {

    @Resource
    private FillRequirementService fillRequirementService;

    @Resource
    private InitParamService initParamService;

    @Resource
    private GridL2Service gridL2Service;

    @Resource
    private LineService lineService;

    @Resource
    private CabinetService cabinetService;


    /**
     * 查询已有环网柜
     * @param needNo 需求编码
     * @return java.util.List<com.power.grid.plan.dto.bo.CabinetBo>
     */
    public List<CabinetBo> selectExistCabinet(String needNo){
        //获取上下文
        CabinetContextBo cabinetContextBo =context(needNo);

        //获取线路信息
        List<LineBo> lineBoList = lineService.queryLine(cabinetContextBo);

        if (CollectionUtils.isEmpty(lineBoList)) {
            return Lists.newArrayList();
        }

        return cabinetService.queryCabinet(lineBoList, cabinetContextBo);
    }

    /**
    * 查询已有线路规划环网柜
    * @param needNo 需求编码
    * @return java.util.List<com.power.grid.plan.dto.bo.CabinetBo>
    */
    public List<CabinetBo> selectPlanCabinetExistLine(String needNo){
        //获取上下文
        CabinetContextBo cabinetContextBo =context(needNo);

        return lineService.newCabinetExistLine(cabinetContextBo);
    }

    /**
     * 查询规划线路规划环网柜
     * @param needNo 需求编码
     * @return java.util.List<com.power.grid.plan.dto.bo.CabinetBo>
     */
    public List<CabinetBo> selectPlanCabinetPlanLine(String needNo){
        //获取上下文
        CabinetContextBo cabinetContextBo =context(needNo);

        return lineService.newCabinetPlanLine(cabinetContextBo);
    }





    public CabinetContextBo context(String needNo) {

        //用户填报需求
        FillRequirementBo fillRequirementBo = fillRequirementService.selectOne(needNo);

        //L2网格信息
        GridL2Bo gridL2Bo = gridL2Service.queryGridL2(fillRequirementBo);

        //初始化参数信息
        RequirementParamBo requirementParamBo=initParamService.selectOne(needNo);

        //初始化参数转换
        ParamBo paramBo=RequirementParamParseUtil.parse(requirementParamBo,fillRequirementBo);

        return CabinetContextBo.builder().gridL2Bo(gridL2Bo).fillRequirementBo(fillRequirementBo).paramBo(paramBo).build();
    }
}
