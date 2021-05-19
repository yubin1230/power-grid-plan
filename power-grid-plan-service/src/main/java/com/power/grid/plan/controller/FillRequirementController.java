package com.power.grid.plan.controller;

import com.power.grid.plan.BaseResponse;
import com.power.grid.plan.JsonUtil;
import com.power.grid.plan.ResponseCodeEnum;
import com.power.grid.plan.dto.bo.FillRequirementBo;
import com.power.grid.plan.vo.FillRequirementVo;
import com.power.grid.plan.service.FillRequirementService;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * 作者（@author yubin ）
 * 版本（@version 1.0）
 * 创建、开发日期（@date 2020/6/24 14:41）
 **/
@RestController
@RequestMapping("/fillRequirement")
public class FillRequirementController {

    private static final Logger LOG = LogManager.getLogger(FillRequirementController.class);

    @Resource
    private FillRequirementService fillRequirementService;

    @PostMapping("/insert")
    public BaseResponse insert(@Valid FillRequirementVo vo, BindingResult bindingResult) {
        try {
            LOG.info("requestNo：{} 新增用户需求：{}", vo.getRequestNo(), JsonUtil.tryToString(vo));

            // 参数校验
            if (bindingResult.hasErrors()) {
                String messages = bindingResult.getAllErrors()
                        .stream()
                        .map(ObjectError::getDefaultMessage)
                        .reduce((m1, m2) -> m1 + "；" + m2)
                        .orElse("参数输入有误！");
                return BaseResponse.error(vo.getRequestNo(), ResponseCodeEnum.PARAM_ERROR.getCode(),messages);
            }

            if (Objects.nonNull(vo.getDoubleCapacity())) {
                vo.setDoublePower(2);
            }
            FillRequirementBo bo = new FillRequirementBo();
            BeanUtils.copyProperties(vo, bo);
            bo.setNeedNo(createNeedo());
            fillRequirementService.insert(bo);
            LOG.info("requestNo：{} 新增用户需求成功", vo.getRequestNo());
            return BaseResponse.success(vo.getRequestNo());
        } catch (Exception e) {
            LOG.error("requestNo：{} 新增用户需求异常", vo.getRequestNo(), e);
            return BaseResponse.error(vo.getRequestNo(), ResponseCodeEnum.DEFAULT_ERROR);
        }
    }

    /**
     * 生成序列号
     */
    private String createNeedo() {
        String strDateFormat1 = "yyyyMMddHHmmss";
        SimpleDateFormat sdf1 = new SimpleDateFormat(strDateFormat1);
        return String.format("%s%s%s", "SZFT", sdf1.format(new Date()), String.valueOf((int) ((Math.random() * 9 + 1) * 100)));
    }
}
