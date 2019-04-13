package com.runonce.service.impl.base;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import com.runonce.service.base.SourceChannelService;
import com.runonce.dao.base.SourceChannelDao;
import com.runonce.service.AbstractService;
import  com.runonce.model.base.SourceChannel;

/**
* 
*
* Created by xuxueli on '2018-12-10 14:57:47'.
*/
@Service
public class SourceChannelServiceImpl extends AbstractService<SourceChannel> implements SourceChannelService  {

	@Resource
	private SourceChannelDao sourceChannelDao;

	
}
