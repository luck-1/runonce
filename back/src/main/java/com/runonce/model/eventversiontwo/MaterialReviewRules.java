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
@Table(name="material_review_rules")
public class MaterialReviewRules implements Serializable {
    private static final long serialVersionUID = 42L;

    /**
    * 
    */
    @Id
    @Column(name = "id")
    private String id;

    /**
    * 事件id
    */
    @Column(name = "eventId")
    private String eventId;

    /**
    * 材料名称
    */
    @Column(name = "materiaName")
    private String materiaName;

    /**
    * 受理人员审查
    */
    @Column(name = "fontReceivingPersonnel")
    private Object fontReceivingPersonnel;

    /**
    * 受理人员审查方式
    */
    @Column(name = "reviewMethodOfReceivingPersonne")
    private String reviewMethodOfReceivingPersonne;

    /**
    * 审批人员审查
    */
    @Column(name = "backReceivingPersonnel")
    private Object backReceivingPersonnel;

    /**
    * 审批人员审查方式
    */
    @Column(name = "approvalPersonnelReviewMethod")
    private String approvalPersonnelReviewMethod;

    /**
    * 材料分组V2.0ID
    */
    @Column(name = "materialGroupingTwoId")
    private String materialGroupingTwoId;


}