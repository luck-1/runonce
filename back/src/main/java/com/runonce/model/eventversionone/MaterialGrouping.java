package com.runonce.model.eventversionone;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

/**
 * Created by xuxueli on '2018-12-10 12:17:30'.
 */
@Data
@Table(name = "material_grouping")
public class MaterialGrouping implements Serializable {
    private static final long serialVersionUID = 42L;

    /**
     * 材料id
     */
    @Id
    @Column(name = "id")
    private String id;

    /**
     * 事项id
     */
    @NotNull
    @NotEmpty
    @Column(name = "eventId")
    private String eventId;

    /**
     * 材料序号
     */
    @NotNull
    @NotEmpty
    @Column(name = "orderNum")
    private String orderNum;

    /**
     * 材料类别
     */
    @Column(name = "type")
    private String type;

    /**
     * 材料编号
     */
    @Column(name = "number")
    private String number;

    /**
     * 输入或输出
     */
    @Column(name = "io")
    private String io;

    /**
     * 名称
     */
    @ApiModelProperty(value = "材料名称")
    @Column(name = "name")
    private String name;

    /**
     * 原件提供
     */
    @Column(name = "originalScriptProvide")
    private Boolean originalScriptProvide;

    /**
     * 复印件提供
     */
    @Column(name = "copyProvide")
    private Boolean copyProvide;

    /**
     * 电子件提供
     */
    @Column(name = "electronicProvide")
    private Boolean electronicProvide;

    /**
     * 审核输出原件
     */
    @Column(name = "originalScriptOutput")
    private Boolean originalScriptOutput;

    /**
     * 审核输出电子件
     */
    @Column(name = "electronicOutput")
    private Boolean electronicOutput;

    /**
     * 材料要求份数
     */
    @Column(name = "count")
    private int count;


    /**
     * 是否属于可容缺的材料
     */
    @Column(name = "isItIndispensable")
    private Boolean isItIndispensable;

    /**
     * 材料提供方式
     */
    @Column(name = "provideWay")
    private String provideWay;

    /**
     * 材料来源(职能部门/自制)
     */
    @Column(name = "source")
    private String source;

    /**
     * 职能部门名称
     */
    @Column(name = "departmrntName")
    private String departmrntName;

    /**
     * 职能部门系统名称
     */
    @Column(name = "departmrntSystemName")
    private String departmrntSystemName;

    /**
     * 职能部门系统网址
     */
    @Column(name = "departmrntSystemUrl")
    private String departmrntSystemUrl;

    /**
     * 备注
     */
    @Column(name = "remarks")
    private String remarks;

    /**
     * 样表
     */
    @Column(name = "sampleTable")
    private String sampleTable;

    /**
     * 空表
     */
    @Column(name = "emptyTable")
    private String emptyTable;

    /**
     *
     */
    @Transient
    @Column(name = "primary")
    private Object primary;

    /**
     * 是否预受理
     */
    @Column(name = "preAcceptance")
    private String preAcceptance;
    /**
     * 可缺少的
     */
    @ApiModelProperty(value = "可通过数据共享进行减免（是/否）")
    @Column(name = "isStreamlining")
    private Boolean isStreamlining;
    /**
     * 如果可精简，则数据所需归集单位
     */
    @ApiModelProperty(value = "数据所需归集单位")
    @Column(name = "belongUnit")
    private String belongUnit;
    /**
     * 是否可通过政策依据进行取消/合并
     */
    @ApiModelProperty(value = "可通过政策依据进行取消/合并（是/否）")
    @Column(name = "cancelMerge")
    private String cancelMerge;
    /**
     * 受理人员审查
     */

    @ApiModelProperty(value = "受理人员审查")
    @Column(name = "receivePersonReview")
    private Boolean receivePersonReview;
    /**
     * 受理人员审查方式
     */
    @ApiModelProperty(value = "受理人员审查方式")
    @Column(name = "receivePersonReviewWay")
    private String receivePersonReviewWay;
    /**
     * 审批人员审查
     */
    @ApiModelProperty(value = "审批人员审查")
    @Column(name = "approverReview")
    private Boolean approverReview;
    /**
     * 审批人员审查方式
     */
    @ApiModelProperty(value = "审批人员审查方式")
    @Column(name = "approverReviewWay")
    private String approverReviewWay;
}