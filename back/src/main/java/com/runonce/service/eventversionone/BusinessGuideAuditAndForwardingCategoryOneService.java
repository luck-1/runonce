package com.runonce.service.eventversionone;
import com.runonce.ReturnCode;
import com.runonce.httpbean.assets.reqbean.BusinessGuideAuditAndForwardingCategoryOneReq;
import com.runonce.model.eventversionone.BusinessGuideAuditAndForwardingCategoryOnePrivate;
import org.springframework.stereotype.Component;
import com.runonce.service.Service;
import  com.runonce.model.eventversionone.BusinessGuideAuditAndForwardingCategoryOne;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
* 
*
* Created by xuxueli on '2018-12-10 11:31:13'.
*/
@Component
public interface BusinessGuideAuditAndForwardingCategoryOneService extends Service<BusinessGuideAuditAndForwardingCategoryOne> {

    BusinessGuideAuditAndForwardingCategoryOneReq findByEventId(String EventId);

    void deleteByEventId(String eventId);

    ReturnCode add(BusinessGuideAuditAndForwardingCategoryOne  businessGuideAuditAndForwardingCategoryOne,
                   BusinessGuideAuditAndForwardingCategoryOnePrivate businessGuideAuditAndForwardingCategoryOnePrivate,
                   HttpServletRequest request);

    /**
     * 保存Private表信息
     * @param businessGuideAuditAndForwardingCategoryOnePrivate
     */
    void savePrivate(BusinessGuideAuditAndForwardingCategoryOnePrivate businessGuideAuditAndForwardingCategoryOnePrivate,
                     HttpServletRequest request);
}
