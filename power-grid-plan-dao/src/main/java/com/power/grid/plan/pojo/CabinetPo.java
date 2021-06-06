package com.power.grid.plan.pojo;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class CabinetPo {
    private Integer id;

    private String cabinetNo;

    private Double longitude;

    private Double latitude;

    private String type;

    private Integer clValue;

    private Integer capacity;

    private Integer useCapacity;

    private String outputNo;

    private String outputUsedNo;

    private String gridNo;

    private String lineNo;

    private Integer automation;

    private String controlType;

    private Date createTime;

    private String createUser;

    private Date updateTime;

    private String updateUser;

    private Integer isDeleted;

    private Date ts;
}