package com.runonce.service.eventversionone;
import com.runonce.httpbean.assets.reqbean.MinderDataReq;
import com.runonce.httpbean.assets.reqbean.SituationBrainMapReq;
import com.runonce.model.eventpublic.MinderData;
import org.springframework.stereotype.Component;
import com.runonce.service.Service;
import  com.runonce.model.eventversionone.SituationBrainMap;



/**
* 情形引导-脑图
*
* Created by xuxueli on '2019-02-28 14:53:56'.
*/
@Component
public interface SituationBrainMapService extends Service<SituationBrainMap> {


    void saveInfo(SituationBrainMapReq situationBrainMapReq);

    SituationBrainMapReq selectByEventId(String situationId, Integer type);
}
