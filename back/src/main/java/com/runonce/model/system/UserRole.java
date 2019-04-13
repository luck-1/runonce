package com.runonce.model.system;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * @author Exrickx
 */
@Data
@Entity
@DynamicUpdate
@Table(name = "t_user_role")
public class UserRole extends BaseEntity{
    private static final long serialVersionUID = 42L;

    @ApiModelProperty(value = "用户唯一id")
    @Column(name = "user_id")
    private String userId;

    @ApiModelProperty(value = "角色唯一id")
    @Column(name = "role_id")
    private String roleId;

    /**
     * 需要再封装的对象
     */
    @Transient
    @ApiModelProperty(value = "角色名")
    private String roleName;
}
