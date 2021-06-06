package com.power.grid.plan.controller;


import com.power.grid.plan.dto.bo.HandleBo;
import com.power.grid.plan.dto.vo.HandleVo;
import com.power.grid.plan.service.manager.AStarCalculateManage;
import com.power.grid.plan.service.manager.GridPlanManage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class CalculateController {

    private static final Logger LOG = LogManager.getLogger(CalculateController.class);


    @Resource
    private GridPlanManage gridPlanManage;

    @Resource
    private AStarCalculateManage aStarCalculateManage;

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
            LOG.error("计算异常",e);
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
            LOG.error("计算异常",e);
        }
        long endTime = System.currentTimeMillis();
        System.out.println("总计算耗时：" + (endTime - startTime) / 1000 + "秒");
        return HandleVoList;
    }

}