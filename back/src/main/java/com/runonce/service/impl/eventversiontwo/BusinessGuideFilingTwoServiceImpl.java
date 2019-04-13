package com.runonce.service.impl.eventversiontwo;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import com.runonce.service.eventversiontwo.BusinessGuideFilingTwoService;
import com.runonce.dao.eventversiontwo.BusinessGuideFilingTwoDao;
import com.runonce.service.AbstractService;
import  com.runonce.model.eventversiontwo.BusinessGuideFilingTwo;

/**
* 
*
* Created by xuxueli on '2018-12-10 15:04:12'.
*/
@Service
public class BusinessGuideFilingTwoServiceImpl extends AbstractService<BusinessGuideFilingTwo> implements BusinessGuideFilingTwoService  {

	@Resource
	private BusinessGuideFilingTwoDao businessGuideFilingTwoDao;

	public BusinessGuideFilingTwo findByEventId(String eventId){
		BusinessGuideFilingTwo businessGuideFilingTwo = businessGuideFilingTwoDao.findByEventId(eventId);
		return businessGuideFilingTwo;
	}


	public void deleteByEventId(String eventId) {
		businessGuideFilingTwoDao.deleteByEventId(eventId);
	}
}
