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
@Table(name="application_type_field_two")
public class ApplicationTypeFieldTwo implements Serializable {
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
    @Column(name = "parentId")
    private String parentId;

    /**
    * 事项id
    */
    @Column(name = "eventId")
    private String eventId;
    /**
     * 是否树叶节点
     */
    @Column(name = "leafNode")
    private Boolean leafNode;
    /**
    * 字段类型
    */
    @Column(name = "fieldType")
    private String fieldType;


}