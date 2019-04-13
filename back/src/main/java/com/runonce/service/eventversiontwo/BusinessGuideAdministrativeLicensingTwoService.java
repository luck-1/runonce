package com.runonce.service.eventversiontwo;
import org.springframework.stereotype.Component;
import com.runonce.service.Service;
import  com.runonce.model.eventversiontwo.BusinessGuideAdministrativeLicensingTwo;



/**
* 
*
* Created by xuxueli on '2018-12-10 15:04:12'.
*/
@Component
public interface BusinessGuideAdministrativeLicensingTwoService extends Service<BusinessGuideAdministrativeLicensingTwo> {

    BusinessGuideAdministrativeLicensingTwo findByEventId(String EventId);

    void deleteByEventId(String eventId);

}
