package com.runonce.service.impl.base;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import com.runonce.service.base.HandlingFormService;
import com.runonce.dao.base.HandlingFormDao;
import com.runonce.service.AbstractService;
import  com.runonce.model.base.HandlingForm;

/**
* 
*
* Created by xuxueli on '2018-12-10 14:57:46'.
*/
@Service
public class HandlingFormServiceImpl extends AbstractService<HandlingForm> implements HandlingFormService  {

	@Resource
	private HandlingFormDao handlingFormDao;

	
}
