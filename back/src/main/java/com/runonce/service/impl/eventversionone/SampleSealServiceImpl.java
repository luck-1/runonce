package com.runonce.service.impl.eventversionone;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import com.runonce.service.eventversionone.SampleSealService;
import com.runonce.dao.eventversionone.SampleSealDao;
import com.runonce.service.AbstractService;
import  com.runonce.model.eventversionone.SampleSeal;

/**
* 
*
* Created by xuxueli on '2018-12-11 09:31:17'.
*/
@Service
public class SampleSealServiceImpl extends AbstractService<SampleSeal> implements SampleSealService  {

	@Resource
	private SampleSealDao sampleSealDao;

	
}
