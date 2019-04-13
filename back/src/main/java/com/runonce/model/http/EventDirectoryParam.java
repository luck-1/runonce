package com.runonce.model.http;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

@Data
public class EventDirectoryParam {

    @ApiModelProperty(value = "实施部门")
    private String implementatioDepartment;

    @ApiModelProperty(value = "通用名称")
    private String genericName;

    @ApiModelProperty(value = "省级名称")
    private String provincialName;

    @ApiModelProperty(value = "事项大类型")
    private String bigTypesOfEvents;

    @ApiModelProperty(value = "事项小类型")
    private String itemSubtype;

    private Integer page;

    private Integer size;
}
