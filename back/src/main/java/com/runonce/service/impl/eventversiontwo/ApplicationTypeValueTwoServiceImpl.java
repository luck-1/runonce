package com.runonce.service.impl.eventversiontwo;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import com.runonce.service.eventversiontwo.ApplicationTypeValueTwoService;
import com.runonce.dao.eventversiontwo.ApplicationTypeValueTwoDao;
import com.runonce.service.AbstractService;
import  com.runonce.model.eventversiontwo.ApplicationTypeValueTwo;

/**
* 
*
* Created by xuxueli on '2018-12-10 15:04:12'.
*/
@Service
public class ApplicationTypeValueTwoServiceImpl extends AbstractService<ApplicationTypeValueTwo> implements ApplicationTypeValueTwoService  {

	@Resource
	private ApplicationTypeValueTwoDao applicationTypeValueTwoDao;

	
}
