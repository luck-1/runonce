package com.runonce.service.eventversionone;
import com.github.pagehelper.PageInfo;
import com.runonce.model.http.TasktodoReq;
import org.springframework.stereotype.Component;
import com.runonce.service.Service;
import  com.runonce.model.eventversionone.Tasktodo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;


/**
* taskToDo
*
* Created by xuxueli on '2019-02-21 11:20:11'.
*/
@Component
public interface TasktodoService extends Service<Tasktodo> {


    PageInfo<Tasktodo> findByUserId(String userId, TasktodoReq tasktodoReq);

    Map clickTodo(String id, HttpServletRequest request);

    Boolean sendTodo(String dmId,String eventName ,Integer type, List<String > userIds,String content);
}
