package com.runonce.httpbean.assets.reqbean;

import com.runonce.model.eventversionone.BusinessGuideAuditAndForwardingCategoryOne;
import com.runonce.model.eventversionone.BusinessGuideAuditAndForwardingCategoryOnePrivate;
import lombok.Data;

import javax.validation.Valid;

/**
 * @author swq
 * @date 2019/1/17 0017
 * @description
 */
@Data
public class BusinessGuideAuditAndForwardingCategoryOneReq {

    @Valid
    private BusinessGuideAuditAndForwardingCategoryOne businessGuideAuditAndForwardingCategoryOne;

    @Valid
    private BusinessGuideAuditAndForwardingCategoryOnePrivate businessGuideAuditAndForwardingCategoryOnePrivate;
}
