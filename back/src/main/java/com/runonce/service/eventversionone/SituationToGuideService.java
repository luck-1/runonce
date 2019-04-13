package com.runonce.service.eventversionone;
import com.runonce.model.eventversionone.SituationToGuideChild;
import org.springframework.stereotype.Component;
import com.runonce.service.Service;
import  com.runonce.model.eventversionone.SituationToGuide;

import java.util.List;


/**
* 情形引导
*
* Created by xuxueli on '2019-02-27 14:09:17'.
*/
@Component
public interface SituationToGuideService extends Service<SituationToGuide> {

    void add(SituationToGuide situationToGuide);

    void deleteBySelect(List<String> ids);

    List<SituationToGuide> findByEventName(String eventName);

//    List<SituationToGuide> findByKeyword(String keyword);

    void delete(String id);

    void addChildren(String pid,List<String> ids);

    List<SituationToGuideChild> findChildren(String pid);

    Boolean checkHasChild(String pid);

}
