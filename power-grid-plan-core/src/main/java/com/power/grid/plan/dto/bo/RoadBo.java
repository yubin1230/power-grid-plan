package com.power.grid.plan.dto.bo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
* 路径业务类
* @author yubin
* @date 2020/11/30 23:10
*/
@Data
@Builder
public class RoadBo {


    /**
    * 主键
    */
    @JsonProperty("road")
    private Long id;

    /**
    * 路段编码
    */
    private String roadNo;

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


    private String roadName;

    private Double roadWidth;

    private Integer roadClass;

    private Integer roadType;

    private Integer rodeLay;

    private Integer roadStatus;

    private Date useTime;

    private String gridNo;

    public RoadBo() {
    }

    public RoadBo(Long id, String roadNo, Long startNodeId, Long endNodeId, Double distance, Double price, String roadName, Double roadWidth, Integer roadClass, Integer roadType, Integer rodeLay, Integer roadStatus, Date useTime, String gridNo) {
        this.id = id;
        this.roadNo = roadNo;
        this.startNodeId = startNodeId;
        this.endNodeId = endNodeId;
        this.distance = distance;
        this.price = price;
        this.roadName = roadName;
        this.roadWidth = roadWidth;
        this.roadClass = roadClass;
        this.roadType = roadType;
        this.rodeLay = rodeLay;
        this.roadStatus = roadStatus;
        this.useTime = useTime;
        this.gridNo = gridNo;
    }
}
