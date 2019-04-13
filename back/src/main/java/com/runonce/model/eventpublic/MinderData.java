package com.runonce.model.eventpublic;

import java.io.Serializable;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

/**
 * Created by xuxueli on '2018-12-12 19:56:38'.
 */
@Data
@Table(name = "minder_data")
public class MinderData implements Serializable {
    private static final long serialVersionUID = 42L;

    /**
     * id
     */
    @Id
    @Column(name = "id")
    private String id;

    /**
     *
     */
    @Column(name = "minderType")
    private Integer minderType;

    /**
     *
     */
    @Column(name = "minderName")
    private String minderName;

    /**
     * 事项id
     */
    @NotBlank(message = "事项id不能为空")
    @Column(name = "eventId")
    private String eventId;

    /**
     *
     */
    @NotBlank(message = "导图数据不能为空")
    @Column(name = "MinderData")
    private String minderData;


    @NotBlank(message = "导图路径不能为空")
    @Transient
    private String picPath;// 导图路径

    @NotNull(message = "导图类型不能为空!")
    @Transient
    private Integer type;// 导图路径(1.申请类型导图2..办事情形导图 2.场景导出导图)

}