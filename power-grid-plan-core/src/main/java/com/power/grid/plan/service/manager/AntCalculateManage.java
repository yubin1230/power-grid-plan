package com.power.grid.plan.service.manager;

import com.power.grid.plan.dto.bo.HandleBo;
import com.power.grid.plan.dto.bo.RoadHandleBo;
import com.power.grid.plan.exception.DeadCircleException;
import com.power.grid.plan.service.CalculateService;
import lombok.Data;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

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

    private volatile HandleBo bestHandleBo;


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

        for (int j = 0; j < antNum; j++) {
            //计算路径
            HandleBo boCalculate = calculateService.handle(start, end, roadHandleBoMap);

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
