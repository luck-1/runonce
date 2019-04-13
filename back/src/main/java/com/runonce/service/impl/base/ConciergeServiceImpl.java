package com.runonce.service.impl.base;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import com.runonce.service.base.ConciergeService;
import com.runonce.dao.base.ConciergeDao;
import com.runonce.service.AbstractService;
import  com.runonce.model.base.Concierge;

/**
* 
*
* Created by xuxueli on '2018-12-10 14:57:46'.
*/
@Service
public class ConciergeServiceImpl extends AbstractService<Concierge> implements ConciergeService  {

	@Resource
	private ConciergeDao conciergeDao;

	
}
