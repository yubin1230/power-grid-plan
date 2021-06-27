package com.power.grid.plan.controller;


import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.power.grid.plan.BaseResponse;
import com.power.grid.plan.JsonUtil;
import com.power.grid.plan.LogUtil;
import com.power.grid.plan.ResponseCodeEnum;
import com.power.grid.plan.dto.bo.CabinetBo;
import com.power.grid.plan.dto.bo.FillRequirementBo;
import com.power.grid.plan.dto.bo.HandleBo;
import com.power.grid.plan.dto.enums.AreaType;
import com.power.grid.plan.dto.enums.CabinetCategoryType;
import com.power.grid.plan.exception.BizException;
import com.power.grid.plan.service.FillRequirementService;
import com.power.grid.plan.service.manager.AStarCalculateManage;
import com.power.grid.plan.service.manager.CabinetChoiceManage;
import com.power.grid.plan.service.manager.CalculateManage;
import com.power.grid.plan.service.manager.GridPlanManage;
import com.power.grid.plan.vo.CalculateRequest;
import com.power.grid.plan.vo.CalculateVo;
import com.power.grid.plan.vo.HandleVo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class CalculateController {

    private static final Logger LOG = LogManager.getLogger(CalculateController.class);


    @Resource
    private GridPlanManage gridPlanManage;

    @Resource
    private AStarCalculateManage aStarCalculateManage;

    @Resource
    private FillRequirementService fillRequirementService;

    @Resource
    private CabinetChoiceManage cabinetChoiceManage;

    @Resource
    private CalculateManage calculateManage;

    @GetMapping(value = "/calculate")
    @ResponseBody
    public List<HandleVo> calculate(long start, long end) {
        List<HandleVo> HandleVoList = new ArrayList<>();
        long startTime = System.currentTimeMillis();
        try {
            List<HandleBo> handleBoList = gridPlanManage.calculate(start, end).stream().sorted(Comparator.comparing(HandleBo::getSumPrice)).collect(Collectors.toList());
            handleBoList.forEach(s -> {
                HandleVo vo = new HandleVo();
                vo.setSumPrice(String.format("%.3f", s.getSumPrice()));
                vo.setHandlePath(StringUtils.collectionToDelimitedString(s.getHandlePath(), "-"));
                HandleVoList.add(vo);
            });
        } catch (Exception e) {
            LOG.error("计算异常", e);
        }
        long endTime = System.currentTimeMillis();
        System.out.println("总计算耗时：" + (endTime - startTime) / 1000 + "秒");
        return HandleVoList;
    }

    @GetMapping(value = "/aStarCalculate")
    @ResponseBody
    public List<HandleVo> aStarCalculate(long start, long end) {
        List<HandleVo> HandleVoList = new ArrayList<>();
        long startTime = System.currentTimeMillis();
        try {
            List<HandleBo> handleBoList = aStarCalculateManage.calculate(start, end).stream().sorted(Comparator.comparing(HandleBo::getSumPrice)).collect(Collectors.toList());
            handleBoList.forEach(s -> {
                HandleVo vo = new HandleVo();
                vo.setSumPrice(String.format("%.3f", s.getSumPrice()));
                vo.setHandlePath(StringUtils.collectionToDelimitedString(s.getHandlePath(), "-"));
                HandleVoList.add(vo);
            });
        } catch (Exception e) {
            LOG.error("计算异常", e);
        }
        long endTime = System.currentTimeMillis();
        System.out.println("总计算耗时：" + (endTime - startTime) / 1000 + "秒");
        return HandleVoList;
    }


    @PostMapping("/calculatePath")
    @ResponseBody
    public BaseResponse<List<CalculateVo>> calculate(@RequestBody CalculateRequest calculateRequest) {

        List<CalculateVo> calculateVoList = new ArrayList<>();
        LOG.info("{}开始计算，参数：{}", calculateRequest.getRequestNo(), JsonUtil.tryToString(calculateRequest));
        long startTime = System.currentTimeMillis();
        LogUtil.requestStart(calculateRequest.getRequestNo());
        String[] name = {"方案一", "方案二", "方案三"};
        try {
            List<CabinetBo> cabinetBoList = queryCabinetBoList(calculateRequest.getCabinetType(), calculateRequest.getNeedNo());

            FillRequirementBo fillRequirementBo = fillRequirementService.selectOne(calculateRequest.getNeedNo());

            List<HandleBo> handleBoList = calculateManage.calculate(cabinetBoList, fillRequirementBo);
            for (int i = 0; i < handleBoList.size(); i++) {
                HandleBo handleBo = handleBoList.get(i);
                CalculateVo vo = new CalculateVo();
                vo.setSumPrice(Double.parseDouble(String.format("%.3f", handleBo.getSumPrice())));
                vo.setSumDistance(Double.parseDouble(String.format("%.3f", handleBo.getSumDistance())));
                vo.setArea(AreaType.getAreaTypeMap(handleBo.getContext().getFillRequirementBo().getArea()).getDesc());
                vo.setGridNo(handleBo.getContext().getGridNo());
                vo.setNeedNo(handleBo.getContext().getFillRequirementBo().getNeedNo());
                vo.setCaseName(name[i]);
                calculateVoList.add(vo);
            }
        } catch (BizException e) {
            LOG.error("{}计算异常", calculateRequest.getRequestNo(), e);
            return BaseResponse.error(calculateRequest.getRequestNo(), e.getCode(), e.getMsg());
        } catch (Exception e) {
            LOG.error("{}计算异常", calculateRequest.getRequestNo(), e);
            return BaseResponse.error(calculateRequest.getRequestNo(), ResponseCodeEnum.DEFAULT_ERROR);
        }
        long endTime = System.currentTimeMillis();
        LOG.info("{}计算成功，总耗时：{}", calculateRequest.getRequestNo(), (endTime - startTime) / 1000 + "秒");
        return BaseResponse.success(calculateRequest.getRequestNo(), calculateVoList);
    }

    @PostMapping("/schemeConfirm")
    @ResponseBody
    public BaseResponse schemeConfirm(@RequestBody CalculateRequest calculateRequest) {

        try {
            calculateManage.saveScheme(calculateRequest.getNeedNo());
        } catch (BizException e) {
            LOG.error("{}方案保存异常", calculateRequest.getRequestNo(), e);
            return BaseResponse.error(calculateRequest.getRequestNo(), e.getCode(), e.getMsg());
        } catch (Exception e) {
            LOG.error("{}方案保存异常", calculateRequest.getRequestNo(), e);
            return BaseResponse.error(calculateRequest.getRequestNo(), ResponseCodeEnum.DEFAULT_ERROR);
        }
        return BaseResponse.success(calculateRequest.getRequestNo(), null);
    }

    private List<CabinetBo> queryCabinetBoList(Integer[] cabinetType, String needNo) {
        List<CabinetBo> cabinetBoList = new ArrayList<>();
        Set<Integer> cabinetTypeSet = Sets.newHashSet(Arrays.asList(cabinetType));
        for (Integer value : cabinetTypeSet) {
            CabinetCategoryType type = CabinetCategoryType.getCabinetCategoryTypeMap(value);
            switch (type) {
                case EXIST_LINE_EXIST_CATEGORY:
                    cabinetBoList.addAll(cabinetChoiceManage.selectExistCabinet(needNo));
                    break;
                case EXIST_LINE_PLAN_CATEGORY:
                    cabinetBoList.addAll(cabinetChoiceManage.selectPlanCabinetExistLine(needNo));
                    break;
                case PLAN_LINE_PLAN_CATEGORY:
                    cabinetBoList.addAll(cabinetChoiceManage.selectPlanCabinetPlanLine(needNo));
                    break;
                default:
                    break;
            }
        }
        return cabinetBoList;
    }

}