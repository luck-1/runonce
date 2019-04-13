package com.runonce.service.impl.eventversiontwo;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import com.runonce.service.eventversiontwo.MaterialReviewRulesContentService;
import com.runonce.dao.eventversiontwo.MaterialReviewRulesContentDao;
import com.runonce.service.AbstractService;
import  com.runonce.model.eventversiontwo.MaterialReviewRulesContent;

/**
* 
*
* Created by xuxueli on '2018-12-10 15:04:12'.
*/
@Service
public class MaterialReviewRulesContentServiceImpl extends AbstractService<MaterialReviewRulesContent> implements MaterialReviewRulesContentService  {

	@Resource
	private MaterialReviewRulesContentDao materialReviewRulesContentDao;

	
}
