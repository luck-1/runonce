package com.runonce.model.http;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;

@Data
public class ChangeDepartmentParam {

    @ApiModelProperty(value = "被转移的事项目录id")
    private List<String> ids;

    @ApiModelProperty(value = "转移到的部门id")
    private String newDeptId;

    @ApiModelProperty(value = "（0 -> 用户转移），（1 -> 事项转移） ")
    private Integer type;
}
