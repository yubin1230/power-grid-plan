package com.power.grid.plan.dto.bo;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 业务上下文
 * @author yubin
 * @date 2021/6/6 12:34
 */
@Data
@Builder
public class CalculateContextBo {

    private List<RoadBo> roadBoList;

    private Map<String,RoadBo> roadBoMap;

    private Map<String, NodeBo> nodeBoMap;

    private NodeBo start;

    private NodeBo end;

    /**
     * 方案编码
     */
    private String caseNo;

    /**
    * 网格编码
    */
    private String gridNo;

    /**
     * 环网柜
     */
    private CabinetBo cabinetBo;

    /**
     * 用户包装点
     */
    private FillRequirementBo fillRequirementBo;


}
