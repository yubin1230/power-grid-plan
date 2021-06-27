package com.power.grid.plan.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class PlanCabinetPo {
    private Long id;

    private String cabinetNo;

    private String caseNo;

    private String stationName;

    private String lineNo;

    private String boxName;

    private Integer voltage;

    private Integer outLine;

    private Integer type;

    private Integer controlType;

    private Double longitude;

    private Double latitude;

    private String gridNo;

    private Date createTime;

    private String createUser;

    private Date updateTime;

    private String updateUser;

    private Integer isDeleted;

    private Date ts;
}