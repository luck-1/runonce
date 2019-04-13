package com.runonce.model.http;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: zhaolei
 * @Descriptions:
 * @Date: create at 2018/12/13 0013 上午 9:58
 */
@Data
public class MinderNodeData implements Serializable,Cloneable {
    private String id;
    private long created;
    private String text;
    private String leafNode;
    private String materialId;
    private String fieldType;
}
