package com.runonce.model.eventversionone;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * @author swq
*  回复表
*
*  Created by xuxueli on '2019-01-24 12:15:13'.
*/
@Data
@Table(name="matter_proposal_answer")
public class MatterProposalAnswer implements Serializable {
    private static final long serialVersionUID = 42L;

    /**
    * 回复id
    */
    @Id
    @Column(name = "id")
    private String id;

    /**
    * 问题id
    */
    @Column(name = "questionId")
    private String questionId;

    /**
    * 回复者id
    */
    @Column(name = "answererId")
    private String answererId;

    /**
    * 回复内容
    */
    @Column(name = "answerContent")
    private String answerContent;

    /**
    * 回复时间
    */
    @Column(name = "answerTime")
    private Date answerTime;

    /**
    * 回复删除状态
    */
    @Column(name = "answerDeleteState")
    private Boolean answerDeleteState;

    @Transient
    private String answererUser;


}