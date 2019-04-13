package com.runonce.httpbean.assets.reqbean;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * @author swq
 * @date 2019/2/11 0011
 * @description
 */
@Data
public class ResetPassWordReq {
    @NotNull(message = "用户名ID不能为null")
    private  String userId;
    @NotNull(message = "用户名ID不能为null")
    private  String passWord;
}
