package com.runonce.service.impl.base;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import com.runonce.service.base.IsLogisticsService;
import com.runonce.dao.base.IsLogisticsDao;
import com.runonce.service.AbstractService;
import  com.runonce.model.base.IsLogistics;

/**
* 
*
* Created by xuxueli on '2018-12-10 14:57:47'.
*/
@Service
public class IsLogisticsServiceImpl extends AbstractService<IsLogistics> implements IsLogisticsService  {

	@Resource
	private IsLogisticsDao isLogisticsDao;

	
}
