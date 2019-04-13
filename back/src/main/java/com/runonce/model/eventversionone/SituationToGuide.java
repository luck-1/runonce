package com.runonce.model.eventversionone;
import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
*  情形引导
*
*  Created by xuxueli on '2019-02-27 14:09:17'.
*/
@Data
@Table(name="situation_to_guide")
public class SituationToGuide implements Serializable {

    private static final long serialVersionUID = 42L;

    @Id
    @ApiModelProperty(value = "id")
    @Column(name = "id")
    private String id;

    @NotBlank(message = "\"事项名称\"不能为空")
    @ApiModelProperty(value = "事项名称")
    @Column(name = "eventName")
    private String eventName;

    @NotBlank(message = "\"申请事项\"不能为空")
    @ApiModelProperty(value = "申请事项")
    @Column(name = "eventOfApplication")
    private String eventOfApplication;

    @Transient
    @NotEmpty(message = "\"关键字\"不能为空")
    @ApiModelProperty(value = "关键字")
    private List<String> keyword;

    @Transient
    @ApiModelProperty(value = "子项")
    private List<SituationToGuideChild> children;


}