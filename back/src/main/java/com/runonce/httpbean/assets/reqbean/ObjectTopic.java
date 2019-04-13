package com.runonce.httpbean.assets.reqbean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
public class ObjectTopic {


    @ApiModelProperty(value = "服务对象ID")
    private String fObjectId;

    @ApiModelProperty(value = "服务主题ID")
    private String fTopicId;
}