package com.runonce.model.eventversionone;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.runonce.model.base.ServiceObject;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.*;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 *
 *  办事指南(审核转报类)
 *  Created by xuxueli on '2018-12-10 12:17:30'.
 */
@Data
@Table(name="business_guide_audit_and_forwarding_category_one")
public class BusinessGuideAuditAndForwardingCategoryOne implements Serializable {
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
     * 用户名
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
    private String itemSmallType;

    /**
     * 事项目录（事项名称）
     */
    @ApiModelProperty(value="事项目录（事项名称）")
    @Column(name = "itemCatalogue")
    @NotBlank(message = "\"事项目录（事项名称）\" 为空")
    private String itemCatalogue;

    /**
     * 初审机关
     */
    @ApiModelProperty(value="初审机关")
    @Column(name = "firstInstanceOrgan")
    private String firstInstanceOrgan;

    /**
     * 省级初审机关名称
     */
    @ApiModelProperty(value="省级初审机关名称")
    @Column(name = "nameOfProvincialFirstInspectionAuthority")
    private String nameOfProvincialFirstInspectionAuthority;

    /**
     * 事项名称（办理项）
     */
    @ApiModelProperty(value="事项名称（办理项）")
    @Column(name = "itemName")
    @NotBlank(message = "\"事项名称（办理项）\" 为空")
    private String itemName;

    /**
     * 市级初审机关名称
     */
    @ApiModelProperty(value="市级初审机关名称")
    @Column(name = "nameOfMunicipalFirstInspectionAuthority")
    private String nameOfMunicipalFirstInspectionAuthority;

    /**
     * 县级初审机关名称
     */
    @ApiModelProperty(value="县级初审机关名称")
    @Column(name = "nameOfPreliminaryInspectionOrgansAtCountyLevel")
    private String nameOfPreliminaryInspectionOrgansAtCountyLevel;

    /**
     * 终审机关
     */
    @ApiModelProperty(value="终审机关")
    @Column(name = "courtOfFinalAppeal")
    private String courtOfFinalAppeal;

    /**
     * 中央终审机关名称
     */
    @ApiModelProperty(value="中央终审机关名称")
    @Column(name = "nameOfCentralFinalAppealAuthority")
    private String nameOfCentralFinalAppealAuthority;

    /**
     * 省级终审机关名称
     */
    @ApiModelProperty(value="省级终审机关名称")
    @Column(name = "nameOfProvincialFinalAppealAuthority")
    private String nameOfProvincialFinalAppealAuthority;

    /**
     * 服务对象
     */
    @ApiModelProperty(value="服务对象")
//    @Column(name = "serviceObject")
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
     * 市级终审机关名称
     */
    @ApiModelProperty(value="市级终审机关名称")
    @Column(name = "nameOfMunicipalFinalAppealAuthority")
    private String nameOfMunicipalFinalAppealAuthority;

    /**
     * 转报审批机关
     */
    @ApiModelProperty(value="转报审批机关")
    @Column(name = "reportingAndExaminationAndApprovalAuthority")
    private String reportingAndExaminationAndApprovalAuthority;

    /**
     * 县级转报审批机关名称
     */
    @ApiModelProperty(value="县级转报审批机关名称")
    @Column(name = "nameOfCountyLevelTransferExaminationAndApprovalAuthority")
    private String nameOfCountyLevelTransferExaminationAndApprovalAuthority;

    /**
     * 市级转报审批机关名称
     */
    @ApiModelProperty(value="市级转报审批机关名称")
    @Column(name = "nameOfMunicipalTransferExaminationAndApprovalAuthorities")
    private String nameOfMunicipalTransferExaminationAndApprovalAuthorities;

    /**
     * 省级转报审批机关名称
     */
    @ApiModelProperty(value="省级转报审批机关名称")
    @Column(name = "nameOfProvincialTransferExaminationAndApprovalAuthority")
    private String nameOfProvincialTransferExaminationAndApprovalAuthority;

    /**
     * 县级法定办结时限
     */
    @ApiModelProperty(value="县级法定办结时限")
    @Column(name = "statutoryCompletionTimeLimitAtCountyLevel")
    private String statutoryCompletionTimeLimitAtCountyLevel;

    /**
     * 市级法定办结时限
     */
    @ApiModelProperty(value="市级法定办结时限")
    @Column(name = "municipalStatutoryDeadlineForCompletion")
    private String municipalStatutoryDeadlineForCompletion;

    /**
     * 省级法定办结时限
     */
    @ApiModelProperty(value="省级法定办结时限")
    @Column(name = "provincialStatutoryCompletionTimeLimit")
    private String provincialStatutoryCompletionTimeLimit;

    /**
     * 县级承诺办结时限
     */
    @ApiModelProperty(value="县级承诺办结时限")
    @Column(name = "timeLimitForFulfillingCountyLevelCommitments")
    private String timeLimitForFulfillingCountyLevelCommitments;

    /**
     * 市级承诺办结时限
     */
    @ApiModelProperty(value="市级承诺办结时限")
    @Column(name = "municipalCommitmentCompletionTimeLimit")
    private String municipalCommitmentCompletionTimeLimit;

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
     * 省级承诺办结时限
     */
    @ApiModelProperty(value="省级承诺办结时限")
    @Column(name = "provincialCommitmentDeadline")
    private String provincialCommitmentDeadline;

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
    @Column(name = "numberOfAnnualReviews")
    @NotBlank(message = "\"年审或年检次数\" 为空")
    private String numberOfAnnualReviews;

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
//    @NotBlank(message = "\"收费依据\" 为空")
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
     * 办理结果
     */
    @ApiModelProperty(value="办理结果")
    @Column(name = "handlingResult")
    private String handlingResult;

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
     * 数量限制
     */
    @ApiModelProperty(value="数量限制")
    @Column(name = "quantitativeRestriction")
    @NotBlank(message = "\"数量限制\" 为空")
    private String quantitativeRestriction;

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
     * 受理（申请）条件
     */
    @ApiModelProperty(value="受理（申请）条件")
    @Column(name = "acceptanceConditions")
    @NotBlank(message = "\"受理（申请）条件\" 为空")
    private String acceptanceConditions;

    /**
     * 设定依据
     */
    @ApiModelProperty(value="设定依据")
    @Column(name = "settingBasis")
    @NotBlank(message = "\"设定依据\" 为空")
    private String settingBasis;

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
     * 监督电话
     */
    @ApiModelProperty(value="监督电话")
    @Column(name = "serviceSupportHotline")
    @NotBlank(message = "\"监督电话\" 为空")
    private String serviceSupportHotline;

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
     * 行使层级
     */
    @ApiModelProperty(value="行使层级")
    @Column(name = "exerciseHierarchy")
    @NotBlank(message = "\"行使层级\" 为空")
    private String exerciseHierarchy;

    /**
     * 状态
     */
    @Transient
    private int state;

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
     * 是否有中介服务
     */
    @ApiModelProperty(value="是否有中介服务")
    @Column(name = "intermediaryService")
    private String intermediaryService;

    /**
     * 是否支持网上支付
     */
    @ApiModelProperty(value="是否支持网上支付")
    @Column(name = "supportOnlinePayment")
    private String supportOnlinePayment;

    /**
     * 是否支持物流快递
     */
    @ApiModelProperty(value="是否支持物流快递")
    @Column(name = "isSupportLogistics")
    private String isSupportLogistics;

}