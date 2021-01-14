package com.power.grid.plan.service.algorithm;


import com.power.grid.plan.dto.bo.RoadBo;
import com.power.grid.plan.dto.bo.RoadHandleBo;
import com.power.grid.plan.service.CalculateService;
import com.power.grid.plan.service.manager.GridPlanManage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * TODO
 * @author yubin
 * @date 2021/1/14 0:52
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ArriveTest {

    @Resource
    private GridPlanManage gridPlanManage;

    @Resource
    private CalculateService calculateService;

    @Resource
    private Arrive arrive;

    @Test
    public void arrive() throws IOException {
        long start=97147774L;
        long end=949894L;
        List<RoadBo> roadBoList = gridPlanManage.getRoadBoList(start,end);
        Map<Long, RoadHandleBo> roadHandleBoMap = calculateService.initProbability(roadBoList);
        arrive.arrive(roadHandleBoMap,start,end);
    }
}