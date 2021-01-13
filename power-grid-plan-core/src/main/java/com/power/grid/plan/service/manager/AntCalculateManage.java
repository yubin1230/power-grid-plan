package com.power.grid.plan.service.manager;

import com.power.grid.plan.dto.bo.HandleBo;
import com.power.grid.plan.dto.bo.RoadHandleBo;
import com.power.grid.plan.service.CalculateService;
import lombok.Data;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

/**
 * 蚂蚁计算管理类
 * @author yubin
 * @date 2021/1/10 13:27
 */
@Data
@Component
public class AntCalculateManage {

    private static final Logger LOG = LogManager.getLogger(AntCalculateManage.class);

    @Resource
    private CalculateService calculateService;

    /**
     * 蚂蚁数量
     */
    private int antNum;


    private List<HandleBo> handleBoList;

    private volatile Map<Long, RoadHandleBo> roadHandleBoMap;

    private long start;

    private long end;

    private volatile HandleBo bestHandleBo = new HandleBo();


    public AntCalculateManage() {
    }

    public void initAntCalculateManage(List<HandleBo> handleBoList, Map<Long, RoadHandleBo> roadHandleBoMap, long start, long end, HandleBo bestHandleBo, int antNum) {
        this.handleBoList = handleBoList;
        this.roadHandleBoMap = roadHandleBoMap;
        this.start = start;
        this.end = end;
        this.bestHandleBo = bestHandleBo;
        this.antNum = antNum;
    }

    public HandleBo handle() {

        long startTime = System.currentTimeMillis();
        Set<Long> deadIds = new HashSet<>();
        for (int j = 0; j < antNum; j++) {
            //计算路径
//            Map<Long, RoadHandleBo> currentMap=new HashMap<>(roadHandleBoMap);
            HandleBo boCalculate = calculateService.handle(start, end, roadHandleBoMap, deadIds);

            //已选择路径，不再释放信息素，重新计算
            if (handleBoList.contains(boCalculate)) {
                LOG.info("已包含最优路径，重新计算。");
                j--;
                continue;
            }
            setBestHandle(boCalculate);
            //释放信息素
            releasePheromone();
        }
        //挥发信息素
        volatilizePheromone();
        long endTime = System.currentTimeMillis();
        System.out.println(Thread.currentThread().getName() + "计算50只蚂蚁计算耗时：" + (endTime - startTime) / 1000 + "秒");
        return bestHandleBo;
    }

    private synchronized void setBestHandle(HandleBo bo) {
        if (bo.getSumPrice() < bestHandleBo.getSumPrice()) {
            LOG.info("获取到最佳路径：{}", bo);
            bestHandleBo = bo;
        }
    }

    private synchronized void releasePheromone() {
        calculateService.releasePheromone(roadHandleBoMap, bestHandleBo);
    }

    private synchronized void volatilizePheromone() {
        calculateService.volatilizePheromone(roadHandleBoMap);
    }
}
