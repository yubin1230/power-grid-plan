package com.power.grid.plan.pojo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class RoadPo {

    private Long id;

    private String roadNo;

    private String roadName;

    private Long startNodeId;

    private Long endNodeId;

    private Double distance;

    private Double roadWidth;

    private Integer roadClass;

    private Integer roadType;

    private Integer rodeLay;

    private Double price;

    private Integer roadStatus;

    private Date useTime;

    private String gridNo;

    private Date createTime;

    private String createUser;

    private Date updateTime;

    private String updateUser;

    private Integer isDeleted;

    private Date ts;
}