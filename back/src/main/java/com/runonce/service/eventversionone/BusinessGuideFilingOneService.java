package com.runonce.service.eventversionone;
import com.runonce.ReturnCode;
import com.runonce.dao.eventversionone.BusinessGuideAdministrativeLicensingOneDao;
import com.runonce.httpbean.assets.reqbean.BusinessGuideFilingOneReq;
import com.runonce.model.eventversionone.BusinessGuideAdministrativeLicensingOne;
import com.runonce.model.eventversionone.BusinessGuideFilingOne;
import com.runonce.model.eventversionone.BusinessGuideFilingOnePrivate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.runonce.service.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* 
*
* Created by xuxueli on '2018-12-10 11:31:13'.
*/
@Component
public interface BusinessGuideFilingOneService extends Service<BusinessGuideFilingOne> {

    BusinessGuideFilingOneReq findByEventId(String EventId);

//    List<BusinessGuideFilingOne> findBymajorTypesOfEvents(String majorTypesOfEvents);

    void deleteByEventId(String eventId);

    ReturnCode add(BusinessGuideFilingOne businessGuideFilingOne,
                   BusinessGuideFilingOnePrivate businessGuideFilingOnePrivate,
                   HttpServletRequest request);


    /**
     * 保存Private表信息
     * @param businessGuideFilingOnePrivate
     */
    void savePrivate(BusinessGuideFilingOnePrivate businessGuideFilingOnePrivate,
                     HttpServletRequest request);
}
