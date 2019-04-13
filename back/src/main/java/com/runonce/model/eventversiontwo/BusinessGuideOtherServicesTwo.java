package com.runonce.model.eventversiontwo;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
*  
*
*  Created by xuxueli on '2018-12-10 15:04:12'.
*/
@Data
@Table(name="business_guide_other_services_two")
public class BusinessGuideOtherServicesTwo implements Serializable {
    private static final long serialVersionUID = 42L;

    /**
     *
     */
    @Column(name = "id")
    @Id
    private String id;

    /**
     * 事项ID
     */
    @Column(name = "eventId")
    private String eventId;

    /**
     * 创建时间
     */
    @Column(name = "creationTime")
    private Date creationTime;

    /**
     * 更新时间
     */
    @Column(name = "updateTime")
    private Date updateTime;

    /**
     * 用户
     */
    @Column(name = "userName")
    private String userName;

    /**
     * 事项大类型
     */
    @Column(name = "majorTypesOfEvents")
    @NotBlank(message = "必填")
    private String majorTypesOfEvents;

    /**
     * 事项小类型
     */
    @Column(name = "itemSmallType")
    @NotBlank(message = "必填")
    private String itemSmallType;

    /**
     * 事项目录（事项名称）
     */
    @Column(name = "itemCatalogue")
    @NotBlank(message = "必填")
    private String itemCatalogue;

    /**
     * 基本编码
     */
    @Column(name = "basicCoding")
    @NotBlank(message = "必填")
    private String basicCoding;

    /**
     * 实施编码
     */
    @Column(name = "codingImplementation")
    @NotBlank(message = "必填")
    private String codingImplementation;

    /**
     * 事项名称（办理项）
     */
    @Column(name = "itemName")
    @NotBlank(message = "必填")
    private String itemName;

    /**
     * 实施机关
     */
    @Column(name = "implementingOrgan")
    @NotBlank(message = "必填")
    private String implementingOrgan;

    /**
     * 实施机构（科）室
     */
    @Column(name = "implementingBody")
    @NotBlank(message = "必填")
    private String implementingBody;

    /**
     * 服务对象
     */
    @Column(name = "serviceObject")
    @NotBlank(message = "必填")
    private String serviceObject;

    /**
     * 办件类型
     */
    @Column(name = "typeOfOffice")
    @NotBlank(message = "必填")
    private String typeOfOffice;

    /**
     * 时限类型
     */
    @Column(name = "timeLimitType")
    private String timeLimitType;

    /**
     * 承诺办结时限
     */
    @Column(name = "commitmenTimeLimit")
    private String commitmenTimeLimit;

    /**
     * 承诺办结时限说明
     */
    @Column(name = "statementOfCommitmentDeadline")
    private String statementOfCommitmentDeadline;

    /**
     * 是否收费
     */
    @Column(name = "charge")
    @NotBlank(message = "必填")
    private String charge;

    /**
     * 收费标准
     */
    @Column(name = "chargingStandard")
    @NotBlank(message = "必填")
    private String chargingStandard;

    /**
     * 收费依据
     */
    @Column(name = "chargingBasis")
    @NotBlank(message = "必填")
    private String chargingBasis;

    /**
     * 支付方式
     */
    @Column(name = "paymentMethod")
    private String paymentMethod;

    /**
     * 申请材料递送方式
     */
    @Column(name = "methodOfDeliveryOfApplicationMaterials")
    private String methodOfDeliveryOfApplicationMaterials;

    /**
     * 网上服务系统网址
     */
    @Column(name = "webSiteOfOnlineServiceSystem")
    private String webSiteOfOnlineServiceSystem;

    /**
     * 办理形式
     */
    @Column(name = "handlingForm")
    @NotBlank(message = "必填")
    private String handlingForm;

    /**
     * 到现场大厅（窗口）次数
     */
    @Column(name = "numberOfVisitsToTheFieldHall")
    private String numberOfVisitsToTheFieldHall;

    /**
     * 办理时间或地点类型
     */
    @Column(name = "typeOfProcessingTimeOrPlace")
    private String typeOfProcessingTimeOrPlace;

    /**
     * 办理时间
     */
    @Column(name = "processingTime")
    @NotBlank(message = "必填")
    private String processingTime;

    /**
     * 办理地点
     */
    @Column(name = "placeOfProcessing")
    @NotBlank(message = "必填")
    private String placeOfProcessing;

    /**
     * 办理时间或地点附件
     */
    @Column(name = "addendumToProcessingTimeOrPlace")
    private String addendumToProcessingTimeOrPlace;

    /**
     * 办理窗口
     */
    @Column(name = "processingWindow")
    private String processingWindow;

    /**
     * 是否有中介服务
     */
    @Column(name = "intermediaryService")
    @NotBlank(message = "必填")
    private String intermediaryService;

    /**
     * 办理查询
     */
    @Column(name = "enquiry")
    private String enquiry;

    /**
     * 常见问题
     */
    @Column(name = "commonProblem")
    @NotBlank(message = "必填")
    private String commonProblem;

    /**
     * 受理（申请）条件
     */
    @Column(name = "acceptingConditions")
    @NotBlank(message = "必填")
    private String acceptingConditions;

    /**
     * 办理依据
     */
    @Column(name = "handlingBasis")
    private String handlingBasis;

    /**
     * 是否有服务限制
     */
    @Column(name = "serviceRestrictions")
    private String serviceRestrictions;

    /**
     * 时间(服务限制)
     */
    @Column(name = "serviceTimeConstraint")
    private String serviceTimeConstraint;

    /**
     * 频次(服务限制)
     */
    @Column(name = "serviceFrequencyRestriction")
    private String serviceFrequencyRestriction;

    /**
     * 备注说明
     */
    @Column(name = "remarks")
    private String remarks;

    /**
     * 其他(服务限制)
     */
    @Column(name = "otherServiceRestrictions")
    private String otherServiceRestrictions;

    /**
     * 咨询途径
     */
    @Column(name = "consultationApproach")
    private String consultationApproach;

    /**
     * 咨询地址
     */
    @Column(name = "advisoryAddress")
    private String advisoryAddress;

    /**
     * 监督电话
     */
    @Column(name = "serviceSupportHotline")
    @NotBlank(message = "必填")
    private String serviceSupportHotline;

    /**
     * 投诉途径
     */
    @Column(name = "complaintChannels")
    private String complaintChannels;

    /**
     * 投诉地址
     */
    @Column(name = "complaintAddress")
    private String complaintAddress;

    /**
     * 事项排序
     */
    @Column(name = "itemSequencing")
    private String itemSequencing;

    /**
     * 是否有申请材料
     */
    @Column(name = "applicationMaterials")
    @NotBlank(message = "必填")
    private String applicationMaterials;

    /**
     * 材料提交说明
     */
    @Column(name = "materialSubmissionStatement")
    private String materialSubmissionStatement;

    /**
     * 结果类型
     */
    @Column(name = "resultType")
    private String resultType;

    /**
     * 结果名称
     */
    @Column(name = "resultName")
    @NotBlank(message = "必填")
    private String resultName;

    /**
     * 是否有结果样本
     */
    @Column(name = "havaResultSample")
    private String havaResultSample;

    /**
     * 结果样本
     */
    @Column(name = "resultSample")
    private String resultSample;

    /**
     * 行使层级
     */
    @Column(name = "exerciseHierarchy")
    @NotBlank(message = "必填")
    private String exerciseHierarchy;

    /**
     *
     */
    @Transient
    @Column(name = "primary")
    private Object primary;


}