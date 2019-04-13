package com.runonce.model.http;

import com.runonce.model.eventversionone.FormDescription;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.util.List;
@Data
public class FormDescriptionParam {

    private List<FormDescription> list;

    private String eventId;

    private String materialId;

    private  int state;

    @Transient
    @ApiModelProperty(value = "版本")
    private Integer version;
}
