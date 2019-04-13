package com.runonce.service.impl.base;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import com.runonce.service.base.TypeOfOfficeService;
import com.runonce.dao.base.TypeOfOfficeDao;
import com.runonce.service.AbstractService;
import  com.runonce.model.base.TypeOfOffice;

/**
* 
*
* Created by xuxueli on '2018-12-11 09:35:26'.
*/
@Service
public class TypeOfOfficeServiceImpl extends AbstractService<TypeOfOffice> implements TypeOfOfficeService  {

	@Resource
	private TypeOfOfficeDao typeOfOfficeDao;

	
}
