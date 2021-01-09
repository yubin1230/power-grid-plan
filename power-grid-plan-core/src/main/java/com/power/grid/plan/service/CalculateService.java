package com.power.grid.plan.service;



import com.power.grid.plan.dto.bo.HandleBo;
import com.power.grid.plan.dto.bo.RoadBo;
import com.power.grid.plan.dto.bo.RoadHandleBo;

import java.util.List;
import java.util.Map;

/**
 * 蚂蚁算法计算流程
 * @author yubin
 * @date 2020/11/30 23:59
 */
public interface CalculateService {

    /**
    * 初始化概率
    * @param roadBoList 节点造价信息
    * @return java.util.List<com.neo.domain.bo.ProbabilityBo>
    */
    Map<Long, RoadHandleBo> initProbability(List<RoadBo> roadBoList);

    /**
    * 蚂蚁运行
    * @param start 开始节点
    * @param end 结束节点
    * @param roadHandleBoMap 运行信息
    * @return com.neo.domain.bo.HandleBo
    */
    HandleBo handle(Long start, Long end, Map<Long,RoadHandleBo> roadHandleBoMap);

    /**
    * 释放信息素
    * @param roadHandleBoMap 运行信息
    * @param handleBo  已处理信息
    */
    void releasePheromone(Map<Long,RoadHandleBo> roadHandleBoMap,HandleBo handleBo);


    /**
    * 挥发信息素
    * @param roadHandleBoMap 运行信息
    */
    void volatilizePheromone(Map<Long,RoadHandleBo> roadHandleBoMap);
}
