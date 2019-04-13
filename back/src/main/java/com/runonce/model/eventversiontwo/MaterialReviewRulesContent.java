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
@Table(name="material_review_rules_content")
public class MaterialReviewRulesContent implements Serializable {
    private static final long serialVersionUID = 42L;

    /**
    * 
    */
    @Id
    @Column(name = "id")
    private String id;

    /**
    * 受理人员审查要点
    */
    @Column(name = "receivingPersonnelPoints")
    private String receivingPersonnelPoints;

    /**
    * 审批人员审查要点
    */
    @Column(name = "approverPoints")
    private String approverPoints;

    /**
    * 审查细则表ID
    */
    @Column(name = "rulesId")
    private String rulesId;


}