package com.runonce.model.eventversionone;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.runonce.model.base.ServiceObject;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
*  
*  办事指南(备案类)
*  Created by xuxueli on '2018-12-10 12:17:30'.
*/
@Data
@Table(name="business_guide_filing_one")
public class BusinessGuideFilingOne implements Serializable {
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
     * 办事指南类型
     */
    @Column(name = "type")
    private String type;

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
     * 事项负责人用户名
     */
    @Column(name = "userName")
    private String userName;

    /**
     * 事项大类型
     */
    @ApiModelProperty(value="事项大类型")
    @Column(name = "majorTypesOfEvents")
    @NotBlank(message = "\"事项大类型\" 为空")
    private String majorTypesOfEvents;

    /**
     * 事项小类型
     */
    @ApiModelProperty(value="事项小类型")
    @Column(name = "itemSmallType")
    @NotBlank(message = "\"事项小类型\" 为空")
    private String ItemSmallType;

    /**
     * 是否自贸区事项
     */
    @ApiModelProperty(value="是否自贸区事项")
    @Column(name = "ftaMatters")
    @NotBlank(message = "\"是否自贸区事项\" 为空")
    private String ftaMatters;

    /**
     * 事项目录（事项名称）
     */
    @ApiModelProperty(value="事项目录（事项名称）")
    @Column(name = "transactionDirectory")
    @NotBlank(message = "\"事项目录（事项名称）\" 为空")
    private String transactionDirectory;

    /**
     * 基本编码
     */
    @ApiModelProperty(value="基本编码")
    @Column(name = "basicCoding")
    private String basicCoding;

    /**
     * 实施编码
     */
    @ApiModelProperty(value="实施编码")
    @Column(name = "codingImplementation")
    private String codingImplementation;

    /**
     * 事项名称（办理项）
     */
    @ApiModelProperty(value="事项名称（办理项）")
    @Column(name = "itemName")
    @NotBlank(message = "\"事项名称（办理项）\" 为空")
    private String itemName;

    /**
     * 权力来源
     */
    @ApiModelProperty(value="权力来源")
    @Column(name = "sourceOfPower")
    @NotBlank(message = "\"权力来源\" 为空")
    private String sourceOfPower;

    /**
     * 实施机关
     */
    @ApiModelProperty(value="实施机关")
    @Column(name = "implementingOrgan")
    @NotBlank(message = "\"实施机关\" 为空")
    private String implementingOrgan;

    /**
     * 实施主体性质
     */
    @ApiModelProperty(value="实施主体性质")
    @Column(name = "natureOfImplementingSubject")
    @NotBlank(message = "\"实施主体性质\" 为空")
    private String natureOfImplementingSubject;

    /**
     * 实施机构（科）室
     */
    @ApiModelProperty(value="实施机构（科）室")
    @Column(name = "implementingBody")
    @NotBlank(message = "\"实施机构（科）室\" 为空")
    private String implementingBody;

    /**
     * 服务对象
     */
    @ApiModelProperty(value="服务对象")
    @NotEmpty(message = "\"服务对象\" 为空")
    @Transient
    private String serviceObject;

    /**
     * 服务主题
     */
    @ApiModelProperty(value="服务主题")
    @Column(name = "serviceTopic")
//    @NotBlank(message = "\"服务主题\" 为空")
    private String serviceTopic;

    /**
     * 权限划分
     */
    @ApiModelProperty(value="权限划分")
    @Column(name = "permissionDivision")
    @NotBlank(message = "\"权限划分\" 为空")
    private String permissionDivision;

    /**
     * 详细权限划分
     */
    @ApiModelProperty(value="详细权限划分")
    @Column(name = "detailedPermissionDivision")
    private String detailedPermissionDivision;

    /**
     * 办件类型
     */
    @ApiModelProperty(value="办件类型")
    @Column(name = "typeOfOffice")
    @NotBlank(message = "\"办件类型\" 为空")
    private String typeOfOffice;

    /**
     * 行使层级
     */
    @ApiModelProperty(value="行使层级")
    @Column(name = "exerciseHierarchy")
    @NotBlank(message = "\"行使层级\" 为空")
    private String exerciseHierarchy;

    /**
     * 时限类型
     */
    @ApiModelProperty(value="时限类型")
    @Column(name = "timeLimitType")
    private String timeLimitType;

    /**
     * 法定办结时限
     */
    @ApiModelProperty(value="法定办结时限")
    @Column(name = "statutoryDeadlines")
    private String statutoryDeadlines;

    /**
     * 法定办结时限说明
     */
    @ApiModelProperty(value="法定办结时限说明")
    @Column(name = "statutoryTimeLimitForCompletion")
    private String statutoryTimeLimitForCompletion;

