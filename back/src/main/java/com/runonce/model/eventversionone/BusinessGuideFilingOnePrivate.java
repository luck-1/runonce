package com.runonce.model.eventversionone;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * @author swq
 * @date 2019/1/16 0016
 * @description 办事指南(备案类)私有
 */
@Data
@Table(name="business_guide_filing_one_private")
public class BusinessGuideFilingOnePrivate {
    private static final long serialVersionUID = 42L;

    /**
     * 部门事项id
     */
    @ApiModelProperty(value="部门事项Id")
    @Id
    @Column(name = "departmentalMattersId")
    @NotBlank(message = "\"部门事项id\" 为空")
    private String departmentalMattersId;

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
     * 办件类型
     */
    @ApiModelProperty(value="办件类型")
    @Column(name = "typeOfOffice")
    @NotBlank(message = "\"办件类型\" 为空")
    private String typeOfOffice;


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
    private String appointmentProcessing;

    /**
     * 办理查询
     */
    @ApiModelProperty(value="办理查询")
    @Column(name = "enquiry")
    private String enquiry;

    /**
     * 咨询电话
     */
    @ApiModelProperty(value="咨询电话")
    @Column(name = "advisoryPhone")
    private String advisoryPhone;

    /**
     * 监督电话
     */
    @ApiModelProperty(value="监督电话")
    @Column(name = "serviceSupportHotline")
    @NotBlank(message = "\"监督电话\" 为空")
    private String serviceSupportHotline;

    /**
     * 咨询地址
     */
    @ApiModelProperty(value="咨询地址")
    @Column(name = "advisoryAddress")
    private String advisoryAddress;

    /**
     * 投诉地址
     */
    @ApiModelProperty(value="投诉地址")
    @Column(name = "complaintAddress")
    private String complaintAddress;


    /**
     * 送达人或者送达窗口
     */
    @ApiModelProperty(value="送达人或者送达窗口")
    @Column(name = "deliversUserAndWindow")
    private String deliversUserAndWindow;

    /**
     * 状态
     */
    @ApiModelProperty(value="状态")
    @Transient
    private Integer state;

    @Transient
    @ApiModelProperty(value = "版本")
    private Integer version;
}
