package com.runonce.service.eventversionone;
import com.runonce.ReturnCode;
import com.runonce.httpbean.assets.reqbean.BusinessGuideAdministrativeLicensingOneReq;
import com.runonce.model.eventversionone.BusinessGuideAdministrativeLicensingOnePrivate;
import org.springframework.stereotype.Component;
import com.runonce.service.Service;
import  com.runonce.model.eventversionone.BusinessGuideAdministrativeLicensingOne;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;


/**
* 
*
* Created by xuxueli on '2018-12-10 11:31:13'.
*/
@Component
public interface BusinessGuideAdministrativeLicensingOneService extends Service<BusinessGuideAdministrativeLicensingOne> {

    BusinessGuideAdministrativeLicensingOneReq findByEventId(String EventId);

    void deleteByEventId(String eventId);

    ReturnCode add(BusinessGuideAdministrativeLicensingOne businessGuideAdministrativeLicensingOne,
                   BusinessGuideAdministrativeLicensingOnePrivate businessGuideAdministrativeLicensingOnePrivate,
                   HttpServletRequest request);

    /**
     * 保存Private信息
     * @param businessGuideAdministrativeLicensingOnePrivate
     */
    void savePrivate(BusinessGuideAdministrativeLicensingOnePrivate businessGuideAdministrativeLicensingOnePrivate, HttpServletRequest request);

    Map<String,List> getAllList(String eventType);
}
