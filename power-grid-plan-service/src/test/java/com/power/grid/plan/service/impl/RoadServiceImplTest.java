package com.power.grid.plan.service.impl;

import com.power.grid.plan.service.InitService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;


/**
 * 初始化测试
 * @author yubin
 * @date 2021/1/9 19:07
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RoadServiceImplTest{

    @Resource
    private InitService initService;

    @Test
    public void testCalculate() {
        initService.initRoadInfo();
    }

}