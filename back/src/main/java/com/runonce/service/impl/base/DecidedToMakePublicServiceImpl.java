package com.runonce.service.impl.base;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import com.runonce.service.base.DecidedToMakePublicService;
import com.runonce.dao.base.DecidedToMakePublicDao;
import com.runonce.service.AbstractService;
import  com.runonce.model.base.DecidedToMakePublic;

/**
* 
*
* Created by xuxueli on '2018-12-10 14:57:46'.
*/
@Service
public class DecidedToMakePublicServiceImpl extends AbstractService<DecidedToMakePublic> implements DecidedToMakePublicService  {

	@Resource
	private DecidedToMakePublicDao decidedToMakePublicDao;

	
}
