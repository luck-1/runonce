package com.runonce.httpbean.assets.reqbean;

import com.runonce.model.system.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author swq
 * @date 2018/12/15 0015
 * @description
 */
@Data
public class SaveUserReq {
    @ApiModelProperty(value = "人员基础信息")
    private User user;

    @ApiModelProperty(value = "所选角色")
    private List<String> roles = new ArrayList<>();
}
