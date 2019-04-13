package com.runonce.service.base;
import com.runonce.ReturnCode;
import org.springframework.stereotype.Component;
import com.runonce.service.Service;
import  com.runonce.model.base.ServiceTopic;

import java.util.List;


/**
* 
*
* Created by xuxueli on '2018-12-11 09:37:24'.
*/
@Component
public interface ServiceTopicService extends Service<ServiceTopic> {

    List<ServiceTopic> findByPid(List<String> pids);

    /**
     * 发送所有服务主题
     * @return ReturnCode
     */
    ReturnCode sendTopic();

//    String selectByCode(String code);
}
