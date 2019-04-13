package com.runonce.model.system;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.runonce.util.constant.CommonConstant;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Exrickx
 */
@Data
@MappedSuperclass
@JsonDeserialize
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public abstract class BaseEntity implements Serializable{

    private static final long serialVersionUID = 42L;

    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "JDBC")
    @GenericGenerator(name = "JDBC", strategy = "uuid")
    @ApiModelProperty(value = "唯一标识")
    private String id ;

    @ApiModelProperty(value = "创建者")
    @Column(name = "create_by")
    private String createBy;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    @Column(name = "create_time")
    private Date createTime;

    @ApiModelProperty(value = "更新者")
    @Column(name = "update_by")
    private String updateBy;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新时间")
    @Column(name = "update_time")
    private Date updateTime;

    @ApiModelProperty(value = "删除标志 默认0")
    @Column(name = "del_flag")
    private Integer delFlag = CommonConstant.STATUS_NORMAL;
}
