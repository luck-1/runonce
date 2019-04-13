package com.runonce.model.http;

import com.runonce.model.eventversionone.ApplicationTypeField;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: zhaolei
 * @Descriptions: 申请类型叶子结点关联材料接口
 * @Date: create at 2018/12/11 0011 下午 5:47
 */
@Data
public class RelatedMaterialsParam extends ApplicationTypeField implements Serializable{
    /**
     * 申请类型字段id
     */
    private String applicationTypeFieldId;
    /**
     * 材料id
     */
    private List<String> materialId;
}
