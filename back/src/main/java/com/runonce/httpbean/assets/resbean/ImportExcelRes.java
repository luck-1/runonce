package com.runonce.httpbean.assets.resbean;

import com.runonce.httpbean.assets.BusinessGuide;
import com.runonce.model.http.MaterialGroupVersionParam;
import lombok.Data;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author swq
 * @date 2019/2/15 0015
 * @description
 */
@Data
public class ImportExcelRes<T>{
    /**
     * 办事指南
     */
    private  T businessGuide;

    /**
     * 材料分组
     */
    private MaterialGroupVersionParam materialGroupVersionParam;

    /**
     * 图片对象
     */
    private Map<Integer,List> objectDataMap = new LinkedHashMap<>();

}
