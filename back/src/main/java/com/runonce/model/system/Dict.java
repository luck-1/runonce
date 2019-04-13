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
@Table(name = "t_dict")
public class Dict extends BaseEntity {

    private static final long serialVersionUID = 42L;

    @ApiModelProperty(value = "字典名称")
    @Column(name = "title")
    private String title;

    @ApiModelProperty(value = "字典类型")
    @Column(name = "type")
    private String type;

    @ApiModelProperty(value = "备注")
    @Column(name = "description")
    private String description;
}