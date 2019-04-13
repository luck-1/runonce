package com.runonce.service.impl.base;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import com.runonce.service.base.MaterialSpecificationService;
import com.runonce.dao.base.MaterialSpecificationDao;
import com.runonce.service.AbstractService;
import  com.runonce.model.base.MaterialSpecification;

/**
* 
*
* Created by xuxueli on '2018-12-10 14:57:47'.
*/
@Service
public class MaterialSpecificationServiceImpl extends AbstractService<MaterialSpecification> implements MaterialSpecificationService  {

	@Resource
	private MaterialSpecificationDao materialSpecificationDao;

	
}
