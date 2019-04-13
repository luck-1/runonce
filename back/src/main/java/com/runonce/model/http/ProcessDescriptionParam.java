package com.runonce.model.http;

import com.runonce.model.eventversionone.ProcessDescription;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @Author: zhaolei
 * @Descriptions:
 * @Date: create at 2018/12/18 0018 下午 4:25
 */
@Data
public class ProcessDescriptionParam implements Serializable {

    // 流程说明列表
    @Valid
    private List<ProcessDescription> processDescriptionList;
    @NotNull(message = "状态不能为空")
    // 资格预审状态(1. 未完善 2.已完善)
    private Integer state;
    @NotBlank(message = "事项id不能为空")
    // 事项id
    private String eventId;

    @Transient
    @ApiModelProperty(value = "版本")
    private Integer version;
}
