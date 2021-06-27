package com.power.grid.plan.dto.bo;

import com.power.grid.plan.dto.enums.CabinetCategoryType;
import lombok.Builder;
import lombok.Data;

import static com.power.grid.plan.dto.enums.CabinetCategoryType.*;


@Data
@Builder
public class CabinetBo {

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

    private CabinetCategoryType cabinetCategory;

    private String stationName;
}