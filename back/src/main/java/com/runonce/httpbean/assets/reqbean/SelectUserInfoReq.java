package com.runonce.httpbean.assets.reqbean;

import com.runonce.model.system.BaseEntity;
import com.runonce.model.system.Permission;
import com.runonce.model.system.Role;
import com.runonce.util.constant.CommonConstant;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.List;

/**
 * @author swq
 * @date 2018/12/15 0015
 * @description
 */
@Data
public class SelectUserInfoReq extends BaseEntity {

    private static final long serialVersionUID = 42L;

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

    @ApiModelProperty(value = "密码强度")
    private String passStrength;

    @ApiModelProperty(value = "用户头像")
    private String avatar = CommonConstant.USER_DEFAULT_AVATAR;

    @ApiModelProperty(value = "用户类型 0普通用户 1管理员")
    private Integer type = CommonConstant.USER_TYPE_NORMAL;

    @ApiModelProperty(value = "状态 默认0正常 -1拉黑")
    private Integer status = CommonConstant.USER_STATUS_NORMAL;

    @ApiModelProperty(value = "描述/详情/备注")
    private String description;

    @ApiModelProperty(value = "所属部门id")
    private String departmentId;

    /**
     * 需要再封装的对象
     */
    @Transient
    @ApiModelProperty(value = "所属部门名称")
    private String departmentTitle;

    @Transient
    @ApiModelProperty(value = "用户拥有角色")
    private List<Role> roles;

    @Transient
    @ApiModelProperty(value = "用户拥有的权限")
    private List<Permission> permissions;

    @Transient
    @ApiModelProperty(value = "导入数据时使用")
    private Integer defaultRole;

    @Transient
    @ApiModelProperty(value = "验证登录的WebToken")
    private String webToken;

}
