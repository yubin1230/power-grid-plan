package com.power.grid.plan.service.manager;


import com.power.grid.plan.dto.bo.HandleBo;
import com.power.grid.plan.dto.bo.RoadBo;
import com.power.grid.plan.dto.bo.RoadHandleBo;
import com.power.grid.plan.exception.DeadCircleException;
import com.power.grid.plan.service.CalculateService;
import com.power.grid.plan.service.InitService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

/**
 * 网格计划服务
 * @author yubin
 * @date 2020/12/5 15:54
 */
@Component
public class GridPlanManage {

    private static final Logger LOG = LogManager.getLogger(GridPlanManage.class);

    /**
     * 蚂蚁数量
     */
    private int antNum=50;

    /**
     * 循环次数
     */
    private int loop=100;

    /**
    * 返回路径数量
    */
    private int pathNum=3;

    @Resource
    private InitService initService;

    @Resource
    private CalculateService calculateService;

    public List<HandleBo> calculate(Long start, Long end){

        List<HandleBo> handleBoList=new ArrayList<>();

        for(int i=0;i<pathNum;i++){
            HandleBo bo=calculateBestPath(start,end,handleBoList);
            handleBoList.add(bo);
        }

        return handleBoList;
    }

    public HandleBo calculateBestPath(Long start, Long end,List<HandleBo> handleBoList){
        HandleBo bo=new HandleBo();
        List<RoadBo> roadBoList= initService.initRoadInfo();
        //初始化概率
        Map<Long, RoadHandleBo> roadHandleBoMap=calculateService.initProbability(roadBoList);
        for(int i=0;i<loop;i++){
            for(int j=0;j<antNum;j++){
                //计算路径
                HandleBo boCalculate;
                try{
                    boCalculate= calculateService.handle(start,end,roadHandleBoMap);
                }catch (DeadCircleException exception){
                    LOG.info("计算死循环，重新计算。");
                    j--;
                    continue;
                }
                //已选择路径，不再释放信息素，重新计算
                if(handleBoList.contains(boCalculate)){
                    LOG.info("已包含最优路径，重新计算。");
                    j--;
                    continue;
                }
                if(boCalculate.getSumPrice()<bo.getSumPrice()){
                    bo=boCalculate;
                }
                //释放信息素
                calculateService.releasePheromone(roadHandleBoMap,bo);
            }
            //挥发信息素
            calculateService.volatilizePheromone(roadHandleBoMap);
        }
        return bo;
    }

}
