package com.runonce.service.eventversionone;
import com.runonce.ReturnCode;
import com.runonce.model.http.EventDirectoryParam;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import com.runonce.service.Service;
import  com.runonce.model.eventversionone.EventDirectory;

import java.util.List;


/**
* 三级四同目录
*
* Created by xuxueli on '2019-01-16 12:00:31'.
*/
@Component
public interface EventDirectoryService extends Service<EventDirectory> {

    ReturnCode add(EventDirectory  eventDirectory);

    ReturnCode updateEventDirectory(EventDirectory  eventDirectory);

    ReturnCode findEventDirectory(EventDirectoryParam eventDirectoryParam);

    Boolean findGroupIdCount(String eventName,String id);

    List<EventDirectory> findPrivate(String deptId);

    List<EventDirectory> findCatalogList(String deptId);

    Integer checkDirectoryIsUsed(String deptId);

}
