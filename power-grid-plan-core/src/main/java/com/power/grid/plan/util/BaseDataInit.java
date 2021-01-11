package com.power.grid.plan.util;


import com.fasterxml.jackson.core.type.TypeReference;
import com.power.grid.plan.JsonUtil;
import com.power.grid.plan.dto.bo.NodeBo;
import com.power.grid.plan.dto.bo.RoadBo;
import com.power.grid.plan.service.coordinate.LuceneSpatial;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

@Component
public class BaseDataInit implements CommandLineRunner {

    private static final Logger LOG = LogManager.getLogger(BaseDataInit.class);


    private List<RoadBo> roadBoList;

    private List<NodeBo> nodeBoList;

    @Resource
    private LuceneSpatial luceneSpatial;

    public List<RoadBo> initRoadInfo() {

        List<RoadBo> roadBoList;
        try {
            InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("static/data.json");
            BufferedReader data = new BufferedReader(new InputStreamReader(is));
            StringBuilder dataJson=new StringBuilder();
            while (true){
                String strBuff = data.readLine();
                if(StringUtils.isBlank(strBuff)){
                    break;
                }
                dataJson.append(strBuff);
            }
            roadBoList=JsonUtil.parsJson(dataJson.toString(), new TypeReference<List<RoadBo>>(){});
        } catch (Exception e) {
            throw new RuntimeException("数据初始化异常"+e);
        }
        return roadBoList;
    }

    public List<NodeBo> initNodeInfo() {

        List<NodeBo> nodeBoList;
        try {
            InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("static/node.json");
            BufferedReader data = new BufferedReader(new InputStreamReader(is));
            StringBuilder dataJson=new StringBuilder();
            while (true){
                String strBuff = data.readLine();
                if(StringUtils.isBlank(strBuff)){
                    break;
                }
                dataJson.append(strBuff);
            }
            nodeBoList=JsonUtil.parsJson(dataJson.toString(),new TypeReference<List<NodeBo>>(){});
        } catch (Exception e) {
            throw new RuntimeException("数据初始化异常");
        }
        return nodeBoList;
    }

    @Override
    public void run(String... args) throws Exception {
        //路段信息
        roadBoList=initRoadInfo();
        LOG.info("初始化路段信息初始化完成，路段信息数量：{}",roadBoList.size());
        //节点信息
        nodeBoList=initNodeInfo();
        LOG.info("初始化节点信息初始化完成，节点信息数量：{}",roadBoList.size());
        //加载计算节点信息
        luceneSpatial.createIndex(nodeBoList);
        LOG.info("位置信息计算初始化完成");
    }

    public List<RoadBo> getRoadBoList() {
        return roadBoList;
    }


    public List<NodeBo> getNodeBoList() {
        return nodeBoList;
    }
}
