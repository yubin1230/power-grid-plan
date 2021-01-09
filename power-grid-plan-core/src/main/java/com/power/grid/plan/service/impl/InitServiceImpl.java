package com.power.grid.plan.service.impl;


import com.fasterxml.jackson.core.type.TypeReference;
import com.power.grid.plan.JsonUtil;
import com.power.grid.plan.dto.bo.NodeBo;
import com.power.grid.plan.dto.bo.RoadBo;
import com.power.grid.plan.service.InitService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class InitServiceImpl implements InitService {


    @Override
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
            throw new RuntimeException("数据初始化异常");
        }
        return roadBoList;
    }

    @Override
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
            e.printStackTrace();
            throw new RuntimeException("数据初始化异常");
        }
        return nodeBoList;
    }
}
