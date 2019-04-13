package com.runonce.service.impl.base;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import com.runonce.service.base.ScopeOfOperationService;
import com.runonce.dao.base.ScopeOfOperationDao;
import com.runonce.service.AbstractService;
import  com.runonce.model.base.ScopeOfOperation;

/**
* 
*
* Created by xuxueli on '2018-12-10 14:57:47'.
*/
@Service
public class ScopeOfOperationServiceImpl extends AbstractService<ScopeOfOperation> implements ScopeOfOperationService  {

	@Resource
	private ScopeOfOperationDao scopeOfOperationDao;

	
}
