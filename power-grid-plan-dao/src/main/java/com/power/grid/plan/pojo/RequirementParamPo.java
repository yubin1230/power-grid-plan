package com.power.grid.plan.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class RequirementParamPo {
    private Integer id;

    private String needNo;

    private Integer industryRatio;

    private Integer businessRatio;

    private Integer residentRatio;

    private Integer chargingPileRatio;

    private Integer blendRatio;

    private Integer industryMaximumCapacity;

    private Integer businessMaximumCapacity;

    private Integer residentMaximumCapacity;

    private Integer chargingPileMaximumCapacity;

    private Integer a1AutomationRatio;

    private Integer a2AutomationRatio;

    private Integer bAutomationRatio;

    private Integer lineThreshold;

    private Double lineThresholdRatio;

    private Double a1IndustryLoad;

    private Double a1IndustryLoadRatio;

    private Integer a1IndustryCapacity;

    private Double a1IndustryCapacityRatio;

    private Integer a1IndustryUsers;

    private Double a1BusinessLoad;

    private Double a1BusinessLoadRatio;

    private Integer a1BusinessCapacity;

    private Double a1BusinessCapacityRatio;

    private Integer a1BusinessUsers;

    private Double a1ResidentLoad;

    private Double a1ResidentLoadRatio;

    private Integer a1ResidentCapacity;

    private Double a1ResidentCapacityRatio;

    private Integer a1ResidentUsers;

    private Double a2bIndustryLoad;

    private Double a2bIndustryLoadRatio;

    private Integer a2bIndustryCapacity;

    private Double a2bIndustryCapacityRatio;

    private Integer a2bIndustryUsers;

    private Double a2bBusinessLoad;

    private Double a2bBusinessLoadRatio;

    private Integer a2bBusinessCapacity;

    private Double a2bBusinessCapacityRatio;

    private Integer a2bBusinessUsers;

    private Double a2bResidentLoad;

    private Double a2bResidentLoadRatio;

    private Integer a2bResidentCapacity;

    private Double a2bResidentCapacityRatio;

    private Integer a2bResidentUsers;

    private Integer a1NodeNum;

    private Integer a2NodeNum;

    private Integer bNodeNum;

    private Date createTime;

    private String createUser;

    private Date updateTime;

    private String updateUser;

    private Integer isDeleted;

    private Date ts;
}