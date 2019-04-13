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
import java.util.List;

/**
 * 菜单/权限
 * @author Exrick
 */
@Data
@Entity
@DynamicUpdate
@Table(name = "t_permission")
public class Permission extends BaseEntity  {

    private static final long serialVersionUID = 42L;

    @ApiModelProperty(value = "菜单/权限名称")
    @Column(name = "name")
    private String name;

    @ApiModelProperty(value = "层级")
    @Column(name = "level")
    private Integer level;

    @ApiModelProperty(value = "类型 0页面 1具体操作")
    @Column(name = "type")
    private Integer type;

    @ApiModelProperty(value = "菜单标题")
    @Column(name = "title")
    private String title;

    @ApiModelProperty(value = "页面路径/资源链接url")
    @Column(name = "path")
    private String path;

    @ApiModelProperty(value = "前端组件")
    @Column(name = "component")
    private String component;

    @ApiModelProperty(value = "图标")
    @Column(name = "icon")
    private String icon;

    @ApiModelProperty(value = "按钮权限类型")
    @Column(name = "button_type")
    private String buttonType;

    @ApiModelProperty(value = "父id")
    @Column(name = "parent_id")
    private String parentId;

    @ApiModelProperty(value = "说明备注")
    @Column(name = "description")
    private String description;

    @ApiModelProperty(value = "排序值")
    @Column(name = "sort_order",precision = 10, scale = 2)
    private BigDecimal sortOrder;

    @ApiModelProperty(value = "是否启用 0启用 -1禁用")
    @Column(name = "status")
    private Integer status = CommonConstant.STATUS_NORMAL;

    @ApiModelProperty(value = "网页链接")
    @Column(name = "url")
    private String url;

    /**
     * 需要再封装的对象
     */
    @Transient
    @ApiModelProperty(value = "子菜单/权限")
    private List<Permission> children;

    @Transient
    @ApiModelProperty(value = "页面拥有的权限类型")
    private List<String> permTypes;


    /**
     * 需要设定的对象
     */
    @Transient
    @ApiModelProperty(value = "节点展开 前端所需")
    private Boolean expand = true;

    @Transient
    @ApiModelProperty(value = "是否勾选 前端所需")
    private Boolean checked = false;

    @Transient
    @ApiModelProperty(value = "是否选中 前端所需")
    private Boolean selected = false;
}