package com.runonce.model.eventversionone;
import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

/**
*  
*
*  Created by xuxueli on '2018-12-10 12:17:30'.
*/
@Data
@Table(name="prequalification")
public class Prequalification implements Serializable {
    private static final long serialVersionUID = 42L;

    /**
    * id
    */
    @Id
    @Column(name = "id")
    private String id;

    /**
    * 预审内容
    */
    @ApiModelProperty(value="预审内容")
    @Column(name = "pretrialContent")
    private String pretrialContent;

    /**
    * 审核规范
    */
    @ApiModelProperty(value="审核规范")
    @Column(name = "auditSpecification")
    private String auditSpecification;

    /**
    * 事项id
    */
//    @ApiModelProperty(value="")
    @NotNull
    @Column(name = "eventId")
    private String eventId;

    /**
    * 现有审核方式（纸质审核/平台查询）
    */
    @ApiModelProperty(value="现有审核方式（纸质审核/平台查询）")
    @Column(name = "auditMode")
    private String auditMode;

    /**
    * 现有审核操作（平台查询需截图，纸质审核需描述）
    */
    @ApiModelProperty(value="现有审核操作（平台查询需截图，纸质审核需描述）")
    @Column(name = "auditOperation")
    private String auditOperation;

    /**
    * 备注（平台查询登录方式）
    */
    @ApiModelProperty(value="备注（平台查询登录方式）")
    @Column(name = "remarks")
    private String remarks;

    /**
    * 现有审核操作图片（平台查询需截图，纸质审核需描述）
    */
//    @ApiModelProperty(value="")
    @Column(name = "auditOperationPic")
    private String auditOperationPic;
    /**
     * 序号
     */
    @ApiModelProperty(value="序号")
    @Column(name = "itemNumber")
    private String itemNumber;
//    @Column(name = "primary")
//    private Object primary;

    @Transient
    @ApiModelProperty(value = "版本")
    private Integer version;
}