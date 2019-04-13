package com.runonce.model.http;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DealCount {

    @ApiModelProperty(value = "总办理项")
    private Integer allDeal;

    @ApiModelProperty(value = "待办理项")
    private Integer stayDeal;

    @ApiModelProperty(value = "已完成办理项")
    private Integer accomplishDeal;

}