    /**
     * 承诺办结时限
     */
    @ApiModelProperty(value="承诺办结时限")
    @Column(name = "commitmentTimeLimit")
    private String commitmentTimeLimit;

    /**
     * 承诺办结时限说明
     */
    @ApiModelProperty(value="承诺办结时限说明")
    @Column(name = "statementOfCommitmentDeadline")
    private String statementOfCommitmentDeadline;

    /**
     * 联办机构
     */
    @ApiModelProperty(value="联办机构")
    @Column(name = "jointAgency")
    private String jointAgency;

    /**
     * 联办机构适用情形
     */
    @ApiModelProperty(value="联办机构适用情形")
    @Column(name = "applicationOfJointOrganizations")
    private String applicationOfJointOrganizations;

    /**
     * 是否收费
     */
    @ApiModelProperty(value="是否收费")
    @Column(name = "charge")
    @NotBlank(message = "\"是否收费\" 为空")
    private String charge;

    /**
     * 收费标准
     */
    @ApiModelProperty(value="收费标准")
    @Column(name = "chargingStandard")
//    @NotBlank(message = "\"收费标准\" 为空")
    private String chargingStandard;

    /**
     * 收费依据
     */
    @ApiModelProperty(value="收费依据")
    @Column(name = "chargingBasis")
//    @NotBlank(message = "\"\" 为空")
    private String chargingBasis;

    /**
     * 支付方式
     */
    @ApiModelProperty(value="支付方式")
    @Column(name = "paymentMethod")
    private String paymentMethod;

    /**
     * 申请材料递送方式
     */
    @ApiModelProperty(value="申请材料递送方式")
    @Column(name = "methodOfDeliveryOfApplicationMaterials")
    private String methodOfDeliveryOfApplicationMaterials;

    /**
     * 是否有网上服务系统
     */
    @ApiModelProperty(value="是否有网上服务系统")
    @Column(name = "onlineServiceSystem")
    @NotBlank(message = "\"是否有网上服务系统\" 为空")
    private String onlineServiceSystem;

    /**
     * 网上服务系统网址
     */
    @ApiModelProperty(value="网上服务系统网址")
    @Column(name = "webSiteOfOnlineServiceSystem")
    private String webSiteOfOnlineServiceSystem;

    /**
     * 企业投资项目申报地址
     */
    @ApiModelProperty(value="企业投资项目申报地址")
    @Column(name = "declarationAddressOfEnterpriseInvestmentProjects")
    private String declarationAddressOfEnterpriseInvestmentProjects;

    /**
     * 委托代办
     */
    @ApiModelProperty(value="委托代办")
    @Column(name = "concierge")
    @NotBlank(message = "\"委托代办\" 为空")
    private String concierge;

    /**
     * 通办范围
     */
    @ApiModelProperty(value="通办范围")
    @Column(name = "scopeOfOperation")
    private String scopeOfOperation;

    /**
     * 办理形式
     */
    @ApiModelProperty(value="办理形式")
    @Column(name = "handlingForm")
    @NotBlank(message = "\"办理形式\" 为空")
    private String handlingForm;

    /**
     * 网上申请形式
     */
    @ApiModelProperty(value="网上申请形式")
    @Column(name = "onlineApplicationForm")
    @NotBlank(message = "\"网上申请形式\" 为空")
    private String onlineApplicationForm;

    /**
     * 到现场大厅（窗口）次数
     */
    @ApiModelProperty(value="到现场大厅（窗口）次数")
    @Column(name = "numberOfVisitsToTheFieldHall")
    @NotBlank(message = "\"到现场大厅（窗口）次数\" 为空")
    private String numberOfVisitsToTheFieldHall;

    /**
     * 事项星级
     */
    @ApiModelProperty(value="事项星级")
    @Column(name = "eventStar")
    private String eventStar;

    /**
     * 办理时间或地点类型
     */
    @ApiModelProperty(value="办理时间或地点类型")
    @Column(name = "typeOfProcessingTimeOrPlace")
//    @NotBlank(message = "\"办理时间或地点类型\" 为空")
    private String typeOfProcessingTimeOrPlace;

    /**
     * 办理时间
     */
    @ApiModelProperty(value="办理时间")
    @Column(name = "processingTime")
    @NotBlank(message = "\"办理时间\" 为空")
    private String processingTime;

    /**
     * 办理地点
     */
    @ApiModelProperty(value="办理地点")
    @Column(name = "placeOfProcessing")
    @NotBlank(message = "\"办理地点\" 为空")
    private String placeOfProcessing;

    /**
     * 办理时间或地点附件
     */
    @ApiModelProperty(value="办理时间或地点附件")
    @Column(name = "addendumToProcessingTimeOrPlace")
    private String addendumToProcessingTimeOrPlace;

    /**
     * 办理窗口
     */
    @ApiModelProperty(value="办理窗口")
    @Column(name = "processingWindow")
    private String processingWindow;

