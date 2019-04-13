package com.runonce.service.eventpublic;
import com.github.pagehelper.PageInfo;
import com.runonce.ReturnCode;
import com.runonce.model.http.EventDistributionParam;
import com.runonce.model.http.SelectDepartmentalMattersListParam;
import org.springframework.stereotype.Component;
import com.runonce.service.Service;
import  com.runonce.model.eventpublic.EventsTable;

import java.util.List;
import java.util.Map;


/**
* 
*
* Created by xuxueli on '2018-12-10 15:05:36'.
*/
@Component
public interface EventsTableService extends Service<EventsTable> {


    ReturnCode eventDistribution(EventDistributionParam param);

    PageInfo selectByParam(SelectDepartmentalMattersListParam param);

  //  List<EventsTable> selectDistributedEvents(String deptId);

    Map<String,String> getEncoding(String deptId, String catalogueItemSubtype, Boolean isMin,String isMindirectoryEncoding);
}
