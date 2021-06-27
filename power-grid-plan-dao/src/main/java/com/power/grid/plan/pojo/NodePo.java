package com.power.grid.plan.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class NodePo {

    private Long id;

    private String nodeNo;

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