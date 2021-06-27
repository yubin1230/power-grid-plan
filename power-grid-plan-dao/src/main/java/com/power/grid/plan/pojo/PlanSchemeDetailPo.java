package com.power.grid.plan.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class PlanSchemeDetailPo {

    private Integer id;

    private String caseNo;

    private String roadNo;

    private String casePoints;

    private Double pathLength;

    private Double cost;

    private Integer roadIndex;

    private Integer layWay;

    private Integer cableType;

    private Date createTime;

    private String createUser;

    private Date updateTime;

    private String updateUser;

    private Integer isDeleted;

    private Date ts;
}