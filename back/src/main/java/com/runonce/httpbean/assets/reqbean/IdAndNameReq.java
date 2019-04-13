package com.runonce.httpbean.assets.reqbean;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @author swq
 * @date 2019/3/1 0001
 * @description
 */
@Data
public class IdAndNameReq {

    private  String id;

    @NotBlank(message = "\"name\"为空")
    private  String name;
}
