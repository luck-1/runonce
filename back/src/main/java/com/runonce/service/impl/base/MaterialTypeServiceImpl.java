package com.runonce.service.impl.base;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import com.runonce.service.base.MaterialTypeService;
import com.runonce.dao.base.MaterialTypeDao;
import com.runonce.service.AbstractService;
import  com.runonce.model.base.MaterialType;

/**
* 
*
* Created by xuxueli on '2018-12-10 14:57:47'.
*/
@Service
public class MaterialTypeServiceImpl extends AbstractService<MaterialType> implements MaterialTypeService  {

	@Resource
	private MaterialTypeDao materialTypeDao;

	
}
