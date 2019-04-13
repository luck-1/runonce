package com.runonce.model.eventversionone;
import java.io.Serializable;
import java.util.List;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

/**
*  
*
*  Created by xuxueli on '2018-12-10 12:17:29'.
*/
@Data
@Table(name="application_type_field")
public class ApplicationTypeField implements Serializable {
    private static final long serialVersionUID = 42L;

    /**
    * id
    */
    @Id
    @Column(name = "id")
    private String id;

    /**
    * 字段名称
    */
    @Column(name = "fieldName")
    private String fieldName;

    /**
    * 父级id
    */
    @NotNull
    @NotEmpty
    @Column(name = "parentId")
    private String parentId;

    /**
    * 事项id
    */
    @NotNull
    @NotEmpty
    @Column(name = "eventId")
    private String eventId;

    /**
    * 字段类型
    */
    @Column(name = "fieldType")
    private String fieldType;
    /**
     * 是否树叶节点
     */
    @Column(name = "leafNode")
    private Boolean leafNode;
    /**
     * 序号
     */
    @Column(name= "sortNumber")
    private Integer sortNumber;
    /**
     * 层级
     */
    @Column(name = "level")
    private Integer level;
    /**
    * 
    */
    @Transient
    @Column(name = "primary")
    private Object primary;


    @Transient
    private String levelTwoFieldId;

    private List<ApplicationTypeField> applicationTypeFields;


}