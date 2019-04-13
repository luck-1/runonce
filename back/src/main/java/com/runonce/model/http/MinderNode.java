package com.runonce.model.http;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: zhaolei
 * @Descriptions: 思维导图节点数据
 * @Date: create at 2018/12/13 0013 上午 9:55
 */
@Data
public class MinderNode implements Serializable,Cloneable{

    private MinderNodeData data;

    private List<MinderNode> children;

    public Object clone() throws CloneNotSupportedException{
        return super.clone();
    }
}
