package com.runonce.httpbean.assets.reqbean;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


import java.io.Serializable;

/**
 * @author swq
 * @date 2018/12/12 0012
 * @description
 */
@Data
public class UserReq  implements Serializable {

    @ApiModelProperty(value = "主键id")
    private String Id;

    @ApiModelProperty(value = "用户名")
    private String userName;

    @ApiModelProperty(value = "昵称")
    private String nickName;

    @ApiModelProperty(value = "手机")
    private String mobile;

    @ApiModelProperty(value = "邮件")
    private String email;

    @ApiModelProperty(value = "省市县地址")
    private String address;

    @ApiModelProperty(value = "街道地址")
    private String street;

    @ApiModelProperty(value = "0女 1男 2保密")
    private Integer sex;

    @ApiModelProperty(value = "用户类型 0普通用户 1管理员")
    private Integer type;

    @ApiModelProperty(value = "状态 默认0正常 -1拉黑")
    private Integer status;

    @ApiModelProperty(value = "描述/详情/备注")
    private String description;

    @ApiModelProperty(value = "所属部门id")
    private String departmentId;
    /**
     * 当前页
     */
    private int currentPage;

    /**
     * 分页大小
     */
    private int pageSize;


}
