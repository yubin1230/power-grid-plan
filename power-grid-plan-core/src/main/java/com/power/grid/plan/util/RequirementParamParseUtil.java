package com.power.grid.plan.util;

import com.power.grid.plan.ResponseCodeEnum;
import com.power.grid.plan.dto.bo.FillRequirementBo;
import com.power.grid.plan.dto.bo.ParamBo;
import com.power.grid.plan.dto.bo.RequirementParamBo;
import com.power.grid.plan.dto.enums.AreaType;
import com.power.grid.plan.dto.enums.PowerType;
import com.power.grid.plan.exception.BizException;


/**
 * 初始化参数转换
 * @author yubin
 * @date 2021/6/20 23:57
 */
public class RequirementParamParseUtil {

    public static ParamBo parse(RequirementParamBo requirementParamBo, FillRequirementBo fillRequirementBo) {
        AreaType areaType = AreaType.getAreaTypeMap(fillRequirementBo.getArea());
        PowerType powerType = PowerType.getPowerTypeMap(fillRequirementBo.getType());
        int conversionRatio;
        int maximumCapacity;
        int automationRatio;
        double load;
        double loadRatio;
        int capacity;
        double capacityRatio;
        int users;
        int nodeNum;
        switch (areaType) {
            case A1:
                switch (powerType) {
                    case INDUSTRY:
                        conversionRatio = requirementParamBo.getIndustryRatio();
                        maximumCapacity = requirementParamBo.getIndustryMaximumCapacity();
                        automationRatio = requirementParamBo.getA1AutomationRatio();
                        load = requirementParamBo.getA1IndustryLoad();
                        loadRatio = requirementParamBo.getA1IndustryLoadRatio();
                        capacity = requirementParamBo.getA1IndustryCapacity();
                        capacityRatio = requirementParamBo.getA1IndustryCapacityRatio();
                        users = requirementParamBo.getA1IndustryUsers();
                        nodeNum = requirementParamBo.getA1NodeNum();
                        break;
                    case BUSINESS:
                        conversionRatio = requirementParamBo.getBusinessRatio();
                        maximumCapacity = requirementParamBo.getBusinessMaximumCapacity();
                        automationRatio = requirementParamBo.getA1AutomationRatio();
                        load = requirementParamBo.getA1BusinessLoad();
                        loadRatio = requirementParamBo.getA1BusinessLoadRatio();
                        capacity = requirementParamBo.getA1BusinessCapacity();
                        capacityRatio = requirementParamBo.getA1BusinessCapacityRatio();
                        users = requirementParamBo.getA1BusinessUsers();
                        nodeNum = requirementParamBo.getA1NodeNum();
                        break;
                    case RESIDENT:
                        conversionRatio = requirementParamBo.getResidentRatio();
                        maximumCapacity = requirementParamBo.getResidentMaximumCapacity();
                        automationRatio = requirementParamBo.getA1AutomationRatio();
                        load = requirementParamBo.getA1ResidentLoad();
                        loadRatio = requirementParamBo.getA1ResidentLoadRatio();
                        capacity = requirementParamBo.getA1ResidentCapacity();
                        capacityRatio = requirementParamBo.getA1ResidentCapacityRatio();
                        users = requirementParamBo.getA1ResidentUsers();
                        nodeNum = requirementParamBo.getA1NodeNum();
                        break;
                    default:
                        throw new BizException(ResponseCodeEnum.DEFAULT_ERROR, "非法负荷性质");

                }
                break;
            case A2:
                switch (powerType) {
                    case INDUSTRY:
                        conversionRatio = requirementParamBo.getIndustryRatio();
                        maximumCapacity = requirementParamBo.getIndustryMaximumCapacity();
                        automationRatio = requirementParamBo.getA2AutomationRatio();
                        load = requirementParamBo.getA2bIndustryLoad();
                        loadRatio = requirementParamBo.getA2bIndustryLoadRatio();
                        capacity = requirementParamBo.getA2bIndustryCapacity();
                        capacityRatio = requirementParamBo.getA2bIndustryCapacityRatio();
                        users = requirementParamBo.getA2bIndustryUsers();
                        nodeNum = requirementParamBo.getA2NodeNum();
                        break;
                    case BUSINESS:
                        conversionRatio = requirementParamBo.getBusinessRatio();
                        maximumCapacity = requirementParamBo.getBusinessMaximumCapacity();
                        automationRatio = requirementParamBo.getA2AutomationRatio();
                        load = requirementParamBo.getA2bBusinessLoad();
                        loadRatio = requirementParamBo.getA2bBusinessLoadRatio();
                        capacity = requirementParamBo.getA2bBusinessCapacity();
                        capacityRatio = requirementParamBo.getA2bBusinessCapacityRatio();
                        users = requirementParamBo.getA2bBusinessUsers();
                        nodeNum = requirementParamBo.getA2NodeNum();
                        break;
                    case RESIDENT:
                        conversionRatio = requirementParamBo.getResidentRatio();
                        maximumCapacity = requirementParamBo.getResidentMaximumCapacity();
                        automationRatio = requirementParamBo.getA2AutomationRatio();
                        load = requirementParamBo.getA2bResidentLoad();
                        loadRatio = requirementParamBo.getA2bResidentLoadRatio();
                        capacity = requirementParamBo.getA2bResidentCapacity();
                        capacityRatio = requirementParamBo.getA2bResidentCapacityRatio();
                        users = requirementParamBo.getA2bResidentUsers();
                        nodeNum = requirementParamBo.getA2NodeNum();
                        break;
                    default:
                        throw new BizException(ResponseCodeEnum.DEFAULT_ERROR, "非法负荷性质");
                }
                break;
            case B:
                switch (powerType) {
                    case INDUSTRY:
                        conversionRatio = requirementParamBo.getIndustryRatio();
                        maximumCapacity = requirementParamBo.getIndustryMaximumCapacity();
                        automationRatio = requirementParamBo.getBAutomationRatio();
                        load = requirementParamBo.getA2bIndustryLoad();
                        loadRatio = requirementParamBo.getA2bIndustryLoadRatio();
                        capacity = requirementParamBo.getA2bIndustryCapacity();
                        capacityRatio = requirementParamBo.getA2bIndustryCapacityRatio();
                        users = requirementParamBo.getA2bIndustryUsers();
                        nodeNum = requirementParamBo.getBNodeNum();
                        break;
                    case BUSINESS:
                        conversionRatio = requirementParamBo.getBusinessRatio();
                        maximumCapacity = requirementParamBo.getBusinessMaximumCapacity();
                        automationRatio = requirementParamBo.getBAutomationRatio();
                        load = requirementParamBo.getA2bBusinessLoad();
                        loadRatio = requirementParamBo.getA2bBusinessLoadRatio();
                        capacity = requirementParamBo.getA2bBusinessCapacity();
                        capacityRatio = requirementParamBo.getA2bBusinessCapacityRatio();
                        users = requirementParamBo.getA2bBusinessUsers();
                        nodeNum = requirementParamBo.getBNodeNum();
                        break;
                    case RESIDENT:
                        conversionRatio = requirementParamBo.getResidentRatio();
                        maximumCapacity = requirementParamBo.getResidentMaximumCapacity();
                        automationRatio = requirementParamBo.getBAutomationRatio();
                        load = requirementParamBo.getA2bResidentLoad();
                        loadRatio = requirementParamBo.getA2bResidentLoadRatio();
                        capacity = requirementParamBo.getA2bResidentCapacity();
                        capacityRatio = requirementParamBo.getA2bResidentCapacityRatio();
                        users = requirementParamBo.getA2bResidentUsers();
                        nodeNum = requirementParamBo.getBNodeNum();
                        break;
                    default:
                        throw new BizException(ResponseCodeEnum.DEFAULT_ERROR, "非法负荷性质");
                }
                break;
            default:
                throw new BizException(ResponseCodeEnum.DEFAULT_ERROR, "非法区域类型");
        }
        return ParamBo.builder().conversionRatio(conversionRatio).maximumCapacity(maximumCapacity).automationRatio(automationRatio).lineThreshold(requirementParamBo.getLineThreshold()).lineThresholdRatio(requirementParamBo.getLineThresholdRatio())
                .load(load).loadRatio(loadRatio).capacity(capacity).capacityRatio(capacityRatio).users(users).nodeNum(nodeNum).build();
    }

}
