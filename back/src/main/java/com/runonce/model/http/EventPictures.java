package com.runonce.model.http;

import com.runonce.model.eventversionone.MaterialGrouping;
import com.runonce.model.eventversionone.Prequalification;
import com.runonce.model.eventversiontwo.QuantitativeRendering;
import lombok.Data;

import java.util.List;

/**
 * @Author: zhaolei
 * @Descriptions: 事项相关材料
 * @Date: create at 2018/12/21 0021 下午 12:08
 */
@Data
public class EventPictures {

    private String eventName;// 事项名称

    private String processingFlowPic;// 办理流程图

    private List<MaterialGrouping> materialGroupingList;// 材料list

    private QuantitativeRendering quantitativeRendering;// 量化呈现

    private String situationPic;// 办事情形图

    private List<Prequalification> prequalificationList;// 资格预审list
}
