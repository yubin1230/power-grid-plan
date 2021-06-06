package com.power.grid.plan.controller;

import com.power.grid.plan.BaseResponse;
import com.power.grid.plan.JsonUtil;
import com.power.grid.plan.ResponseCodeEnum;
import com.power.grid.plan.dto.bo.CabinetBo;
import com.power.grid.plan.dto.bo.FillRequirementBo;
import com.power.grid.plan.dto.bo.GridL2Bo;
import com.power.grid.plan.dto.bo.LineBo;
import com.power.grid.plan.service.CabinetService;
import com.power.grid.plan.service.FillRequirementService;
import com.power.grid.plan.service.GridL2Service;
import com.power.grid.plan.service.LineService;
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
 * GIS信息展示
 * @author yuhin
 * @date 2021/5/30 19:40
 */
@RestController
@RequestMapping("/showGis")
public class ShowGisController {

    private static final Logger LOG = LogManager.getLogger(ShowGisController.class);

    @Resource
    private GridL2Service gridL2Service;

    @Resource
    private LineService lineService;

    @Resource
    private CabinetService cabinetService;

    @Resource
    private FillRequirementService fillRequirementService;

    @PostMapping("/select")
    public BaseResponse<GisShowVo> getGisInfo(FillRequirementVo fillRequirementVo) {
        try {
            LOG.info("requestNo：{} 获取L2网格信息：{}", fillRequirementVo.getRequestNo(), fillRequirementVo.getNeedNo());

            FillRequirementBo fillRequirementBo = fillRequirementService.selectOne(fillRequirementVo.getNeedNo());

            BeanUtils.copyProperties(fillRequirementBo, fillRequirementVo);

            GridL2Bo gridL2Bo = gridL2Service.queryGridL2(fillRequirementBo);

            if (Objects.isNull(gridL2Bo)) {
                return BaseResponse.error(fillRequirementVo.getRequestNo(), ResponseCodeEnum.DEFAULT_ERROR.getCode(), "填报地址所在L2网格为空");
            }

            GridL2Vo gridL2Vo = new GridL2Vo();

            BeanUtils.copyProperties(gridL2Bo, gridL2Vo);

            List<CabinetBo> cabinetBoList = cabinetService.queryCabinetListByGridNo(gridL2Bo.getGridNo());

            List<CabinetVo> cabinetVoList = cabinetBoList.stream().map(c -> {
                CabinetVo vo = CabinetVo.builder().build();
                BeanUtils.copyProperties(c, vo);
                return vo;
            }).collect(Collectors.toList());

            List<LineBo> lineBoList = lineService.queryLineList(gridL2Bo.getGridNo());

            List<LineVo> lineVoList = lineBoList.stream().map(l -> {
                LineVo vo = LineVo.builder().build();
                BeanUtils.copyProperties(l, vo);
                return vo;
            }).collect(Collectors.toList());

            GisShowVo gisShowVo = GisShowVo.builder().fillRequirementVo(fillRequirementVo).gridL2Vo(gridL2Vo).cabinetVoList(cabinetVoList).lineVoList(lineVoList).build();

            LOG.info("requestNo：{} 获取L2网格信息成功，{}", fillRequirementVo.getRequestNo(), JsonUtil.tryToString(gisShowVo));

            return BaseResponse.success(fillRequirementVo.getRequestNo(), gisShowVo);
        } catch (Exception e) {
            LOG.error("requestNo：{} 获取L2网格信息", fillRequirementVo.getRequestNo(), e);
            return BaseResponse.error(fillRequirementVo.getRequestNo(), ResponseCodeEnum.DEFAULT_ERROR);
        }
    }


}
