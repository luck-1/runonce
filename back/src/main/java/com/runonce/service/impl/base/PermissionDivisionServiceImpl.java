package com.runonce.service.impl.base;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import com.runonce.service.base.PermissionDivisionService;
import com.runonce.dao.base.PermissionDivisionDao;
import com.runonce.service.AbstractService;
import  com.runonce.model.base.PermissionDivision;

/**
* 
*
* Created by xuxueli on '2018-12-10 14:57:47'.
*/
@Service
public class PermissionDivisionServiceImpl extends AbstractService<PermissionDivision> implements PermissionDivisionService  {

	@Resource
	private PermissionDivisionDao permissionDivisionDao;

	
}
