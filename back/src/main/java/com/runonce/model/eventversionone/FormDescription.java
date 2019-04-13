package com.runonce.model.eventversionone;
import java.io.Serializable;

import com.sun.javafx.beans.IDProperty;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 *  表单说明
 *
 *  Created by xuxueli on '2018-12-18 13:17:37'.
 */
@Data
@Table(name="form_description")
public class FormDescription implements Serializable {
    private static final long serialVersionUID = 42L;

    @Id
    @Column(name = "id")
    @ApiModelProperty(value="表单说明ID")
    private String id;

    @Column(name = "materialId")
    @ApiModelProperty(value="材料Id")
    private String materialId;

    @Column(name = "number")
    @ApiModelProperty(value="序号")
    private int number;

    @Column(name = "formName")
    @ApiModelProperty(value="表单名称")
    private String formName;

    @Column(name = "fieldName")
    @ApiModelProperty(value="字段名称")
    private String fieldName;

    @Column(name = "fieldGroup")
    @ApiModelProperty(value="字段分组")
    private String fieldGroup;

    @Column(name = "fieldAttribute")
    @ApiModelProperty(value="字段属性")
    private String fieldAttribute;

    @Column(name = "isExistenceOfPlural")
    @ApiModelProperty(value="是否复数存在")
    private Boolean isExistenceOfPlural;

    @Column(name = "isRequired")
    @ApiModelProperty(value="是否必填")
    private Boolean isRequired;

    @Column(name = "fillInInstructions")
    @ApiModelProperty(value="填写说明")
    private String fillInInstructions;

    @Column(name = "remarks")
    @ApiModelProperty(value="备注")
    private String remarks;

    /**
     *  状态
     */
    @Transient
    private Integer state;
}