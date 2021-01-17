package com.power.grid.plan.service.manager;

import com.power.grid.plan.dto.bo.HandleBo;
import com.power.grid.plan.dto.bo.RoadHandleBo;
import com.power.grid.plan.service.CalculateService;
import lombok.Data;
import org.apache.commons.collections.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;

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


    private volatile Map<Long, RoadHandleBo> roadHandleBoMap;

    private long start;

    private long end;

    private volatile List<HandleBo> handleBoList = Collections.synchronizedList(new ArrayList<>());


    public AntCalculateManage() {
    }

    public void initAntCalculateManage(Map<Long, RoadHandleBo> roadHandleBoMap, long start, long end, int antNum) {
        this.roadHandleBoMap = roadHandleBoMap;
        this.start = start;
        this.end = end;
        this.antNum = antNum;
    }

    public List<HandleBo> handle() {
        long startTime = System.currentTimeMillis();
        Set<Long> deadIds = new HashSet<>();
        for (int j = 0; j < antNum; j++) {
            //计算路径
//            Map<Long, RoadHandleBo> currentMap=new HashMap<>(roadHandleBoMap);
            HandleBo boCalculate = calculateService.handle(start, end, roadHandleBoMap, deadIds);
//            System.out.println(Thread.currentThread().getName() + "死亡节点数量：" + deadIds.size() + "个");

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
        //挥发回路信息素，加快收敛
//        volatilizePheromone(deadIds);

        long endTime = System.currentTimeMillis();
//        System.out.println(Thread.currentThread().getName() + "计算50只蚂蚁计算耗时：" + (endTime - startTime) / 1000 + "秒");
        return handleBoList;
    }

    private List<HandleBo> setBestHandle(HandleBo bo) {
        List<HandleBo> currentList = new ArrayList<>(handleBoList);
        if (currentList.size() < 1) {
            currentList.add(bo);
            synchronized (this) {
                handleBoList = currentList;
            }
            return currentList;
        }
        for(HandleBo handleBo:currentList){
            if (bo.getSumPrice() <= handleBo.getSumPrice() && !currentList.contains(bo)) {
                currentList.add(bo);
                break;
            }
        }

        currentList = currentList.stream().sorted(Comparator.comparing(HandleBo::getSumPrice)).collect(Collectors.toList()).subList(0,1);
        synchronized (this) {
            handleBoList = currentList;
        }
        return currentList;
    }

    private synchronized void releasePheromone(List<HandleBo> handleBoList) {
        calculateService.releasePheromone(roadHandleBoMap, handleBoList);
    }

    private synchronized void volatilizePheromone() {
        calculateService.volatilizePheromone(roadHandleBoMap);
    }

    private synchronized void volatilizePheromone(Long deadId) {
        calculateService.volatilizePheromone(roadHandleBoMap,deadId);
    }
}
