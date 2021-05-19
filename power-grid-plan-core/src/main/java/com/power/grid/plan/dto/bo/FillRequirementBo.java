package com.power.grid.plan.dto.bo;

import lombok.Data;


/**
 * 填报需求实体
 * @author yubin
 * @version 1.0
 * @date 2021-05-16
 */
@Data
public class FillRequirementBo {

    /**
     * 主键
     */
    private Integer id;

    /**
     * 需求编码
     */
    private String needNo;

    /**
     * 报装用户名称
     */
    private String needName;

    /**
     * 报装容量
     */
    private Integer needCapacity;

    /**
     * 变压器构成
     */
    private String transformerConstitute;

    /**
     * 报装位置
     */
    private String locationName;

    /**
     * 报装位置经度
     */
    private Double longitude;

    /**
     * 报装位置纬度
     */
    private Double latitude;

    /**
     * 负荷性质 1：工业 2：商业 3：居民 4：充电桩 5：综合
     */
    private Integer type;

    /**
     * 是否需要双电源  1：不需要 2：需要
     */
    private Integer doublePower;

    /**
     * 双电源容量
     */
    private Integer doubleCapacity;

    /**
     * 业扩类型 1：新增 2：扩容 3：减容
     */
    private Integer bizType;

    /**
     * 业扩期限 1：永久 2：临时
     */
    private Integer bizTerm;

    /**
     * 区域 1：A+ 2: A 3:B
     */
    private Integer area;

    /**
     * 供电电房编码集合
     */
    private String electricRoomNos;

    /**
     * 用户电能质量敏感级别 1:一级 2：二级 3：三级 4：四级 5：五级
     */
    private Integer sensitivityLevel;


}
