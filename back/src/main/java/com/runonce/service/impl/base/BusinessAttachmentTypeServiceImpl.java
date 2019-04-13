package com.runonce.service.impl.base;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import com.runonce.service.base.BusinessAttachmentTypeService;
import com.runonce.dao.base.BusinessAttachmentTypeDao;
import com.runonce.service.AbstractService;
import  com.runonce.model.base.BusinessAttachmentType;

/**
* 
*
* Created by xuxueli on '2018-12-10 14:57:46'.
*/
@Service
public class BusinessAttachmentTypeServiceImpl extends AbstractService<BusinessAttachmentType> implements BusinessAttachmentTypeService  {

	@Resource
	private BusinessAttachmentTypeDao businessAttachmentTypeDao;

	
}
