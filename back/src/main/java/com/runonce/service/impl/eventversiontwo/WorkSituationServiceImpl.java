package com.runonce.service.impl.eventversiontwo;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import com.runonce.service.eventversiontwo.WorkSituationService;
import com.runonce.dao.eventversiontwo.WorkSituationDao;
import com.runonce.service.AbstractService;
import  com.runonce.model.eventversiontwo.WorkSituation;

/**
* 
*
* Created by xuxueli on '2018-12-11 09:33:33'.
*/
@Service
public class WorkSituationServiceImpl extends AbstractService<WorkSituation> implements WorkSituationService  {

	@Resource
	private WorkSituationDao workSituationDao;

	
}
