package com.runonce.httpbean.assets.reqbean;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @author swq
 * @date 2019/2/11 0011
 * @description
 */
@Data
public class RevisePassWordReq {

    @NotBlank(message = "请输入旧密码")
    private String oldPass;

    @NotBlank(message = "请输入新密码")
    private String newPass;

}
