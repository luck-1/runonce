package com.runonce.service.impl.base;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import com.runonce.service.base.OnlineApplicationFormService;
import com.runonce.dao.base.OnlineApplicationFormDao;
import com.runonce.service.AbstractService;
import  com.runonce.model.base.OnlineApplicationForm;

/**
* 
*
* Created by xuxueli on '2018-12-10 14:57:47'.
*/
@Service
public class OnlineApplicationFormServiceImpl extends AbstractService<OnlineApplicationForm> implements OnlineApplicationFormService  {

	@Resource
	private OnlineApplicationFormDao onlineApplicationFormDao;

	
}
