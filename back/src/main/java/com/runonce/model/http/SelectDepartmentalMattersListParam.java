package com.runonce.model.http;

import lombok.Data;

import java.util.List;

/**
 * @Author: zhaolei
 * @Descriptions: 根据登录用户以及条件分页查询事项参数
 * @Date: create at 2018/12/13 0013 下午 8:35
 */
@Data
public class SelectDepartmentalMattersListParam {

    private String deptId;//部门id

    private String eventName; //事项名称

    private Integer eventType;//事项类型

    private List<String> deptIdList;


    /**
     * 状态
     */
    private Integer state;

    /**
     * 审批状态
     */
    private List<Integer> examineAndApproveStateList;

    private Boolean showTree;

    private Integer page;

    private Integer size;

    private String dmId;
}
