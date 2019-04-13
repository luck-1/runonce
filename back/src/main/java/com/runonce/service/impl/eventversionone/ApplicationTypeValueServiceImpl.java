package com.runonce.service.impl.eventversionone;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import com.runonce.service.eventversionone.ApplicationTypeValueService;
import com.runonce.dao.eventversionone.ApplicationTypeValueDao;
import com.runonce.service.AbstractService;
import  com.runonce.model.eventversionone.ApplicationTypeValue;

/**
* 
*
* Created by xuxueli on '2018-12-10 11:31:13'.
*/
@Service
public class ApplicationTypeValueServiceImpl extends AbstractService<ApplicationTypeValue> implements ApplicationTypeValueService  {

	@Resource
	private ApplicationTypeValueDao applicationTypeValueDao;

	
}
