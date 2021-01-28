package com.power.grid.plan.dto.bo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 禁止开挖路段
 * @author yubin
 * @date 2021/1/28 22:57
 */
@Data
public class NoGoBo {

    @JsonProperty("roadID")
    private Long roadID;

    @JsonProperty("type")
    private Integer type;
}
