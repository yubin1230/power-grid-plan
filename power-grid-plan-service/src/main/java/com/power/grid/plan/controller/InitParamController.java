package com.power.grid.plan.controller;

import com.power.grid.plan.BaseResponse;
import com.power.grid.plan.JsonUtil;
import com.power.grid.plan.ResponseCodeEnum;
import com.power.grid.plan.dto.bo.RequirementParamBo;
import com.power.grid.plan.service.InitParamService;
import com.power.grid.plan.vo.RequirementParamVo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


/**
 * 作者（@author yubin ）
 * 版本（@version 1.0）
 * 创建、开发日期（@date 2020/6/24 14:41）
 **/
@RestController
@RequestMapping("/initRequirementParam")
public class InitParamController {

    private static final Logger LOG = LogManager.getLogger(InitParamController.class);

    @Resource
    private InitParamService initParamService;

    @PostMapping("/insert")
    public BaseResponse insert(RequirementParamVo vo) {
        try {
            LOG.info("requestNo：{} 初始化参数：{}", vo.getRequestNo(), JsonUtil.tryToString(vo));
            RequirementParamBo bo = new RequirementParamBo();
            BeanUtils.copyProperties(vo, bo);
            initParamService.initRequirementParam(bo);
            LOG.info("requestNo：{} 初始化参数成功", vo.getRequestNo());
            return BaseResponse.success(vo.getRequestNo());
        } catch (Exception e) {
            LOG.error("requestNo：{} 初始化参数异常", vo.getRequestNo(), e);
            return BaseResponse.error(vo.getRequestNo(), ResponseCodeEnum.DEFAULT_ERROR);
        }
    }
}
