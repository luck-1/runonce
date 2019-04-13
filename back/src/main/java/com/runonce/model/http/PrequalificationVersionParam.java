package com.runonce.model.http;

import com.runonce.model.eventversionone.Prequalification;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class PrequalificationVersionParam {


    @ApiModelProperty(value = "版本")
    private Integer version;

    @ApiModelProperty(value = "资格预审")
    private List<Prequalification> list;
}
