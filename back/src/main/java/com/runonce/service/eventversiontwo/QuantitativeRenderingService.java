package com.runonce.service.eventversiontwo;
import com.runonce.ReturnCode;
import com.runonce.httpbean.assets.reqbean.QuantitativeRenderingReq;
import com.runonce.model.http.ProcessDataParam;
import org.springframework.stereotype.Component;
import com.runonce.service.Service;
import  com.runonce.model.eventversiontwo.QuantitativeRendering;
import org.xml.sax.SAXException;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;


/**
* 
*
* Created by xuxueli on '2018-12-10 15:04:12'.
*/
@Component
public interface QuantitativeRenderingService extends Service<QuantitativeRendering> {


    String generatePngByXml(ProcessDataParam param,HttpServletRequest request) throws IOException, ParserConfigurationException, SAXException;

    ReturnCode saveInfo(QuantitativeRendering quantitativeRendering,Integer version, HttpServletRequest request);

    QuantitativeRenderingReq findBydmId(String dmId);
}
