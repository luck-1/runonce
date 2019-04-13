package com.runonce.httpbean.assets.reqbean;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author zhangyulei
 * @Description //服务主题
 * @Date 13:45 2018/10/23 0023
 **/
@Data
public class ServiceTopic {

    @ApiModelProperty(value = "主题id")
    private String fTopicId;

    @ApiModelProperty(value = "主体")
    private String fMainBody;

    @ApiModelProperty(value = "导入主题id")
    private String fImportId;

    @ApiModelProperty(value = "主题名称")
    private String fTopicName;

    @ApiModelProperty(value = "主题点击次数")
    private Integer fTopicCount;

    @ApiModelProperty(value = "主题图标")
    private String fTopicIcon;

    @ApiModelProperty(value = "主题备注")
    private String fTopicRemarks;
}