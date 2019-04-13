package com.runonce.httpbean.assets.reqbean;

import lombok.Data;

import javax.persistence.Column;

import java.util.Date;

@Data
public class JointSectoralMatters {
    /**
     *
     */
    private String id;

    /**
     *
     */
    private String depId;

    /**
     *
     */
    private String eventName;

    /**
     *
     */
    @Column(name = "eventNumber")
    private String eventNumber;

    /**
     *
     */

    private Integer eventType;


    /**
     * 事项小类型
     */

    private String itemSmallType;

    /**
     *
     */

    private String eventPid;

    /**
     *
     */

    private Boolean isMin;

    /**
     *
     */

    private int eventState;

    /**
     *
     */

    private Date eventCreateTime;

    /**
     *
     */

    private Date eventUpdateTime;

    /**
     * 目录编码
     */

    private String directoryEncoding;

    /**
     * 行使层级
     */

    private String exerciseHierarchy;

    /**
     * 实施编码
     */

    private String codingImplementation;

    /**
     * 实施机关
     */

    private String implementingOrgan;

    /**
     * 实施(服务)对象
     */

    private String serviceObject;

    /**
     * 通用名称
     */

    private String genericName;

    /**
     * 目录名称
     */

    private String directoryName;

    /**
     * 设定依据
     */

    private String settingBasis;

    /**
     * 备注
     */

    private String remarks;

    /**
     * 目录排序
     */

    private String directorySorting;

    private  String  groupId;

    private String DepartmentalMattersId;

    public Boolean getIsMin(){


        return this.isMin;
    }
    public void setIsMin( boolean isMin){


        this.isMin=isMin;
    }


}
