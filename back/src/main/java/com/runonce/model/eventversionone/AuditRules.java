package com.runonce.model.eventversionone;
import java.io.Serializable;
import lombok.Data;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
*  审核细则
*
*  Created by xuxueli on '2018-12-18 12:32:51'.
*/
@Data
@Table(name="audit_rules")
public class AuditRules implements Serializable {
    private static final long serialVersionUID = 42L;



    /**
    * 
    */
    @Column(name = "materialId")
    private String materialId;
    /**
     *
     */
    @Id
    @Column(name = "id")
    private String id;
    /**
    * 
    */
    @Column(name = "type")
    private Integer type;

    /**
    * 
    */
    @Column(name = "number")
    private Integer number;

    /**
    * 
    */
    @Column(name = "content")
    private String content;


}