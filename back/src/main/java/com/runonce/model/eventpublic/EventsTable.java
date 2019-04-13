package com.runonce.model.eventpublic;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Created by xuxueli on '2018-12-10 15:05:36'.
 */
@Data
@Table(name = "events_table")
public class EventsTable implements Serializable {
    private static final long serialVersionUID = 42L;

    /**
     *
     */
    @Id
    @Column(name = "id")
    private String id;

    /**
     *
     */
    @Column(name = "depId")
    private String depId;

    /**
     *
     */
    @Column(name = "eventName")
    private String eventName;

    /**
     *
     */
    @Column(name = "eventNumber")
    private String eventNumber;

    /**
     *
     */
    @Column(name = "eventType")
    private Integer eventType;


    /**
     * 事项小类型
     */
    @Column(name = "itemSmallType")
    private String itemSmallType;

    /**
     *
     */
    @Column(name = "eventPid")
    private String eventPid;


    /**
     *
     */
    @Column(name = "eventState")
    private int eventState;

    /**
     *
     */
    @Column(name = "eventCreateTime")
    private Date eventCreateTime;

    /**
     *
     */
    @Column(name = "eventUpdateTime")
    private Date eventUpdateTime;

    /**
     * 基本目录编码
     */
    @Column(name = "baseDirectoryEncoding")
    private String baseDirectoryEncoding;

    /**
     * 实施清单编码
     */
    @Column(name = "codingImplementation")
    private String codingImplementation;

    /**
     * 业务办理项编码
     */
    @Column(name = "directoryEncoding")
    private String directoryEncoding;

    /**
     * 行使层级
     */
    @Column(name = "exerciseHierarchy")
    private String exerciseHierarchy;

    /**
     * 实施机关
     */
    @Column(name = "implementingOrgan")
    private String implementingOrgan;

    /**
     * 实施(服务)对象
     */
    @Column(name = "serviceObject")
    private String serviceObject;

    /**
     * 通用名称
     */
    @Column(name = "genericName")
    private String genericName;

    /**
     * 目录名称
     */
    @Column(name = "directoryName")
    private String directoryName;

    /**
     * 设定依据
     */
    @Column(name = "settingBasis")
    private String settingBasis;

    /**
     * 备注
     */
    @Column(name = "remarks")
    private String remarks;

    /**
     * 目录排序
     */
    @Column(name = "directorySorting")
    private String directorySorting;

   @Transient
    private  Boolean historyCheck;

    @Transient
    private  Boolean checked;

    @Transient
    private  String  groupId;
    @Transient
    private  String eventNameId;

    @Transient
    private Integer isReproducible;


    /**
     *
     */
    @Column(name = "isMin")
    private Boolean isMin;


    public Boolean getIsMin(){


    return this.isMin;
    }
    public void setIsMin( boolean isMin){


         this.isMin=isMin;
    }

}