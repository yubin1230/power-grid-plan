package com.power.grid.plan.pojo;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class LinePo {
    private Integer id;

    private String lineNo;

    private Integer lineType;

    private String linePoints;

    private Integer linePower;

    private Integer linePowerUsed;

    private Integer planLinePowerUsed;

    private String stationName;

    private Integer overload;

    private String busNo;

    private String flName;

    private Integer overallDesirability;

    private String gridNo;

    private Date createTime;

    private String createUser;

    private Date updateTime;

    private String updateUser;

    private Integer isDeleted;

    private Date ts;
}