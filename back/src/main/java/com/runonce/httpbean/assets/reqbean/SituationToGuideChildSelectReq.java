package com.runonce.httpbean.assets.reqbean;

import com.runonce.httpbean.assets.PageBean;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Column;
import java.util.List;

/**
 * @author swq
 * @date 2019/3/1 0001
 * @description
 */
@Data
public class SituationToGuideChildSelectReq {

    @ApiModelProperty(value="名称")
    private String eventName;

    @ApiModelProperty(value="服务对象")
    private String serviceObject;

    @ApiModelProperty(value="行使层级")
    private String exerciseHierarchy;

    @ApiModelProperty(value="办理形式")
    private String handlingForm;


    private  PageBean pageBean ;


}
