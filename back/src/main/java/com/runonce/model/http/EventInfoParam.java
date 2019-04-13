package com.runonce.model.http;

import com.runonce.model.eventpublic.MinderData;
import com.runonce.model.eventversionone.*;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: zhaolei
 * @Descriptions:
 * @Date: create at 2018/12/12 0012 下午 3:56
 */
@Data
public class EventInfoParam implements Serializable{
    /**
     * 办事指南
     */
    private BusinessGuideFilingOne businessGuideFilingOne;

    private BusinessGuideAdministrativeLicensingOne businessGuideAdministrativeLicensingOne;

    private BusinessGuideOtherServicesOne businessGuideOtherServicesOne;

    private BusinessGuideAuditAndForwardingCategoryOne businessGuideAuditAndForwardingCategoryOne;
    /**
     * 材料分组
     */
    private List<MaterialGroupAddParam> materialGroupAddParamList;
    /**
     * 申请类型
     */
    private List<RelatedMaterialsParam> relatedMaterialsParamList;
    /**
     * 办事情形(申请类型)思维导图对象
     */
    private MinderData minderData;
    /**
     * 资格预审
     */
    private List<Prequalification> prequalificationList;

    /**
     * 现有流程说明(表格数据)
     */
    private List<ProcessDescription> processDescriptionList;

    /**
     * 流程图对象
     */

}
