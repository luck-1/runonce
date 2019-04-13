package com.runonce.service.base;
import com.runonce.ReturnCode;
import org.springframework.stereotype.Component;
import com.runonce.service.Service;
import  com.runonce.model.base.ServiceObject;



/**
* 
*
* Created by xuxueli on '2018-12-10 14:57:47'.
*/
@Component
public interface ServiceObjectService extends Service<ServiceObject> {

    void save(ServiceObject serviceObject);

}
