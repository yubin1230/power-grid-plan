package com.power.grid.plan.service.algorithm;


import com.power.grid.plan.dto.bo.RoadHandleBo;
import com.power.grid.plan.dto.bo.WalkBo;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;

/**
 * TODO
 * @author yubin
 * @date 2021/1/14 0:52
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SegmentTest {

    @Resource
    private Segment Segment;

    @Test
    public void calculate() {
        RoadHandleBo bo = new RoadHandleBo();
        bo.setNodeId(100L);
        Map<Long, Double> probability = new HashMap<>();
        probability.put(101L, 0.4);
        probability.put(102L, 0.3);
        probability.put(103L, 0.2);
        probability.put(104L, 0.1);
        Map<Long, Double> price = new HashMap<>();
        price.put(101L, 1.0);
        price.put(102L, 1.0);
        price.put(103L, 1.0);
        price.put(104L, 1.0);
        bo.setProbability(probability);
        bo.setSumPrice(price);
        for (int i = 0; i < 1000; i++) {
            WalkBo walkBo = Segment.calculate(bo, new HashSet<>());
            System.out.println("概率节点" + walkBo.getNext());
        }
    }
}