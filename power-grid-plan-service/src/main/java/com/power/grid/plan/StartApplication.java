package com.power.grid.plan;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ImportResource;

/**
 * 作者（@author wangchun8 部门： 开放库存研发部 ）
 * 版本（@version 1.0）
 * 创建、开发日期（@date 2020/6/24 14:25）
 **/
@SpringBootApplication
@ImportResource(locations = {"classpath:spring/applicationContext.xml"})
@MapperScan(basePackages = "com.power.grid.plan.mapper")
public class StartApplication extends SpringBootServletInitializer {
    private static final Logger LOG = LogManager.getLogger(StartApplication.class);
    public static void main(String[] args) {
        try{
            LOG.info("服务启动...");
            SpringApplication.run(StartApplication.class, args);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(StartApplication.class);
    }
}
