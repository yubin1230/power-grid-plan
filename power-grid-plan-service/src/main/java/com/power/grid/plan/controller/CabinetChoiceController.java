package com.power.grid.plan.controller;

import com.power.grid.plan.BaseResponse;
import com.power.grid.plan.JsonUtil;
import com.power.grid.plan.ResponseCodeEnum;
import com.power.grid.plan.dto.bo.*;
import com.power.grid.plan.service.manager.CabinetChoiceManage;
import com.power.grid.plan.util.MapUtil;
import com.power.grid.plan.vo.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 环网柜选择
 * @author yuhin
 * @date 2021/5/30 19:40
 */
@RestController
@RequestMapping("/selectCabinet")
public class CabinetChoiceController {

    private static final Logger LOG = LogManager.getLogger(CabinetChoiceController.class);

    @Resource
    private CabinetChoiceManage cabinetChoiceManage;


    @PostMapping("/selectExistCabinet")
    public BaseResponse<List<CabinetVo>> selectExistCabinet(FillRequirementVo fillRequirementVo) {
        try {
            LOG.info("requestNo：{} 获取已有环网柜信息：{}", fillRequirementVo.getRequestNo(), fillRequirementVo.getNeedNo());

            List<CabinetBo> cabinetBoList = cabinetChoiceManage.selectExistCabinet(fillRequirementVo.getNeedNo());

            LOG.info("requestNo：{} 获取已有环网柜信息成功，{}", fillRequirementVo.getRequestNo(), JsonUtil.tryToString(cabinetBoList));

            return BaseResponse.success(fillRequirementVo.getRequestNo(), buildCabinetVoList(cabinetBoList));
        } catch (Exception e) {
            LOG.error("requestNo：{} 获取已有环网柜信息失败", fillRequirementVo.getRequestNo(), e);
            return BaseResponse.error(fillRequirementVo.getRequestNo(), ResponseCodeEnum.DEFAULT_ERROR);
        }
    }

    @PostMapping("/selectPlanCabinetExistLine")
    public BaseResponse<List<CabinetVo>> selectPlanCabinetExistLine(FillRequirementVo fillRequirementVo) {
        try {
            LOG.info("requestNo：{} 已有线路获取规划环网柜信息：{}", fillRequirementVo.getRequestNo(), fillRequirementVo.getNeedNo());

            List<CabinetBo> cabinetBoList = cabinetChoiceManage.selectPlanCabinetExistLine(fillRequirementVo.getNeedNo());

            LOG.info("requestNo：{} 已有线路获取规划环网柜信息成功，{}", fillRequirementVo.getRequestNo(), JsonUtil.tryToString(fillRequirementVo));

            return BaseResponse.success(fillRequirementVo.getRequestNo(), buildCabinetVoList(cabinetBoList));
        } catch (Exception e) {
            LOG.error("requestNo：{} 已有线路获取规划环网柜信息失败", fillRequirementVo.getRequestNo(), e);
            return BaseResponse.error(fillRequirementVo.getRequestNo(), ResponseCodeEnum.DEFAULT_ERROR);
        }
    }

    @PostMapping("/selectPlanCabinetPlanLine")
    public BaseResponse<List<CabinetVo>> selectPlanCabinetPlanLine(FillRequirementVo fillRequirementVo) {
        try {
            LOG.info("requestNo：{} 规划线路获取规划环网柜信息：{}", fillRequirementVo.getRequestNo(), fillRequirementVo.getNeedNo());

            List<CabinetBo> cabinetBoList = cabinetChoiceManage.selectPlanCabinetPlanLine(fillRequirementVo.getNeedNo());


            LOG.info("requestNo：{} 规划线路获取规划环网柜信息成功，{}", fillRequirementVo.getRequestNo(), JsonUtil.tryToString(fillRequirementVo));

            return BaseResponse.success(fillRequirementVo.getRequestNo(), buildCabinetVoList(cabinetBoList));
        } catch (Exception e) {
            LOG.error("requestNo：{} 规划线路获取规划环网柜信息失败", fillRequirementVo.getRequestNo(), e);
            return BaseResponse.error(fillRequirementVo.getRequestNo(), ResponseCodeEnum.DEFAULT_ERROR);
        }
    }

    private List<CabinetVo> buildCabinetVoList(List<CabinetBo> cabinetBoList) {
        //坐标转换
        cabinetBoList = cabinetBoList.stream().peek(cabinetBo -> {
            NodeBo nodeBo = MapUtil.parseBDCoordinate(cabinetBo.getLongitude(), cabinetBo.getLatitude());
            if (Objects.nonNull(nodeBo)) {
                cabinetBo.setLongitude(nodeBo.getLongitude());
                cabinetBo.setLatitude(nodeBo.getLatitude());
            }
        }).collect(Collectors.toList());

        return cabinetBoList.stream().map(bo -> {
            CabinetVo vo = CabinetVo.builder().build();
            BeanUtils.copyProperties(bo, vo);
            vo.setCabinetCategory(bo.getCabinetCategory().getCode());
            return vo;
        }).collect(Collectors.toList());
    }

}
