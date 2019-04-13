package com.runonce.service.eventversiontwo;
import org.springframework.stereotype.Component;
import com.runonce.service.Service;
import  com.runonce.model.eventversiontwo.BusinessGuideOtherServicesTwo;



/**
* 
*
* Created by xuxueli on '2018-12-10 15:04:12'.
*/
@Component
public interface BusinessGuideOtherServicesTwoService extends Service<BusinessGuideOtherServicesTwo> {

    BusinessGuideOtherServicesTwo findByEventId(String EventId);

    void deleteByEventId(String eventId);

}
