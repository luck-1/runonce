package com.runonce.model.system;

import com.runonce.util.constant.CommonConstant;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * @author Exrick
 */
@Data
@Entity
@DynamicUpdate
@Table(name = "t_dict_data")
public class DictData extends BaseEntity {

    private static final long serialVersionUID = 42L;

    @ApiModelProperty(value = "数据名称")
    @Column(name = "title")
    private String title;

    @ApiModelProperty(value = "数据值")
    @Column(name = "value")
    private String value;

    @ApiModelProperty(value = "排序值")
    @Column(name = "sort_order",precision = 10, scale = 2)
    private BigDecimal sortOrder;

    @ApiModelProperty(value = "是否启用 0启用 -1禁用")
    @Column(name = "status")
    private Integer status = CommonConstant.STATUS_NORMAL;

    @ApiModelProperty(value = "备注")
    @Column(name = "description")
    private String description;

    @ApiModelProperty(value = "所属字典")
    private String dictId;
}