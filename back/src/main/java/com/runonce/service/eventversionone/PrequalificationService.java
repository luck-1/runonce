package com.runonce.service.eventversionone;
import com.runonce.ReturnCode;
import com.runonce.model.http.PrequalificationParam;
import org.springframework.stereotype.Component;
import com.runonce.service.Service;
import  com.runonce.model.eventversionone.Prequalification;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
* 
*
* Created by xuxueli on '2018-12-10 11:31:13'.
*/
@Component
public interface PrequalificationService extends Service<Prequalification> {
    List<Prequalification> findByEventId(String eventId);
    void deleteByEventId(String eventId);

    ReturnCode saveInfo(PrequalificationParam prequalificationParam, HttpServletRequest request);
}
