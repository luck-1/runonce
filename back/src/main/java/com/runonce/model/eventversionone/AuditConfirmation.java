package com.runonce.model.eventversionone;
import java.io.Serializable;
import lombok.Data;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
*  
*
*  Created by xuxueli on '2018-12-10 12:17:29'.
*/
@Data
@Table(name="AuditConfirmation")
public class AuditConfirmation implements Serializable {
    private static final long serialVersionUID = 42L;

    /**
    * id
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
    * 部门
    */
    @Column(name = "departmrnt")
    private String departmrnt;

    /**
    * 审核确认内容的表单名
    */
    @Column(name = "formName")
    private String formName;

    /**
    * 审核确认内容描述
    */
    @Column(name = "describetion")
    private String describetion;

    /**
    * 审核确认内容页数
    */
    @Column(name = "pageCount")
    private int pageCount;

    /**
    * 是否核准
    */
    @Column(name = "approval")
    private Object approval;

    /**
    * 工作人员是否确认
    */
    @Column(name = "staffConfirmation")
    private Object staffConfirmation;

    /**
    * 工作人员名字
    */
    @Column(name = "staffName")
    private String staffName;

    /**
    * 
    */
    @Column(name = "primary")
    private Object primary;


}