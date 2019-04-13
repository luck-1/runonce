package com.runonce.service.eventversionone;
import com.runonce.ReturnCode;
import com.runonce.httpbean.assets.reqbean.BusinessGuideOtherServicesOneReq;
import com.runonce.model.eventversionone.BusinessGuideOtherServicesOnePrivate;
import org.springframework.stereotype.Component;
import com.runonce.service.Service;
import  com.runonce.model.eventversionone.BusinessGuideOtherServicesOne;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
* 
*
* Created by xuxueli on '2018-12-10 11:31:13'.
*/
@Component
public interface BusinessGuideOtherServicesOneService extends Service<BusinessGuideOtherServicesOne> {

    BusinessGuideOtherServicesOneReq findByEventId(String EventId);

    void deleteByEventId(String eventId);

    ReturnCode add(BusinessGuideOtherServicesOne  businessGuideOtherServicesOne,
                   BusinessGuideOtherServicesOnePrivate businessGuideOtherServicesOnePrivate,
                   HttpServletRequest request);

    /**
     * 保存Private表信息
     * @param businessGuideOtherServicesOnePrivate
     */
    void savePrivate(BusinessGuideOtherServicesOnePrivate businessGuideOtherServicesOnePrivate,
                     HttpServletRequest request);
}
