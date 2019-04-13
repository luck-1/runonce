package com.runonce.httpbean.assets.reqbean;

import com.runonce.model.eventpublic.MinderData;
import lombok.Data;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @author swq
 * @date 2019/1/30 0030
 * @description
 */
@Data
public class MinderDataReq {

    @Valid
    @NotNull(message = "对象不能为空！")
    private MinderData minderData;

    /**
     * 版本号
     */
    @Valid
    @NotNull(message = "版本号不能为空")
    private Integer version;
}
