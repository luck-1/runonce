package com.runonce.model.system;

import com.runonce.util.constant.CommonConstant;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.List;

/**
 * @author Exrickx
 */
@Data
@Entity
@DynamicUpdate
@Table(name = "t_role")
public class Role  extends BaseEntity {

    private static final long serialVersionUID = 42L;

    @ApiModelProperty(value = "角色名 以ROLE_开头")
    @Column(name = "name")
    private String name;

    @ApiModelProperty(value = "是否为注册默认角色")
    @Column(name = "default_role")
    private Boolean defaultRole;

    @ApiModelProperty(value = "数据权限类型 0全部默认 1自定义 2自定义")
    @Column(name = "data_type")
    private Integer dataType = CommonConstant.DATA_TYPE_ALL;

    @ApiModelProperty(value = "备注")
    @Column(name = "description")
    private String description;


    /**
     * 需要再封装的对象
     */
    @Transient
    @ApiModelProperty(value = "拥有权限")
    private List<RolePermission> permissions;

    @Transient
    @ApiModelProperty(value = "拥有数据权限")
    private List<RoleDepartment> departments;
}
