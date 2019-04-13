package com.runonce.model.eventversionone;
import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

/**
 *  三级四同目录
 *
 *  Created by xuxueli on '2019-01-16 12:00:32'.
 */
@Data
@Table(name="event_directory")
public class EventDirectory implements Serializable {
    private static final long serialVersionUID = 42L;

    @Id
    @Column(name = "id")
    private String id;

    @ApiModelProperty(value = "创建时间")
    @Column(name = "createTime")
    private Date createTime;

    @ApiModelProperty(value = "实施部门")
    @NotBlank(message = "\"实施部门\"不能为空")
    @Column(name = "implementatioDepartment")
    private String implementatioDepartment;

    @ApiModelProperty(value = "事项大类型")
    @NotBlank(message = "\"事项大类型\"不能为空")
    @Column(name = "bigTypesOfEvents")
    private String bigTypesOfEvents;

    @ApiModelProperty(value = "事项小类型")
    @NotBlank(message = "\"事项小类型\"不能为空")
    @Column(name = "itemSubtype")
    private String itemSubtype;

    @ApiModelProperty(value = "通用名称")
    @NotBlank(message = "\"通用名称\"不能为空")
    @Column(name = "genericName")
    private String genericName;

    @ApiModelProperty(value = "省级序号")
    @Column(name = "provincialsSerialNumber")
    private Integer provincialsSerialNumber;

    @ApiModelProperty(value = "省级名称")
    @Column(name = "provincialName")
    private String provincialName;

    @ApiModelProperty(value = "市级序号")
    @Column(name = "municipalSerialNumber")
    private Integer municipalSerialNumber;

    @ApiModelProperty(value = "市级名称")
    @Column(name = "municipalName")
    private String municipalName;

    @ApiModelProperty(value = "县级序号")
    @Column(name = "countySerialnNmber")
    private Integer countySerialnNmber;

    @ApiModelProperty(value = "县级名称")
    @Column(name = "nameOfCounty")
    private String nameOfCounty;

    @ApiModelProperty(value = "实施依据")
    @NotBlank(message = "\"实施依据\"不能为空")
    @Column(name = "establishmentBasis")
    private String establishmentBasis;

    @ApiModelProperty(value = "备注")
    @Column(name = "remarks")
    private String remarks;

    @ApiModelProperty(value = "是否区县独有")
    @NotNull(message = "\"是否区县独有\"不能为空")
    @Column(name = "isPrivate")
    private Boolean isPrivate;

    @Transient
    private String name;
}