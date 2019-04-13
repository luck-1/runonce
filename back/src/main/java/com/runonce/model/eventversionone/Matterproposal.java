package com.runonce.model.eventversionone;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 事项建议
 * <p>
 * Created by xuxueli on '2019-01-04 14:23:27'.
 */
@Data
@Table(name = "matterproposal")
public class Matterproposal implements Serializable {
    private static final long serialVersionUID = 42L;

    /**
     * id
     */
    @Id
    @Column(name = "id")
    private String id;

    /**
     * 事项ID
     */
    @Column(name = "departmentalMattersId")
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
    @Column(name = "sheetNum")
    private Integer sheetNum;

    /**
     * 创建时间
     */
    @Column(name = "createTime")
    private Date createTime;

    /**
     * 用户
     */
    @Column(name = "userId")
    private String userId;

    /**
     * 状态(1：同意 2：拒绝 3:待审批)
     */
    @Column(name = "state")
    private Integer state;

    /**
     * 是否为审批(1：部门领导意见  2：审批意见)
     */
    @Column(name = "isApproval")
    private Integer isApproval;

    /**
     * 问题List
     */
    @Transient
    private List<MatterProposalQuestion> matterProposalQuestionList;

}