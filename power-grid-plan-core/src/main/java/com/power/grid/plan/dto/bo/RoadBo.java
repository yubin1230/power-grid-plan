package com.power.grid.plan.dto.bo;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
* 路径业务类
* @author yubin
* @date 2020/11/30 23:10
*/
@Data
public class RoadBo {


    /**
    * 主键
    */
    @JsonProperty("road")
    private Long id;

    /**
    * 开始节点ID
    */
    @JsonProperty("start")
    private Long startNodeId;

    /**
    * 结束节点ID
    */
    @JsonProperty("end")
    private Long endNodeId;

    /**
    * 路段距离
    */
    @JsonProperty("distance")
    private Double distance;

    /**
    * 每米造价
    */
    @JsonProperty("price")
    private Double price;


    public RoadBo(Long id, Long startNodeId, Long endNodeId, Double distance, Double price) {
        this.id = id;
        this.startNodeId = startNodeId;
        this.endNodeId = endNodeId;
        this.distance = distance;
        this.price = price;
    }
}
