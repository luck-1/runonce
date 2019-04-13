package com.runonce.service.impl.eventversiontwo;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import com.runonce.service.eventversiontwo.FillInFormFieldsService;
import com.runonce.dao.eventversiontwo.FillInFormFieldsDao;
import com.runonce.service.AbstractService;
import  com.runonce.model.eventversiontwo.FillInFormFields;

/**
* 
*
* Created by xuxueli on '2018-12-10 15:04:12'.
*/
@Service
public class FillInFormFieldsServiceImpl extends AbstractService<FillInFormFields> implements FillInFormFieldsService  {

	@Resource
	private FillInFormFieldsDao fillInFormFieldsDao;

	
}
