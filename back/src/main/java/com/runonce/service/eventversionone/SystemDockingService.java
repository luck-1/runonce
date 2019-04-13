package com.runonce.service.eventversionone;

import com.runonce.ReturnCode;
import com.runonce.model.eventversionone.SystemDocking;
import com.runonce.service.Service;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

/**
 * @Author: zhaolei
 * @Descriptions: 系统对接service
 * @Date: create at 2018/12/29 0029 下午 4:45
 */
@Component
public interface SystemDockingService extends Service<SystemDocking> {
    ReturnCode saveInfo(SystemDocking systemDocking, HttpServletRequest request);

    ReturnCode selectByEventId(String eventId);
}
