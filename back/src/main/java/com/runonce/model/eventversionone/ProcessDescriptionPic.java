package com.runonce.model.eventversionone;
import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 *
 *  办理流程图
 *  Created by xuxueli on '2018-12-10 12:17:30'.
 */
@Data
@Table(name="process_description_pic")
public class ProcessDescriptionPic implements Serializable {
    private static final long serialVersionUID = 42L;

    /**
     * id
     */
    @Id
    @Column(name = "id")
    private String id;

    /**
     * 事项id
     */
    @Column(name = "eventId")
    private String eventId;

    /**
     * 图片名称
     */
    @Column(name = "picname")
    private String picname;
    /**
     * 图片数据
     */
    @Column(name = "picliu")
    private String picliu;
    /**
     * 图片路径
     */
    @Column(name = "picbase")
    private String picbase;
    /**
     * 流程图类型(1.用户上传 2.工具绘制)
     */
    @Column(name = "picType")
    private Integer picType;
    /**
     * 转码数据
     */
    @Transient
    private String codeedXmlString;
    /**
     * 图片宽
     */
    @Transient
    private int width;
    /**
     * 图片高
     */
    @Transient
    private int height;

    /**
     * 状态
     */
    @Transient
    private int state;

    @ApiModelProperty(value = "条件")
    @Column(name = "conditionTemp")
    private String conditionTemp;


    /**
     * 条件在前端展示，不存储到数据库
     */
    @Transient
    private List conditionList;
    
}