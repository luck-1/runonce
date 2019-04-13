package com.runonce.service.eventversiontwo;
import org.springframework.stereotype.Component;
import com.runonce.service.Service;
import  com.runonce.model.eventversiontwo.BusinessGuideAuditAndForwardingCategoryTwo;



/**
* 
*
* Created by xuxueli on '2018-12-10 15:04:12'.
*/
@Component
public interface BusinessGuideAuditAndForwardingCategoryTwoService extends Service<BusinessGuideAuditAndForwardingCategoryTwo> {

    BusinessGuideAuditAndForwardingCategoryTwo findByEventId(String EventId);

    void deleteByEventId(String eventId);

}
