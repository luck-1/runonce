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
 *  办事指南(其他服务类)
 *  Created by xuxueli on '2018-12-10 12:17:30'.
 */
@Data
@Table(name="business_guide_other_services_one")
public class BusinessGuideOtherServicesOne implements Serializable {
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
     * 基本编码
     */
    @ApiModelProperty(value="基本编码")
    @Column(name = "basicCoding")
//    @NotBlank(message = "\"基本编码\" 为空")
    private String basicCoding;

    /**
     * 实施编码
     */
    @ApiModelProperty(value="实施编码")
    @Column(name = "codingImplementation")
    @NotBlank(message = "\"实施编码\" 为空")
    private String codingImplementation;

    /**
     * 事项名称（办理项）
     */
    @ApiModelProperty(value="事项名称（办理项）")
    @Column(name = "itemName")
    @NotBlank(message = "\"事项名称（办理项）\" 为空")
    private String itemName;

    /**
     * 实施机关
     */
    @ApiModelProperty(value="实施机关")
    @Column(name = "implementingOrgan")
    @NotBlank(message = "\"实施机关\" 为空")
    private String implementingOrgan;

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
    @Transient
    private String serviceObject;

    /**
     * 办件类型
     */
    @ApiModelProperty(value="办件类型")
    @Column(name = "typeOfOffice")
    @NotBlank(message = "\"办件类型\" 为空")
    private String typeOfOffice;

    /**
     * 时限类型
     */
    @ApiModelProperty(value="时限类型")
    @Column(name = "timeLimitType")
    private String timeLimitType;

    /**
     * 承诺办结时限
     */
    @ApiModelProperty(value="承诺办结时限")
    @Column(name = "commitmenTimeLimit")
    private String commitmenTimeLimit;

    /**
     * 承诺办结时限说明
     */
    @ApiModelProperty(value="承诺办结时限说明")
    @Column(name = "statementOfCommitmentDeadline")
    private String statementOfCommitmentDeadline;

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
//    @NotBlank(message = "\"\" 为空")
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
     * 网上服务系统网址
     */
    @ApiModelProperty(value="网上服务系统网址")
    @Column(name = "webSiteOfOnlineServiceSystem")
    private String webSiteOfOnlineServiceSystem;

    /**
     * 办理形式
     */
    @ApiModelProperty(value="办理形式")
    @Column(name = "handlingForm")
    @NotBlank(message = "\"办理形式\" 为空")
    private String handlingForm;

    /**
     * 到现场大厅（窗口）次数
     */
    @ApiModelProperty(value="到现场大厅（窗口）次数")
    @Column(name = "numberOfVisitsToTheFieldHall")
    @NotBlank(message = "\"到现场大厅（窗口）次数\" 为空")
    private String numberOfVisitsToTheFieldHall;

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
     * 是否有中介服务
     */
    @ApiModelProperty(value="是否有中介服务")
    @Column(name = "intermediaryService")
    @NotBlank(message = "\"是否有中介服务\" 为空")
    private String intermediaryService;

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
    @Column(name = "acceptingConditions")
    @NotBlank(message = "\"受理（申请）条件\" 为空")
    private String acceptingConditions;

    /**
     * 办理依据
     */
    @ApiModelProperty(value="办理依据")
    @Column(name = "handlingBasis")
    private String handlingBasis;

    /**
     * 是否有服务限制
     */
    @ApiModelProperty(value="是否有服务限制")
    @Column(name = "serviceRestrictions")
    private String serviceRestrictions;

    /**
     * 时间(服务限制)
     */
    @ApiModelProperty(value="时间(服务限制)")
    @Column(name = "serviceTimeConstraint")
    private String serviceTimeConstraint;

    /**
     * 频次(服务限制)
     */
    @ApiModelProperty(value="频次(服务限制)")
    @Column(name = "serviceFrequencyRestriction")
    private String serviceFrequencyRestriction;

    /**
     * 备注说明
     */
    @ApiModelProperty(value="备注说明")
    @Column(name = "remarks")
    private String remarks;

    /**
     * 其他(服务限制)
     */
    @ApiModelProperty(value="其他(服务限制)")
    @Column(name = "otherServiceRestrictions")
    private String otherServiceRestrictions;

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
     * 是否有申请材料
     */
    @ApiModelProperty(value="是否有申请材料")
    @Column(name = "applicationMaterials")
    @NotBlank(message = "\"是否有申请材料\" 为空")
    private String applicationMaterials;

    /**
     * 材料提交说明
     */
    @ApiModelProperty(value="材料提交说明")
    @Column(name = "materialSubmissionStatement")
    private String materialSubmissionStatement;

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
     * 是否有结果样本
     */
    @ApiModelProperty(value="是否有结果样本")
    @Column(name = "havaResultSample")
    private String havaResultSample;

    /**
     * 结果样本
     */
    @ApiModelProperty(value="结果样本")
    @Column(name = "resultSample")
    private String resultSample;

    /**
     * 行使层级
     */
    @ApiModelProperty(value="行使层级")
    @Column(name = "exerciseHierarchy")
//    @NotBlank(message = "\"行使层级\" 为空")
    private String exerciseHierarchy;

    /**
     * 状态
     */
    @Transient
    private int state;

    /**
     * 服务主题
     */
    @ApiModelProperty(value="服务主题")
    @Column(name = "serviceTopic")
    private String serviceTopic;

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