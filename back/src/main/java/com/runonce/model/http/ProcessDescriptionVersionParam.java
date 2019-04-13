package com.runonce.model.http;

import com.runonce.model.eventversionone.ProcessDescription;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class ProcessDescriptionVersionParam {
    @ApiModelProperty(value = "版本")
    private Integer version;

    @ApiModelProperty(value = "现有流程")
    private List<ProcessDescription> list;

}
