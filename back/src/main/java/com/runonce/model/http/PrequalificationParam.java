package com.runonce.model.http;

import com.runonce.model.eventversionone.Prequalification;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.io.Serializable;
import java.util.List;

/**
 * @Author: zhaolei
 * @Descriptions: 资格预审保存参数
 * @Date: create at 2018/12/18 0018 下午 12:16
 */
@Data
public class PrequalificationParam implements Serializable{

    // 资格预审list
    private List<Prequalification> prequalificationList;
    // 资格预审状态(1. 未完善 2.已完善)
    private Integer state;
    // 事项id
    private String eventId;

    @Transient
    @ApiModelProperty(value = "版本")
    private Integer version;
}
