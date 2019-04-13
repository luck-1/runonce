package com.runonce.service.impl.base;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import com.runonce.service.base.ChannelDescriptionService;
import com.runonce.dao.base.ChannelDescriptionDao;
import com.runonce.service.AbstractService;
import  com.runonce.model.base.ChannelDescription;

/**
* 
*
* Created by xuxueli on '2018-12-10 14:57:46'.
*/
@Service
public class ChannelDescriptionServiceImpl extends AbstractService<ChannelDescription> implements ChannelDescriptionService  {

	@Resource
	private ChannelDescriptionDao channelDescriptionDao;

	
}
