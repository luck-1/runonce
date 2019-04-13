package com.runonce.service.impl.base;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import com.runonce.service.base.EventStarService;
import com.runonce.dao.base.EventStarDao;
import com.runonce.service.AbstractService;
import  com.runonce.model.base.EventStar;

/**
* 
*
* Created by xuxueli on '2018-12-10 14:57:46'.
*/
@Service
public class EventStarServiceImpl extends AbstractService<EventStar> implements EventStarService  {

	@Resource
	private EventStarDao eventStarDao;

	
}
