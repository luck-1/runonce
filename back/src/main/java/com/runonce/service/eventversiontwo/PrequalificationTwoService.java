package com.runonce.service.eventversiontwo;
import org.springframework.stereotype.Component;
import com.runonce.service.Service;
import  com.runonce.model.eventversiontwo.PrequalificationTwo;

import java.util.List;


/**
* 
*
* Created by xuxueli on '2018-12-10 15:04:12'.
*/
@Component
public interface PrequalificationTwoService extends Service<PrequalificationTwo> {
    public List<PrequalificationTwo> findByEventId(String eventId);
    public void save(List<PrequalificationTwo> list);
}
