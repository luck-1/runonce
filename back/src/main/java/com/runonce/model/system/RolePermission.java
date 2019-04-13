package com.runonce.model.system;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Exrick
 */
@Data
@Entity
@DynamicUpdate
@Table(name = "t_role_permission")

public class RolePermission extends BaseEntity  {

    private static final long serialVersionUID = 42L;

    @ApiModelProperty(value = "角色id")
    @Column(name = "role_id")
    private String roleId;

    @ApiModelProperty(value = "权限id")
    @Column(name = "permission_id")
    private String permissionId;
}