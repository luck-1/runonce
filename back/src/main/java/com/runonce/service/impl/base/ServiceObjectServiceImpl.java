package com.runonce.service.impl.base;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import com.runonce.service.base.ServiceObjectService;
import com.runonce.dao.base.ServiceObjectDao;
import com.runonce.service.AbstractService;
import  com.runonce.model.base.ServiceObject;

/**
* 
*
* Created by xuxueli on '2018-12-10 14:57:47'.
*/
@Service
public class ServiceObjectServiceImpl extends AbstractService<ServiceObject> implements ServiceObjectService  {

	@Resource
	private ServiceObjectDao serviceObjectDao;

	
}
