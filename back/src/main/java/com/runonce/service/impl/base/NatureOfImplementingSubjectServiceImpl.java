package com.runonce.service.impl.base;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import com.runonce.service.base.NatureOfImplementingSubjectService;
import com.runonce.dao.base.NatureOfImplementingSubjectDao;
import com.runonce.service.AbstractService;
import  com.runonce.model.base.NatureOfImplementingSubject;

/**
* 
*
* Created by xuxueli on '2018-12-10 14:57:47'.
*/
@Service
public class NatureOfImplementingSubjectServiceImpl extends AbstractService<NatureOfImplementingSubject> implements NatureOfImplementingSubjectService  {

	@Resource
	private NatureOfImplementingSubjectDao natureOfImplementingSubjectDao;

	
}
