package com.runonce.model.eventversionone;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
*  审批记录
*
*  Created by xuxueli on '2019-01-22 09:46:27'.
*/
@Data
@Table(name="approvalrecord")
public class Approvalrecord implements Serializable {
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
    private String dmId;

    /**
    * 
    */
    @Column(name = "operation")
    private String operation;

    /**
    * 
    */
    @Column(name = "reason")
    private String reason;

    /**
    * 
    */
    @Column(name = "createTime")
    private Date createTime;

    /**
    * 
    */
    @Column(name = "who")
    private String who;


}