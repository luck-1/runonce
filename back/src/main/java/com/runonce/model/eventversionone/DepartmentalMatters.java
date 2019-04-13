package com.runonce.model.eventversionone;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 部门事项
 * <p>
 * Created by xuxueli on '2018-12-12 16:38:30'.
 */
@Data
@Table(name = "departmental_matters")
public class DepartmentalMatters implements Serializable {
    private static final long serialVersionUID = 42L;

    /**
     * 主键id
     */
    @Id
    @Column(name = "id")
    private String id;

    /**
     * 分组id
     */
    @Column(name = "groupId")
    private String groupId;

    /**
     * 部门id
     */
    @Column(name = "deptId")
    private String deptId;

    /**
     * 映射部门ID
     */
    @Column(name = "mappingId")
    private String mappingId;

    /**
     * 事项id
     */
    @Column(name = "eventId")
    private String eventId;

    /**
     * 状态
     */
    @Column(name = "state")
    private Integer state;

    /**
     * 审核状态
     * 1.待领导确认
     * 2.部门领导拒绝
     * 3.部门领导同意
     * 4.待发起评审
     * 5.部门领导发起评审
     * 6.待评审
     * 7.评审通过
     * 8.评审拒绝
     * 9.最终通过
     */
    @Column(name = "examineAndApproveState")
    private Integer examineAndApproveState;

    /**
     * 办事指南
     */
    @Column(name = "businessGuideState")
    private Integer businessGuideState;

    /**
     * 材料分组
     */
    @Column(name = "materialGroupingState")
    private Integer materialGroupingState;

    /**
     * 申请类型
     */
    @Column(name = "applicationTypeState")
    private Integer applicationTypeState;

    /**
     * 资格预审
     */
    @Column(name = "prequalificationState")
    private Integer prequalificationState;

    /**
     * 现有流程
     */
    @Column(name = "processDescriptionState")
    private Integer processDescriptionState;

    /**
     * 流程绘制
     */
    @Column(name = "processMapState")
    private Integer processMapState;

    /**
     * 办事情形
     */
    @Column(name = "businessSituationState")
    private Integer businessSituationState;

    /**
     * 表单说明
     */
    @Column(name = "formThatState")
    private Integer formThatState;

    /**
     * 量化呈现
     */
    @Column(name = "presentQuantitativeState")
    private Integer presentQuantitativeState;

    /**
     * 系统对接状态
     */
    @Column(name = "systemDockingState")
    private Integer systemDockingState;

    /**
     * 是否可复制
     */
    @Column(name = "isReproducible")
    private Integer isReproducible;

    /**
     * 公示时间
     */
    @Column(name = "publicityTime")
    private Date publicityTime;

    /**
     * 部门审核结束时间
     */
    @Column(name = "deptProposalEndTime")
    private Date deptProposalEndTime;

    /**
     * 审核审批结束时间
     */
    @Column(name = "examineProposalEndTime")
    private Date examineProposalEndTime;

	/**
	*当前版本
	*/
    @Column(name = "changeOfResumeVersion")
    private String changeOfResumeVersion;



    /**
     * 修改用户
     */
    @Column(name = "operator")
    private String operator;



}