package com.runonce.httpbean.assets.resbean;

import lombok.Data;

import javax.persistence.Column;

/**
 * @author swq
 * @date 2019/2/15 0015
 * @description
 */
@Data
public class DepartmentalMattersAndEventRes {

    private String id;

    /**
     * 状态
     */
    private Integer state;

    /**
     * 部门id
     */
    private String deptId;

    /**
     * 目录名称
     */
    private String directoryName;

    /**
     * 事项名称
     */
    private String eventName;

    /**
     * 事项大类型
     */
    private String eventType;

    /**
     * 事项大类型名称
     */
    private String eventTypeName;

    /**
     * 事项小类型
     */
    private String itemSmallType;

    /**
     * 基本编码
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


}