    /**
     * 预约办理
     */
    @ApiModelProperty(value="预约办理")
    @Column(name = "appointmentProcessing")
//    @NotBlank(message = "\"预约办理\" 为空")////////////////////////////////
    private String appointmentProcessing;

    /**
     * 数量限制
     */
    @ApiModelProperty(value="数量限制")
    @Column(name = "quantitativeRestriction")
    @NotBlank(message = "\"数量限制\" 为空")
    private String quantitativeRestriction;

    /**
     * 是否有中介服务
     */
    @ApiModelProperty(value="是否有中介服务")
    @Column(name = "intermediaryService")
    @NotBlank(message = "\"是否有中介服务\" 为空")
    private String intermediaryService;

    /**
     * 批前公示
     */
    @ApiModelProperty(value="批前公示")
    @Column(name = "prePublicationPublicity")
    private String prePublicationPublicity;

    /**
     * 办理查询
     */
    @ApiModelProperty(value="办理查询")
    @Column(name = "enquiry")
    private String enquiry;

    /**
     * 常见问题
     */
    @ApiModelProperty(value="常见问题")
    @Column(name = "commonProblem")
    @NotBlank(message = "\"常见问题\" 为空")
    private String commonProblem;

    /**
     * 申请主体的权利和义务
     */
    @ApiModelProperty(value="申请主体的权利和义务")
    @Column(name = "theRightsAndObligationsOfTheApplicant")
    private String theRightsAndObligationsOfTheApplicant;

    /**
     * 受理（申请）条件
     */
    @ApiModelProperty(value="受理（申请）条件")
    @Column(name = "acceptingConditions")
    @NotBlank(message = "\"受理（申请）条件\" 为空")
    private String acceptingConditions;

    /**
     * 设定依据
     */
    @ApiModelProperty(value="设定依据")
    @Column(name = "settingBasis")
    @NotBlank(message = "\"设定依据\" 为空")
    private String settingBasis;

    /**
     * 办理依据
     */
    @ApiModelProperty(value="办理依据")
    @Column(name = "handlingBasis")
    private String handlingBasis;

    /**
     * 行使内容
     */
    @ApiModelProperty(value="行使内容")
    @Column(name = "exerciseContent")
    @NotBlank(message = "\"行使内容\" 为空")
    private String exerciseContent;

    /**
     * 行政复议行政诉讼
     */
    @ApiModelProperty(value="行政复议行政诉讼")
    @NotBlank(message = "\"行政复议行政诉讼\" 为空")
    @Column(name = "administrativeReconsiderationAdministrativeLitigation")
    private String administrativeReconsiderationAdministrativeLitigation;

    /**
     * 备注说明
     */
    @ApiModelProperty(value="备注说明")
    @Column(name = "remarks")
    private String remarks;

    /**
     * 咨询途径
     */
    @ApiModelProperty(value="咨询途径")
    @Column(name = "consultationApproach")
    private String consultationApproach;

    /**
     * 咨询地址
     */
    @ApiModelProperty(value="咨询地址")
    @Column(name = "advisoryAddress")
    private String advisoryAddress;

    /**
     * 监督电话
     */
    @ApiModelProperty(value="监督电话")
    @Column(name = "serviceSupportHotline")
    @NotBlank(message = "\"监督电话\" 为空")
    private String serviceSupportHotline;

    /**
     * 投诉途径
     */
    @ApiModelProperty(value="投诉途径")
    @Column(name = "complaintChannels")
    private String complaintChannels;

    /**
     * 投诉地址
     */
    @ApiModelProperty(value="投诉地址")
    @Column(name = "complaintAddress")
    private String complaintAddress;

    /**
     * 事项排序
     */
    @ApiModelProperty(value="事项排序")
    @Column(name = "itemSequencing")
    private String itemSequencing;

    /**
     * 材料提交说明
     */
    @ApiModelProperty(value="材料提交说明")
    @Column(name = "materialSubmissionStatement")
    @NotBlank(message = "\"材料提交说明\" 为空")
    private String materialSubmissionStatement;

    /**
     * 受理通过
     */
    @ApiModelProperty(value="受理通过")
    @Column(name = "acceptAndApprove")
    private String acceptAndApprove;

    /**
     * 时限（受理）
     */
    @ApiModelProperty(value="时限（受理）")
    @Column(name = "timeLimitForAcceptance")
    private String timeLimitForAcceptance;

    /**
     * 补正或更正
     */
    @ApiModelProperty(value="补正或更正")
    @Column(name = "correctionOrCorrection")
    private String correctionOrCorrection;

    /**
     * 时限（补正或更正）
     */
    @ApiModelProperty(value="时限（补正或更正）")
    @Column(name = "timeLimitForCorrection")
    private String timeLimitForCorrection;

