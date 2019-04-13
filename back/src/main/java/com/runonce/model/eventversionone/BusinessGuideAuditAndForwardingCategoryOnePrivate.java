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
 * @description  办事指南（审核转报类）私有
 */
@Data
@Table(name="business_guide_audit_and_forwarding_category_one_private")
public class BusinessGuideAuditAndForwardingCategoryOnePrivate {
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
     * 网上服务系统网址
     */
    @ApiModelProperty(value="网上服务系统网址")
    @Column(name = "webSiteOfOnlineServiceSystem")
    private String webSiteOfOnlineServiceSystem;


    /**
     * 是否有网上服务系统
     */
    @ApiModelProperty(value="是否有网上服务系统")
    @Column(name = "onlineServiceSystem")
    @NotBlank(message = "\"是否有网上服务系统\" 为空")
    private String onlineServiceSystem;


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
     * 事项排序
     */
    @ApiModelProperty(value="事项排序")
    @Column(name = "itemSequencing")
    private String itemSequencing;

    /**
     * 备注说明
     */
    @ApiModelProperty(value="备注说明")
    @Column(name = "remarks")
    private String remarks;

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
