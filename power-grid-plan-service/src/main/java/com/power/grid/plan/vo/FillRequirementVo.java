package com.power.grid.plan.vo;

import com.power.grid.plan.BaseRequest;
import lombok.Data;

import javax.validation.constraints.*;


/**
 * 填报需求实体
 * @author yubin
 * @version 1.0
 * @date 2021-05-16
 */
@Data
public class FillRequirementVo extends BaseRequest {

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
    @NotNull(message = "用户名称不能为空")
    private String needName;

    /**
     * 报装容量
     */
    @NotNull(message = "报装容量不能为空")
    @Min(value=1)
    private Integer needCapacity;

    /**
     * 变压器构成
     */
    private String transformerConstitute;

    /**
     * 报装位置
     */
    @NotNull(message = "报装位置不能为空")
    private String locationName;

    /**
     * 报装位置经度
     */
    @NotNull(message = "经度不能为空")
    @DecimalMax(value="135.08333333333334",message="请输入合理经度范围")
    @DecimalMin(value="73.55",message="请输入合理经度范围")
    private Double longitude;

    /**
     * 报装位置纬度
     */
    @NotNull(message = "纬度不能为空")
    @DecimalMax(value="53.916666666666664",message="请输入合理纬度范围")
    @DecimalMin(value="4.433333333333334",message="请输入合理纬度范围")
    private Double latitude;

    /**
     * 负荷性质 1：工业 2：商业 3：居民 4：充电桩 5：综合
     */
    @NotNull(message = "负荷性质不能为空")
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
    @NotNull(message = "业扩类型不能为空")
    private Integer bizType;

    /**
     * 业扩期限 1：永久 2：临时
     */
    @NotNull(message = "业扩期限不能为空")
    private Integer bizTerm;

    /**
     * 区域 1：A+ 2: A 3:B
     */
    @NotNull(message = "供电区域类型不能为空")
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