    /**
     * 受理结果
     */
    @ApiModelProperty(value="受理结果")
    @Column(name = "acceptanceResult")
    @NotBlank(message = "\"受理结果\" 为空")
    private String acceptanceResult;

    /**
     * 补正或更正结果
     */
    @ApiModelProperty(value="补正或更正结果")
    @Column(name = "correctionOfResults")
    @NotBlank(message = "\"补正或更正结果\" 为空")
    private String correctionOfResults;

    /**
     * 不予受理
     */
    @ApiModelProperty(value="不予受理")
    @Column(name = "refuseToAccept")
    private String refuseToAccept;

    /**
     * 审查方式
     */
    @ApiModelProperty(value="审查方式")
    @Column(name = "modeOfExamination")
    private String modeOfExamination;

    /**
     * 审查标准
     */
    @ApiModelProperty(value="审查标准")
    @Column(name = "reviewStandard")
    private String reviewStandard;

    /**
     * 审查结果
     */
    @ApiModelProperty(value="审查结果")
    @Column(name = "examinationResult")
    private String examinationResult;

    /**
     * 审查时限
     */
    @ApiModelProperty(value="审查时限")
    @Column(name = "timeLimitRorReview")
    private String timeLimitRorReview;

    /**
     * 决定时限
     */
    @ApiModelProperty(value="决定时限")
    @Column(name = "decisionTimeLimit")
    private String decisionTimeLimit;

    /**
     * 法律依据及描述（特殊环节）
     */
    @ApiModelProperty(value="法律依据及描述（特殊环节）")
    @Column(name = "legalBasisAndDescriptionSpecial")
    private String legalBasisAndDescriptionSpecial;

    /**
     * 所需时限（特殊环节）
     */
    @ApiModelProperty(value="所需时限（特殊环节）")
    @Column(name = "requiredTimeLimitSpecial")
    private String requiredTimeLimitSpecial;

    /**
     * 是否计算在法定办结时限内（特殊环节）
     */
    @ApiModelProperty(value="是否计算在法定办结时限内（特殊环节）")
    @Column(name = "statutoryCompletionTimeLimitSpecial")
    private String statutoryCompletionTimeLimitSpecial;

    /**
     * 是否收费（特殊环节）
     */
    @ApiModelProperty(value="是否收费（特殊环节）")
    @Column(name = "chargeSpecial")
    private String chargeSpecial;

    /**
     * 收费依据及描述（特殊环节）
     */
    @ApiModelProperty(value="收费依据及描述（特殊环节）")
    @Column(name = "chargeBasisAndDescriptionSpecial")
    private String chargeBasisAndDescriptionSpecial;

    /**
     * 办理流程图
     */
    @ApiModelProperty(value="办理流程图")
    @Column(name = "processFlowChart")
//    @NotBlank(message = "\"办理流程图\" 为空")////////////////
    private String processFlowChart;

    /**
     * 办理流程说明
     */
    @ApiModelProperty(value="办理流程说明")
    @Column(name = "processDescription")
    private String processDescription;

    /**
     * 结果类型
     */
    @ApiModelProperty(value="结果类型")
    @Column(name = "resultType")
    private String resultType;

    /**
     * 结果名称
     */
    @ApiModelProperty(value="结果名称")
    @Column(name = "resultName")
    @NotBlank(message = "\"结果名称\" 为空")
    private String resultName;

    /**
     * 结果样本
     */
    @ApiModelProperty(value="结果样本")
    @Column(name = "resultSample")
//    @NotBlank(message = "\"结果样本\" 为空")/////////////////
    private String resultSample;

    /**
     * 年审或年检
     */
    @ApiModelProperty(value="年审或年检")
    @Column(name = "annualExaminationOrAnnualInspection")
    @NotBlank(message = "\"年审或年检\" 为空")
    private String annualExaminationOrAnnualInspection;

    /**
     * 年审或年检次数
     */
    @ApiModelProperty(value="年审或年检次数")
    @Column(name = "numberOfAnnualOrAnnualReviews")
    @NotBlank(message = "\"年审或年检次数\" 为空")
    private String numberOfAnnualOrAnnualReviews;

    /**
     * 决定公开
     */
    @ApiModelProperty(value="决定公开")
    @Column(name = "decidedToMakePublic")
    @NotBlank(message = "\"决定公开\" 为空")
    private String decidedToMakePublic;

    /**
     * 送达途径
     */
    @ApiModelProperty(value="送达途径")
    @Column(name = "serviceRoute")
    private String serviceRoute;


    /**
     * 状态
     */
    @Transient
    private int state;


    /**
     * 材料适用情形
     */
    @ApiModelProperty(value="材料适用情形")
    @Column(name = "materialApplication")
    private String materialApplication;

}