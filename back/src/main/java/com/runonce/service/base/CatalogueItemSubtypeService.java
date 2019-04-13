package com.runonce.service.base;
import org.springframework.stereotype.Component;
import com.runonce.service.Service;
import  com.runonce.model.base.CatalogueItemSubtype;

import java.util.List;


/**
* 
*
* Created by xuxueli on '2018-12-10 14:57:46'.
*/
@Component
public interface CatalogueItemSubtypeService extends Service<CatalogueItemSubtype> {

    List<CatalogueItemSubtype> findByPid(String Pid);

}
