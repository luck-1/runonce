package com.runonce.model.eventversionone;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;

/**
 * @Author: zhaolei
 * @Descriptions: 系统对接类
 * @Date: create at 2018/12/29 0029 下午 4:33
 */
@Data
@Table(name = "system_docking")
public class SystemDocking implements Serializable {

    @Id
    @Column(name = "id")
    private String id;

    /**
     * 事项id
     */
    @NotBlank(message = "事项id不能为空")
    @Column(name = "eventId")
    private String eventId;
    /**
     * 系统对接图名称
     */
    @Column(name = "picName")
    private String picName;
    /**
     * 图片路径
     */
    @Column(name = "picPath")
    private String picPath;
    /**
     * 备注
     */
    @Column(name = "reMarks")
    private String reMarks;
    /**
     * 图片数据
     */
    @Column(name = "picData")
    private String picData;
    /**
     * 图形类型(1.用户上传 2. 工具绘制 3.用户选择)
     */
    @NotBlank(message = "图形类型不能为空")
    @Column(name = "picType")
    private String picType;

    /**
     * 图形为用户选择时的图形记录id
     */
    @Column(name = "defaultPicId")
    private String defaultPicId;
    /**
     * 编码的图片数据
     */
    @Transient
    private String codeedXmlString;
    /**
     * 图片宽度
     */
    @Transient
    private int width;
    /**
     * 图片高度
     */
    @Transient
    private int height;
    /**
     * 状态 (1.未完善 2.已完善)
     */
    @Transient
    private int state;

    @Transient
    @ApiModelProperty(value = "版本")
    private Integer version;
}
