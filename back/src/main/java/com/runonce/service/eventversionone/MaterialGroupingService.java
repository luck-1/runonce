package com.runonce.service.eventversionone;
import com.runonce.ReturnCode;
import com.runonce.model.eventpublic.MinderData;
import com.runonce.model.http.MaterialGroupAddParam;
import com.runonce.model.http.MaterialGroupParam;
import com.runonce.model.http.MaterialGroupVersionParam;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import com.runonce.service.Service;
import  com.runonce.model.eventversionone.MaterialGrouping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
* 
*
* Created by xuxueli on '2018-12-10 11:31:13'.
*/
@Component
public interface MaterialGroupingService extends Service<MaterialGroupAddParam> {

    MaterialGroupVersionParam selectByEventId(String eventId);

    ReturnCode saveInfo(MaterialGroupParam materialGroupParam, HttpServletRequest request);

     List<MaterialGroupAddParam> selectByEventIdOrderByType(String eventId);

    List<MaterialGroupAddParam>  selectByEventIdAndIo(String eventId,String io);

}
