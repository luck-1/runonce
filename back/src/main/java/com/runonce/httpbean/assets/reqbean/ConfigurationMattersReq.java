package com.runonce.httpbean.assets.reqbean;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author swq
 * @date 2019/3/1 0001
 * @description
 */
@Data
public class ConfigurationMattersReq {


    /**
     * ID
     */
    @NotBlank(message = "\"ID\"为空")
    private String id;

    /**
     * 事项ID
     */
    @NotNull(message = "\"事项ID\"为空")
    private List<String> DepartmentalMattersList;


}
