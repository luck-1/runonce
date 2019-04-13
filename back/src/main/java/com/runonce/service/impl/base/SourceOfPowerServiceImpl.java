package com.runonce.service.impl.base;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import com.runonce.service.base.SourceOfPowerService;
import com.runonce.dao.base.SourceOfPowerDao;
import com.runonce.service.AbstractService;
import  com.runonce.model.base.SourceOfPower;

/**
* 
*
* Created by xuxueli on '2018-12-11 09:37:24'.
*/
@Service
public class SourceOfPowerServiceImpl extends AbstractService<SourceOfPower> implements SourceOfPowerService  {

	@Resource
	private SourceOfPowerDao sourceOfPowerDao;

	
}
