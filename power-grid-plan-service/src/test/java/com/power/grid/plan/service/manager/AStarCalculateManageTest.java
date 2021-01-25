package com.power.grid.plan.service.manager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.concurrent.ExecutionException;


/**
 * A星算法测试
 * @author yubin
 * @date 2021/1/25 21:14
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class AStarCalculateManageTest {

    @Resource
    private AStarCalculateManage aStarCalculateManage;

    @Test
    public void calculate() throws InterruptedException, ExecutionException, IOException {
        aStarCalculateManage.calculate(10970659L, 11265977L);
    }
}