package com.runonce.service.impl.eventversiontwo;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import com.runonce.service.eventversiontwo.ApplicationTypeFieldTwoService;
import com.runonce.dao.eventversiontwo.ApplicationTypeFieldTwoDao;
import com.runonce.service.AbstractService;
import  com.runonce.model.eventversiontwo.ApplicationTypeFieldTwo;

/**
* 
*
* Created by xuxueli on '2018-12-10 15:04:12'.
*/
@Service
public class ApplicationTypeFieldTwoServiceImpl extends AbstractService<ApplicationTypeFieldTwo> implements ApplicationTypeFieldTwoService  {

	@Resource
	private ApplicationTypeFieldTwoDao applicationTypeFieldTwoDao;

	
}
