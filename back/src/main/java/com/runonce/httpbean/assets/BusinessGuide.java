package com.runonce.httpbean.assets;

import lombok.Data;

/**
 * @author swq
 * @date 2019/2/15 0015
 * @description
 */
@Data
public class BusinessGuide<T> {

    /**
     * 办事指南
     */
    private  T businessGuide;
}
