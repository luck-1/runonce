package com.runonce.model.eventversionone;
import java.io.Serializable;
import lombok.Data;
import lombok.NonNull;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
*  变更履历
*
*  Created by xuxueli on '2019-02-19 10:28:47'.
*/
@Data
@Table(name="revisionhistory")
public class RevisionHistory implements Serializable {
    private static final long serialVersionUID = 42L;

    /**
    * 
    */
    @Id
    @Column(name = "id")
    private String id;

    /**
    * 
    */

    @Column(name = "dmId")
    private String dmId;//dmId

    /**
    * 
    */
    @Column(name = "versionNumber")
    private String versionNumber;

    /**
    * 
    */
    @Column(name = "dateOfChange")
    private Object dateOfChange;

    /**
    * 1已发布、2变更申请中、3变更中、4驳回
    */

    @Column(name = "state")
    private int state;//变更状态

    /**
    * 
    */
    @Column(name = "who")
    private String who;

    /**
    * 
    */

    @Column(name = "changeContent")
    private String changeContent;//变更内容

    @Transient
    private String reasons;
}