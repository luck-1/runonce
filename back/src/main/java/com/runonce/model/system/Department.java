package com.runonce.model.system;

import com.runonce.util.constant.CommonConstant;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;

/**
 * @author Exrick
 */
@Data
@Entity
@DynamicUpdate
@Table(name = "t_department")
public class Department extends BaseEntity {

    private static final long serialVersionUID = 42L;

    @ApiModelProperty(value = "部门名称")
    @Column(name = "title")
    private String title;

    @ApiModelProperty(value = "父id")
    @Column(name = "parent_id")
    private String parentId;

    @ApiModelProperty(value = "是否地点")
    @Column(name = "is_location")
    private Boolean isLocation;

    @ApiModelProperty(value = "是否为父节点(含子节点) 默认false")
    @Column(name = "is_parent")
    private Boolean isParent = false;

    @ApiModelProperty(value = "排序值")
    @Column(name = "sort_order",precision = 10, scale = 2)
    private BigDecimal sortOrder;

    @ApiModelProperty(value = "是否启用 0启用 -1禁用")
    @Column(name = "status")
    private Integer status = CommonConstant.STATUS_NORMAL;

    @ApiModelProperty(value = "地区代码")
    @Column(name = "area_code")
    private String areaCode;

    @ApiModelProperty(value = "层级")
    @Column(name = "region")
    private String region;
    /**
     * 需要再封装的对象
     */
    @Transient
    @ApiModelProperty(value = "父节点名称")
    private String parentTitle;

    @ApiModelProperty(value = "18位社会信用代码")
    @Column(name = "social_credit_code")
    private String socialCreditCode;

    @ApiModelProperty(value = "对应中央部门ID")
    @Column(name = "central_departments")
    private String centralDepartments;
}