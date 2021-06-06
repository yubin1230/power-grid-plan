package com.power.grid.plan.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class GridL2Po {

    private Integer id;

    private String gridNo;

    private String gridName;

    private Integer gridType;

    private Date createTime;

    private String createUser;

    private Date updateTime;

    private String updateUser;

    private Integer isDeleted;

    private Date ts;

    private String gridEdge;


}