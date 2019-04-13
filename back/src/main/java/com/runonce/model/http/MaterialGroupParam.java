package com.runonce.model.http;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;

import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @Author: zhaolei
 * @Descriptions:
 * @Date: create at 2018/12/13 0013 下午 9:43
 */
@Data
public class MaterialGroupParam implements Serializable {
    //材料列表
    @Valid
    private List<MaterialGroupAddParam> materialGroupingParamList;

    @NotBlank(message = "事项id不能为空")
    private String eventId;

    // 材料分组状态(1. 未完善 2.已完善)
    @NotNull(message = "状态不能为空")
    private Integer state;

    @Transient
    @ApiModelProperty(value = "版本")
    private Integer version;
}
