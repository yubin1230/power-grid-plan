package com.power.grid.plan.controller;

import com.power.grid.plan.BaseResponse;
import com.power.grid.plan.JsonUtil;
import com.power.grid.plan.ResponseCodeEnum;
import com.power.grid.plan.dto.bo.CabinetBo;
import com.power.grid.plan.dto.bo.NodeBo;
import com.power.grid.plan.dto.bo.PlanSchemeBo;
import com.power.grid.plan.dto.bo.PlanSchemeDetailBo;
import com.power.grid.plan.dto.enums.CabinetCategoryType;
import com.power.grid.plan.exception.BizException;
import com.power.grid.plan.service.CabinetService;
import com.power.grid.plan.service.PlanSchemeService;
import com.power.grid.plan.service.manager.CabinetChoiceManage;
import com.power.grid.plan.util.MapUtil;
import com.power.grid.plan.vo.CabinetVo;
import com.power.grid.plan.vo.FillRequirementVo;
import com.power.grid.plan.vo.PlanSchemeVo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 环网柜选择
 * @author yuhin
 * @date 2021/5/30 19:40
 */
@RestController
@RequestMapping("/scheme")
public class SchemeController {

    private static final Logger LOG = LogManager.getLogger(SchemeController.class);

    @Resource
    private PlanSchemeService planSchemeService;

    @Resource
    private CabinetService cabinetService;


    @PostMapping("/selectScheme")
    public BaseResponse<List<PlanSchemeVo>> selectScheme(FillRequirementVo fillRequirementVo) {
        try {
            LOG.info("requestNo：{} 获取规划信息：{}", fillRequirementVo.getRequestNo(), fillRequirementVo.getNeedNo());

            List<PlanSchemeBo> planSchemeBoList = planSchemeService.select(fillRequirementVo.getNeedNo());

            String[] name = {"方案一", "方案二", "方案三"};

            List<PlanSchemeVo> planSchemeVoList = new ArrayList<>();

            for (int i = 0; i < planSchemeBoList.size(); i++) {
                PlanSchemeBo planSchemeBo = planSchemeBoList.get(i);
                //查询方案信息
                PlanSchemeVo planSchemeVo = buildPlanSchemeVo(planSchemeBo);
                //查询环网柜
                CabinetCategoryType categoryType = CabinetCategoryType.getCabinetCategoryTypeMap(planSchemeBo.getCabinetType());
                CabinetBo cabinetBo = cabinetService.queryCabinetListByNo(planSchemeBo.getCabinetNo(), categoryType);
                planSchemeVo.setCabinetVo(buildCabinetVo(cabinetBo));
                planSchemeVo.setCaseName(name[i]);
                planSchemeVoList.add(planSchemeVo);
            }
            LOG.info("requestNo：{} 获取规划信息成功，{}", fillRequirementVo.getRequestNo(), JsonUtil.tryToString(planSchemeVoList));

            return BaseResponse.success(fillRequirementVo.getRequestNo(), planSchemeVoList);
        } catch (BizException e) {
            return BaseResponse.error(fillRequirementVo.getRequestNo(), e.getCode(), e.getMsg());
        } catch (Exception e) {
            LOG.error("requestNo：{} 获取规划信息失败", fillRequirementVo.getRequestNo(), e);
            return BaseResponse.error(fillRequirementVo.getRequestNo(), ResponseCodeEnum.DEFAULT_ERROR);
        }
    }

    private PlanSchemeVo buildPlanSchemeVo(PlanSchemeBo planSchemeBo) {
        //坐标转换

        List<PlanSchemeDetailBo> planSchemeDetailBoList = planSchemeBo.getPlanSchemeDetailBoList().stream().sorted(Comparator.comparing(PlanSchemeDetailBo::getRoadIndex)).collect(Collectors.toList());

        StringBuilder casePoints = new StringBuilder();

        for (int i = 0; i < planSchemeDetailBoList.size(); i++) {
            PlanSchemeDetailBo planSchemeDetailBo = planSchemeDetailBoList.get(i);
            String points = planSchemeDetailBo.getCasePoints();
            String xys = points.split(";")[0];
            NodeBo nodeBo = MapUtil.parseBDCoordinate(Double.parseDouble(xys.split(",")[0]), Double.parseDouble(xys.split(",")[1]));
            if (Objects.nonNull(nodeBo)) {
                casePoints.append(nodeBo.getLongitude()).append(",").append(nodeBo.getLatitude()).append(";");
            }
            if (i == planSchemeDetailBoList.size() - 1) {
                String end = points.split(";")[1];
                NodeBo endNodeBo = MapUtil.parseBDCoordinate(Double.parseDouble(end.split(",")[0]), Double.parseDouble(end.split(",")[1]));
                if (Objects.nonNull(endNodeBo)) {
                    casePoints.append(endNodeBo.getLongitude()).append(",").append(endNodeBo.getLatitude()).append(";");
                }
            }
        }
        PlanSchemeVo planSchemeVo = new PlanSchemeVo();
        BeanUtils.copyProperties(planSchemeBo, planSchemeVo);
        planSchemeVo.setCasePoints(casePoints.substring(0, casePoints.length() - 1));
        return planSchemeVo;
    }

    private CabinetVo buildCabinetVo(CabinetBo cabinetBo) {
        //坐标转换
        NodeBo nodeBo = MapUtil.parseBDCoordinate(cabinetBo.getLongitude(), cabinetBo.getLatitude());
        if (Objects.nonNull(nodeBo)) {
            cabinetBo.setLongitude(nodeBo.getLongitude());
            cabinetBo.setLatitude(nodeBo.getLatitude());
        }
        CabinetVo vo = CabinetVo.builder().build();
        BeanUtils.copyProperties(cabinetBo, vo);
        return vo;
    }

}
