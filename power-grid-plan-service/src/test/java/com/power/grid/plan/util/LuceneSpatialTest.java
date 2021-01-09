package com.power.grid.plan.util;


import com.power.grid.plan.dto.bo.NodeBo;
import com.power.grid.plan.service.InitService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

/**
 * 计算范围测试类
 * @author yubin
 * @date 2021/1/9 22:56
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class LuceneSpatialTest {

    @Resource
    private InitService initService;

    @Resource
    private LuceneSpatial luceneSpatial;

    @Test
    public void luceneSpatial() throws Exception {
        List<NodeBo> nodeBoList=initService.initNodeInfo();
        luceneSpatial.createIndex(nodeBoList);
        System.out.println(nodeBoList.get(0));
        List<NodeBo> list=luceneSpatial.search(nodeBoList.get(0),5);
        System.out.println(list.size());
        Assert.assertFalse(list.isEmpty());
    }



}