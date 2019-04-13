package com.runonce.model.eventversionone;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * @author lxc
 * @date 2019-02-28 15:47:10
 * @description 情形引导子项
 */
@Data
@Table(name="situation_to_guide_child")
public class SituationToGuideChild implements Serializable {
    private static final long serialVersionUID = 42L;

    @Id
    @ApiModelProperty(value="Id")
    @Column(name = "id")
    private String id;

    @ApiModelProperty(value="名称")
    @Column(name = "eventName")
    @NotBlank(message = "\"名称\"为空")
    private String eventName;

    @ApiModelProperty(value="事项类别")
    @Column(name = "majorTypesOfEvents")
    @NotBlank(message = "\"事项类别\"为空")
    private String majorTypesOfEvents;

    @ApiModelProperty(value="服务对象")
    @Column(name = "serviceObject")
    @NotBlank(message = "\"服务对象\"为空")
    private String serviceObject;

    @ApiModelProperty(value="行使层级")
    @Column(name = "exerciseHierarchy")
    @NotBlank(message = "\"行使层级\"为空")
    private String exerciseHierarchy;

    @ApiModelProperty(value="办理形式")
    @Column(name = "handlingForm")
    @NotBlank(message = "\"办理形式\"为空")
    private String handlingForm;

    @ApiModelProperty(value="创建时间")
    @Column(name = "createTime")
    private Date createTime;

    @ApiModelProperty(value="更新时间")
    @Column(name = "modifiedTime")
    private Date modifiedTime;

    @ApiModelProperty(value="更新人")
    @Column(name = "modifiedBy")
    private String modifiedBy;

}