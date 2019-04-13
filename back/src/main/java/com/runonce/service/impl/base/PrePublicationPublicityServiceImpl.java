package com.runonce.service.impl.base;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import com.runonce.service.base.PrePublicationPublicityService;
import com.runonce.dao.base.PrePublicationPublicityDao;
import com.runonce.service.AbstractService;
import  com.runonce.model.base.PrePublicationPublicity;

/**
* 
*
* Created by xuxueli on '2018-12-10 14:57:47'.
*/
@Service
public class PrePublicationPublicityServiceImpl extends AbstractService<PrePublicationPublicity> implements PrePublicationPublicityService  {

	@Resource
	private PrePublicationPublicityDao prePublicationPublicityDao;

	
}
