package com.power.grid.plan.vo;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class CabinetVo {

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
}