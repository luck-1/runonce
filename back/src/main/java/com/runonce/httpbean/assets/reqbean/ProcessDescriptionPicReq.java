package com.runonce.httpbean.assets.reqbean;

import com.runonce.model.eventversionone.ProcessDescriptionPic;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class ProcessDescriptionPicReq {

    @Valid
    @NotNull(message = "对象不能为空！")
    private List<ProcessDescriptionPic> processDescriptionPicList;

    @NotBlank(message = "事项ID不能为空！")
    private String eventId;

    @NotBlank(message = "状态不能为空！")
    private Integer state;

    /**
     * 版本号
     */
    @Valid
    @NotNull(message = "版本号不能为空")
    private Integer version;
}
