package com.runonce.httpbean.assets.reqbean;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Exrickx
 */
@Data
public class PageReq implements Serializable{

    private static final long serialVersionUID = 42L;

    @ApiModelProperty(value = "页号")
    private int pageNumber;

    @ApiModelProperty(value = "页面大小")
    private int pageSize;

    @ApiModelProperty(value = "排序字段")
    private String sort;

    @ApiModelProperty(value = "排序方式 asc/desc")
    private String order;
}
