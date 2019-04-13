package com.runonce.service.eventversionone;

import com.runonce.ReturnCode;
import com.runonce.httpbean.assets.reqbean.ProcessDescriptionPicReq;
import com.runonce.model.eventversionone.ProcessDescriptionPic;
import com.runonce.model.http.ProcessDataParam;
import com.runonce.service.Service;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;


/**
* 
*
* Created by xuxueli on '2018-12-10 11:31:13'.
*/
@Component
public interface ProcessDescriptionPicService extends Service<ProcessDescriptionPic> {

 ProcessDescriptionPicReq findByEventId(String eventId);
 int updateByEventId(ProcessDescriptionPic ProcessDescriptionPic);

    ReturnCode saveInfo(ProcessDescriptionPicReq processDescriptionPicReq,HttpServletRequest request) ;

    String generatePngByXml(ProcessDataParam param,HttpServletRequest request)throws IOException ;

    String generatePng(ProcessDataParam param, HttpServletRequest request) throws IOException, ParserConfigurationException, SAXException;
    int deleteByEventId(String EventId);

//    ProcessDescriptionPic findBydmId(String dmId);
}
