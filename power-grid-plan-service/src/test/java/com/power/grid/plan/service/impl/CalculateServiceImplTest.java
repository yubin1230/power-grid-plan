package com.power.grid.plan.service.impl;

import com.google.common.collect.Lists;
import com.power.grid.plan.dto.bo.RoadBo;
import com.power.grid.plan.dto.bo.RoadHandleBo;
import com.power.grid.plan.dto.bo.WalkBo;
import com.power.grid.plan.service.CalculateService;
import com.power.grid.plan.util.BaseDataInit;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.annotation.Resource;

import java.math.BigDecimal;
import java.util.*;


/**
 * 静态数据测试
 * @author yubin
 * @date 2021/1/13 21:29
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class CalculateServiceImplTest {

    @Resource
    private BaseDataInit baseDataInit;

    @Resource
    private CalculateService calculateService;

    @Test
    public void initProbability() {
        String s = "97147774-1001789-47360219-1001449-984958-1001791-26090377-21787788-1001803-1011278-11265320-1001801-1033601-1001804-1001802-55482136-47360066-47360054-47360052-1001209-55482122-1001212-1004775-97147500-97147504-97147499-97147502-97147503-97147497-97147498-97147505-97147508-97147509-1026321-11265589-47360056-987901-1001216-21787716-54160861-61704473-61704472-1033585-57018086-987903-1043213-57718958-30577398-1049139-949887-47215452-26090463-949883-997331-997365-997368-949894";
        List<String> road = Lists.newArrayList(s.split("-"));
        List<RoadBo> roadBoList = baseDataInit.getRoadBoList();
        Map<Long, RoadHandleBo> roadHandleBoMap = calculateService.initProbability(roadBoList);
        double price = 0.0;
        for (int i = 0; i < road.size() - 1; i++) {
            BigDecimal d = new BigDecimal(Double.toString(price));
            BigDecimal p = new BigDecimal(roadHandleBoMap.get(Long.parseLong(road.get(i))).getSumPrice().get(Long.parseLong(road.get(i + 1))));
            System.out.println(road.get(i) + "价格" + price);
            price = d.add(p).doubleValue();
            if (road.get(i).equals("47360052")) {
                System.out.println(roadHandleBoMap.get(Long.parseLong(road.get(i))));
            }
        }
        System.out.println("最终价格===" + price);
    }
}