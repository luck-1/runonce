package com.runonce.model.http;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class FormThatParam {

    @ApiModelProperty(value = "事件ID")
    private String eventId;

    @ApiModelProperty(value = "材料ID")
    private String materialId;
}
