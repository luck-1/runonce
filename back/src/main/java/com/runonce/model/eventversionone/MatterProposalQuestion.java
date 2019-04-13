package com.runonce.model.eventversionone;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 问题表(评论)
 * <p>
 * Created by xuxueli on '2019-01-24 12:18:08'.
 */
@Data
@Table(name = "matter_proposal_question")
public class MatterProposalQuestion implements Serializable {
    private static final long serialVersionUID = 42L;

    /**
     * 问题id
     */
    @Id
    @Column(name = "id")
    private String id;

    /**
     * 审批id
     */
    @Column(name = "matterProposalId")
    private String matterProposalId;

    /**
     * 提问者id
     */
    @Column(name = "questionerId")
    private String questionerId;

    /**
     * 提问内容
     */
    @Column(name = "questionContent")
    private String questionContent;

    /**
     * 提问时间
     */
    @Column(name = "questionTime")
    private Date questionTime;

    /**
     * 问题状态(0:待梳理员回复 1:梳理员拒绝 2:沟通中 3:同意  4:梳理员待确认)
     */
    @Column(name = "questionState")
    private Integer questionState;

    /**
     * 0：未删除 1：已删除
     */
    @Column(name = "questionDeleteState")
    private Boolean questionDeleteState;

    /**
     * 问题排序
     */
    @Column(name = "questionSort")
    private Integer questionSort;


    /**
     * 回复list
     */
    @Transient
    private List<MatterProposalAnswer> matterProposalAnswerList;

    @Column(name = "propertyName")
    private String propertyName;   //字段中文名称

    @Column(name = "property")
    private String property;  //字段名称

    @Column(name = "lineNumber")
    private Integer lineNumber; //行号




    /**
     * 事项ID
     */
    @Transient
    private String departmentalMattersId;

    /**
     * sheet页签编号
     * 1:办事指南
     * 2:材料分组
     * 3:申请类型
     * 4:资格预审
     * 5:现有流程
     * 6:办理流程图
     * 7:办事情形
     * 8:表单说明
     * 9:量化呈现
     * 10:系统对接图
     */
    @Transient
    private Integer sheetNum;

    @Transient
    private Integer isApproval;


}