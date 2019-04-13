package com.runonce.model.system;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author Exrick
 */
@Data
@Entity
@DynamicUpdate
@Table(name = "t_role_department")
public class RoleDepartment extends BaseEntity {

    private static final long serialVersionUID = 42L;

    @ApiModelProperty(value = "角色id")
    @Column(name = "role_id")
    private String roleId;

    @ApiModelProperty(value = "部门id")
    @Column(name = "department_id")
    private String departmentId;
}