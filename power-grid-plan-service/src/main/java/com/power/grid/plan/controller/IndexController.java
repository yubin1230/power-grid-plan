package com.power.grid.plan.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 作者（@author wangchun8 部门： 开放库存研发部 ）
 * 版本（@version 1.0）
 * 创建、开发日期（@date 2020/6/24 14:41）
 **/
@RestController
@RequestMapping("/api")
public class IndexController {
    private static final Logger LOG = LogManager.getLogger(IndexController.class);

    @GetMapping("/index")
    public String index() {
        LOG.info("主页访问");
        return "库存中心-核心系统";

    }
}
