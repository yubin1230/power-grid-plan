package com.power.grid.plan.service.manager;

import com.power.grid.plan.dto.bo.HandleBo;
import com.power.grid.plan.dto.bo.RoadHandleBo;
import com.power.grid.plan.service.CalculateService;
import com.power.grid.plan.util.RemoveLoopRoad;
import lombok.Data;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 蚂蚁计算管理类
 * @author yubin
 * @date 2021/1/10 13:27
 */
@Data
public class AntCalculateManage {

    private static final Logger LOG = LogManager.getLogger(AntCalculateManage.class);

    private CalculateService calculateService;

    /**
     * 蚂蚁数量
     */
    private int antNum;


    private volatile Map<Long, RoadHandleBo> roadHandleBoMap;

    private long start;

    private long end;

    private volatile List<HandleBo> handleBoList;


    public AntCalculateManage(Map<Long, RoadHandleBo> roadHandleBoMap, List<HandleBo> handleBoList, long start, long end, int antNum) {
        this.roadHandleBoMap = roadHandleBoMap;
        this.start = start;
        this.end = end;
        this.antNum = antNum;
        this.handleBoList = handleBoList;
    }

    public List<HandleBo> handle() {
        Set<Long> deadIds = new HashSet<>();
        for (int j = 0; j < antNum; j++) {
            //计算路径
            HandleBo boCalculate = calculateService.handle(start, end, roadHandleBoMap, deadIds);

            //已选择路径，不再释放信息素，重新计算
//            if (handleBoList.contains(boCalculate)) {
//                LOG.info("已包含最优路径，重新计算。");
//                j--;
//                continue;
//            }

            List<HandleBo> currentList = setBestHandle(boCalculate);
            //释放信息素
            releasePheromone(currentList);
        }
        //挥发信息素
        volatilizePheromone();

        return handleBoList;
    }

    private List<HandleBo> setBestHandle(HandleBo bo) {
        synchronized (this) {
            List<HandleBo> currentList = new ArrayList<>(handleBoList);
            if (currentList.size() < 3) {
                currentList.add(bo);
                handleBoList = currentList;
                return currentList;
            }
            for (HandleBo handleBo : currentList) {
                if (bo.getSumPrice() <= handleBo.getSumPrice() && !currentList.contains(bo)) {
                    //            删除回路
                    long startTime = System.currentTimeMillis();
                    bo= RemoveLoopRoad.removeLoopRoad(roadHandleBoMap,bo);
                    long endTime = System.currentTimeMillis();
                    System.out.println("删除回路耗时：" + (endTime - startTime) + "毫秒");
                    currentList.add(bo);
                    break;
                }
            }
            currentList = currentList.stream().sorted(Comparator.comparing(HandleBo::getSumPrice)).collect(Collectors.toList()).subList(0, 3);
            handleBoList = currentList;
            return currentList;
        }
    }

    private synchronized void releasePheromone(List<HandleBo> handleBoList) {
        calculateService.releasePheromone(roadHandleBoMap, handleBoList);
    }

    private synchronized void volatilizePheromone() {
        calculateService.volatilizePheromone(roadHandleBoMap);
    }
}
