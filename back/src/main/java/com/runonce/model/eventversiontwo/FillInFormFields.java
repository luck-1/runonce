package com.runonce.model.eventversiontwo;
import java.io.Serializable;
import lombok.Data;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
*  
*
*  Created by xuxueli on '2018-12-10 15:04:12'.
*/
@Data
@Table(name="fill_in_form_fields")
public class FillInFormFields implements Serializable {
    private static final long serialVersionUID = 42L;

    /**
    * 
    */
    @Id
    @Column(name = "id")
    private String id;

    /**
    * 事项id
    */
    @Column(name = "eventId")
    private String eventId;

    /**
    * 表单名称
    */
    @Column(name = "formName")
    private String formName;

    /**
    * 字段名称
    */
    @Column(name = "fieldName")
    private String fieldName;

    /**
    * 字段分组
    */
    @Column(name = "fieldPath")
    private String fieldPath;

    /**
    * 是否复数存在
    */
    @Column(name = "isPlural")
    private Object isPlural;

    /**
    * 是否必填
    */
    @Column(name = "isNecessary")
    private Object isNecessary;

    /**
    * 填写说明
    */
    @Column(name = "fillInInstructions")
    private String fillInInstructions;

    /**
    * 备注
    */
    @Column(name = "remarks")
    private String remarks;


}