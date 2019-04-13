package com.runonce.model.http;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class EventLevel {

    @ApiModelProperty(value = "省级")
    private Integer provincialLevel;

    @ApiModelProperty(value = "市级")
    private Integer cityLevel;

    @ApiModelProperty(value = "区（县）级")
    private Integer countyLevel;

    @ApiModelProperty(value = "区（县）级个性化事项")
    private Integer countyPrivateLevel;
}