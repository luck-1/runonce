package com.runonce.model.http;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class DepartmentalMattersRes implements Serializable {

    @NotBlank
    private String id; //部门事项ID

    private String deptId;//部门id

    private String deptPid;// 部门父id

    private String region;//区域

    private String deptName;//部门名称

    /**
     * 区域code
     */
    private String areaCode;
    /**
     * 区域名称
     */
    private String areaName;

    private String eventId;//事项目录ID

    private Integer state;//部门事项状态


    /**
     * 审核状态
     */
    private Integer examineAndApproveState;

    private String eventName; //事项名称

    private String eventNumber;//事项编号

    private Integer eventType;//事项类型


    /**
     * 事项小类型
     */
    private String itemSmallType;

    private String eventPid;//事项PID

    private Boolean isMin; //是否是子节点

    private int eventState; //事项状态

    private Date eventCreateTime;//创建时间

    private Date eventUpdateTime;//更新时间

    private String directoryEncoding;//目录编码

    /**
     * 实施编码
     */
    private String codingImplementation;


    /**
     * 行使层级
     */
    private String exerciseHierarchy;


    /**
     * 实施(服务)对象
     */
    private String serviceObject;


    /**
     * 实施机关
     */
    private String implementingOrgan;

    private String genericName;//通用名称

    private String directoryName;//目录名称

    private String settingBasis;//设定依据

    private String remarks;//备注

    private String directorySorting;//目录排序
    /**
     * 办事指南
     */
    private Integer businessGuideState;

    /**
     * 材料分组
     */
    private Integer materialGroupingState;

    /**
     * 申请类型
     */
    private Integer applicationTypeState;

    /**
     * 资格预审
     */
    private Integer prequalificationState;

    /**
     * 现有流程
     */
    private Integer processDescriptionState;

    /**
     * 流程绘制
     */
    private Integer processMapState;
    /**
     * 办事情形
     */
    private Integer businessSituationState;

    /**
     * 表单说明
     */
    private Integer formThatState;

    /**
     * 量化呈现
     */
    private Integer presentQuantitativeState;

    /**
     * 系统对接状态
     */
    private Integer systemDockingState;

    /**
     * 审批剩余时间 （部门审批查到部门时间，审核审批查到审批时间）
     */
    private Date examineEndTime;

    private Integer page;

    private Integer size;

    /**
     * 是否显示履历按钮
     */
    private Boolean isShow;

}
