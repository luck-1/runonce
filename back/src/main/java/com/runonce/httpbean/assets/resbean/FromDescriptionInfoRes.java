package com.runonce.httpbean.assets.resbean;


import com.runonce.model.eventversionone.FormDescription;
import lombok.Data;

import java.util.List;

/**
 * @author swq
 * @date 2018/12/27 0027
 * @description
 */
@Data
public class FromDescriptionInfoRes {


    /**
     * 表单名称
     */
    private String fromDescriptionName;

    /**
     * 表单样表
     */
    private String fromDescriptionSampleTable;

    /**
     * 表单各字段
     */
    List<FormDescription> formDescriptionList;

}
