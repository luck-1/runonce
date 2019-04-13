package com.runonce.model.system;

import com.runonce.util.constant.CommonConstant;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;
import javax.persistence.*;
import java.util.List;

/**
 * @author Exrickx
 */
@Data
@Entity
@DynamicUpdate
@Table(name = "t_user")
public class User extends BaseEntity {
    private static final long serialVersionUID = 42L;

    @ApiModelProperty(value = "用户名")
    @Column(name = "user_name", unique = true, nullable = false)
    private String userName;

    @ApiModelProperty(value = "密码")
    @Column(name = "password")
    private String password;

    @ApiModelProperty(value = "昵称")
    @Column(name = "nick_name")
    private String nickName;

    @ApiModelProperty(value = "手机")
    @Column(name = "mobile")
    private String mobile;

    @ApiModelProperty(value = "邮件")
    @Column(name = "email")
    private String email;

    @ApiModelProperty(value = "省市县地址")
    @Column(name = "address")
    private String address;

    @ApiModelProperty(value = "街道地址")
    @Column(name = "street")
    private String street;

    @ApiModelProperty(value = "0女 1男 2保密")
    @Column(name = "sex")
    private Integer sex;

    @ApiModelProperty(value = "密码强度")
    @Column(name = "pass_strength",length = 2)
    private String passStrength;

    @ApiModelProperty(value = "用户头像")
    @Column(name = "avatar",length = 1000)
    private String avatar = CommonConstant.USER_DEFAULT_AVATAR;

    @ApiModelProperty(value = "用户类型 0普通用户 1管理员")
    @Column(name = "type")
    private Integer type = CommonConstant.USER_TYPE_NORMAL;

    @ApiModelProperty(value = "状态 默认0正常 -1拉黑")
    @Column(name = "status")
    private Integer status = CommonConstant.USER_STATUS_NORMAL;

    @ApiModelProperty(value = "描述/详情/备注")
    @Column(name = "description")
    private String description;

    @ApiModelProperty(value = "区域id")
    @Column(name = "location_id")
    private String locationId;

    @ApiModelProperty(value = "所属部门id")
    @Column(name = "department_id")
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
