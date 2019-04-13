package com.runonce.service.eventpublic;
import com.runonce.ReturnCode;
import com.runonce.httpbean.assets.reqbean.MinderDataReq;
import org.springframework.stereotype.Component;
import com.runonce.service.Service;
import  com.runonce.model.eventpublic.MinderData;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


/**
* 
*
* Created by xuxueli on '2018-12-12 19:56:37'.
*/
@Component
public interface MinderDataService extends Service<MinderData> {

    MinderDataReq selectByEventId(String eventId,Integer type);

    String getWorkSitutionData(String eventId) throws CloneNotSupportedException, IOException, ClassNotFoundException;

    ReturnCode saveInfo(MinderDataReq minderDataReq, HttpServletRequest request);
}
