package com.power.grid.plan.service.impl;

import com.power.grid.plan.ResponseCodeEnum;
import com.power.grid.plan.dto.bo.*;
import com.power.grid.plan.dto.enums.AreaType;
import com.power.grid.plan.dto.enums.LineType;
import com.power.grid.plan.exception.BizException;
import com.power.grid.plan.mapper.LineMapper;
import com.power.grid.plan.pojo.LinePo;
import com.power.grid.plan.service.CabinetService;
import com.power.grid.plan.service.LineService;
import com.power.grid.plan.util.LinePointUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 线路实现类
 * @author yubin
 * @date 2021/6/6 12:17
 */
@Service
public class LineServiceImpl implements LineService {

    /**
     * 线路存在过载
     */
    private static final int OVERLOAD = 1;

    /**
     * 三遥
     */
    private static final int THREE_REMOTE = 1;

    /**
    * 环网柜数量
    */
    private static final int CABINET_NUM = 4;


    @Resource
    private LineMapper lineMapper;

    @Resource
    private CabinetService cabinetService;

    @Override
    public List<LineBo> queryLine(String gridNo, ContextBo contextBo) {

        List<LineBo> queryLineList = queryLineList(gridNo);

        return filterRule(queryLineList, contextBo);
    }

    @Override
    public List<LineBo> queryLineList(String gridNo) {
        List<LinePo> linePoList = lineMapper.selectListByCondition(LinePo.builder().gridNo(gridNo).build());
        return linePoList.stream().map(l -> {
            LineBo bo = LineBo.builder().build();
            BeanUtils.copyProperties(l, bo);
            return bo;
        }).collect(Collectors.toList());
    }

    @Override
    public List<CabinetBo> newCabinetExistLine(String gridNo, ContextBo contextBo) {

        List<LineBo> queryLineList = queryLineList(gridNo);

        //过滤线路规则
        List<LineBo> filterList=queryLineList.stream().filter(lineBo ->lineBo.getLineType().equals(LineType.PLAN.getCode())).collect(Collectors.toList());
        filterList = filterCabinetExistLine(filterList, contextBo);

        return getNewCabinetList(filterList,contextBo);
    }

    @Override
    public List<CabinetBo> newCabinetPlanLine(String gridNo, ContextBo contextBo) {

        List<LineBo> queryLineList = queryLineList(gridNo);

        List<LineBo> filterList=queryLineList.stream().filter(lineBo ->lineBo.getLineType().equals(LineType.PLAN.getCode())).collect(Collectors.toList());

        return getNewCabinetList(filterList,contextBo);
    }


    private List<LineBo> filterRule(List<LineBo> queryLineList, ContextBo contextBo) {
        return queryLineList.stream().filter(lineBo -> {
            //线路已有
            if (!lineBo.getLineType().equals(LineType.ALREADY_EXISTING.getCode())) {
                return false;
            }
            FillRequirementBo fillRequirementBo = contextBo.getFillRequirementBo();
            RequirementParamBo requirementParamBo = contextBo.getRequirementParamBo();
            //线路容量+负荷需求≤线路最大总容量*调整系数

//            if(lineBo.getLinePowerUsed()+fillRequirementBo.getNeedCapacity()>lineBo.getLinePower()*requirementParamBo)

            //线路自动化要求
            double requestRatio = queryAutomationRequirement(contextBo);
            if (queryAutomationRatio(lineBo.getLineNo()) < requestRatio) {
                return false;
            }

            //线路所属变电站高峰负荷期间不存在重过载
            if (lineBo.getOverload().equals(OVERLOAD)) {
                return false;
            }

            return true;
        }).collect(Collectors.toList());
    }

    private double queryAutomationRequirement(ContextBo contextBo) {
        FillRequirementBo fillRequirementBo = contextBo.getFillRequirementBo();
        RequirementParamBo requirementParamBo = contextBo.getRequirementParamBo();
        AreaType areaType = AreaType.getAreaTypeMap(fillRequirementBo.getArea());
        switch (areaType) {
            case A1:
                return requirementParamBo.getA1AutomationRatio();

            case A2:
                return requirementParamBo.getA2AutomationRatio();

            case B:
                return requirementParamBo.getBAutomationRatio();
        }
        throw new BizException(ResponseCodeEnum.DEFAULT_ERROR, "非法区域类型");
    }

    private double queryAutomationRatio(String lineNo) {
        List<CabinetBo> cabinetBoList = cabinetService.queryCabinetListByLineNo(lineNo);
        if (CollectionUtils.isEmpty(cabinetBoList)) {
            return 0;
        }
        List<CabinetBo> threeRemote = cabinetBoList.stream().filter(cabinetBo -> cabinetBo.getAutomation().equals(THREE_REMOTE)).collect(Collectors.toList());
        return threeRemote.size() / cabinetBoList.size();
    }


    private List<LineBo> filterCabinetExistLine(List<LineBo> queryLineList, ContextBo contextBo) {
        FillRequirementBo fillRequirementBo = contextBo.getFillRequirementBo();
        RequirementParamBo requirementParamBo = contextBo.getRequirementParamBo();
        AreaType areaType = AreaType.getAreaTypeMap(fillRequirementBo.getArea());
        final int nodeNum;
        switch (areaType) {
            case A1:
                nodeNum = requirementParamBo.getA1NodeNum();
                break;
            case A2:
                nodeNum = requirementParamBo.getA2NodeNum();
                break;
            case B:
                nodeNum = requirementParamBo.getBNodeNum();
                break;
            default:
                throw new BizException(ResponseCodeEnum.DEFAULT_ERROR, "非法区域类型");
        }
        return queryLineList.stream().filter(lineBo -> lineBo.getLinePoints().split(";").length < nodeNum).collect(Collectors.toList());
    }

    private List<CabinetBo> getNewCabinetList(List<LineBo> queryLineList, ContextBo contextBo){
        FillRequirementBo fillRequirementBo = contextBo.getFillRequirementBo();
        List<PointBo> pointBoList=LinePointUtil.getPedalSimplexAndDistance(queryLineList,new NodeBo(fillRequirementBo.getLongitude(), fillRequirementBo.getLatitude()));
        return pointBoList.stream().map(pointBo -> CabinetBo.builder().longitude(pointBo.getLongitude()).latitude(pointBo.getLatitude()).build()).collect(Collectors.toList()).subList(0,CABINET_NUM);
    }


}
