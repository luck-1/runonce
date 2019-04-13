package com.runonce.service.eventversionone;
import org.springframework.stereotype.Component;
import com.runonce.service.Service;
import  com.runonce.model.eventversionone.DefaultSystemDockingPic;

import java.util.List;


/**
* 
*
* Created by xuxueli on '2019-01-04 10:40:30'.
*/
@Component
public interface DefaultSystemDockingPicService extends Service<DefaultSystemDockingPic> {


    List<DefaultSystemDockingPic> selectAll();
}
