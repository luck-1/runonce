package com.runonce.service.impl.base;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import com.runonce.service.base.AnnualReviewOrViewService;
import com.runonce.dao.base.AnnualReviewOrViewDao;
import com.runonce.service.AbstractService;
import  com.runonce.model.base.AnnualReviewOrView;

/**
* 
*
* Created by xuxueli on '2018-12-10 14:57:46'.
*/
@Service
public class AnnualReviewOrViewServiceImpl extends AbstractService<AnnualReviewOrView> implements AnnualReviewOrViewService  {

	@Resource
	private AnnualReviewOrViewDao annualReviewOrViewDao;

	
}
