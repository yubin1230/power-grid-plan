package com.power.grid.plan.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class PlanSchemePo {
    private Integer id;

    private String caseNo;

    private String caseName;

    private String cabinetNo;

    private int cabinetType;

    private Double caseCost;

    private Double caseLength;

    private String needNo;

    private Integer isChoose;

    private Date createTime;

    private String createUser;

    private Date updateTime;

    private String updateUser;

    private Integer isDeleted;

    private Date ts;
}