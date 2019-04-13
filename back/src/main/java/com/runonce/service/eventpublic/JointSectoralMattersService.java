package com.runonce.service.eventpublic;

import com.github.pagehelper.PageInfo;
import com.runonce.httpbean.assets.reqbean.JointSectoralMattersReq;
import com.runonce.model.eventpublic.EventsTable;
import com.runonce.model.eventpublic.JointSectoralMatterReq;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Component
public interface JointSectoralMattersService {


    EventsTable add(JointSectoralMattersReq jointSectoralMattersReq, HttpServletRequest request);

    Integer deleteById(String id);

    JointSectoralMattersReq update(JointSectoralMattersReq jointSectoralMattersReq);

    PageInfo<EventsTable> selectDistributedEvents(JointSectoralMatterReq jointSectoralMatterReq);
}
