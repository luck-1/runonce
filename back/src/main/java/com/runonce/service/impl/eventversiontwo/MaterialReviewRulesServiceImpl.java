package com.runonce.service.impl.eventversiontwo;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import com.runonce.service.eventversiontwo.MaterialReviewRulesService;
import com.runonce.dao.eventversiontwo.MaterialReviewRulesDao;
import com.runonce.service.AbstractService;
import  com.runonce.model.eventversiontwo.MaterialReviewRules;

/**
* 
*
* Created by xuxueli on '2018-12-10 15:04:12'.
*/
@Service
public class MaterialReviewRulesServiceImpl extends AbstractService<MaterialReviewRules> implements MaterialReviewRulesService  {

	@Resource
	private MaterialReviewRulesDao materialReviewRulesDao;

	
}
