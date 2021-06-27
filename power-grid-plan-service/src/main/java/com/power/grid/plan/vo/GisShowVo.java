package com.power.grid.plan.vo;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * L2网格显示信息
 * @author yubin
 * @date 2021/5/30 19:10
 */
@Data
@Builder
public class GisShowVo {

    private GridL2Vo gridL2Vo;

    private List<CabinetVo> cabinetVoList;

    private List<LineVo> lineVoList;

}
