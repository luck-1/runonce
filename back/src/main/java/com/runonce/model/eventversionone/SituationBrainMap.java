package com.runonce.model.eventversionone;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

/**
*  情形引导-脑图
*
*  Created by xuxueli on '2019-02-28 14:53:56'.
*/
@Data
@Table(name="situation_brain_map")
public class SituationBrainMap implements Serializable {
    private static final long serialVersionUID = 42L;

    /**
    * 
    */
    @Id
    @Column(name = "id")
    private String id;

    /**
    * 
    */
    @Column(name = "type")
    private int type;

    /**
    * 
    */
    @Column(name = "situationId")
    private String situationId;

    /**
    * 
    */
    @Column(name = "minderData")
    private String minderData;

    /**
    * 
    */
    @Column(name = "updateTime")
    private Date updateTime;

    @NotBlank(message = "导图路径不能为空")
    @Transient
    private String picPath;// 导图路径


}