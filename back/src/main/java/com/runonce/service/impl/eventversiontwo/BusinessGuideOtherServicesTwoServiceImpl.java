package com.runonce.service.impl.eventversiontwo;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import com.runonce.service.eventversiontwo.BusinessGuideOtherServicesTwoService;
import com.runonce.dao.eventversiontwo.BusinessGuideOtherServicesTwoDao;
import com.runonce.service.AbstractService;
import  com.runonce.model.eventversiontwo.BusinessGuideOtherServicesTwo;

/**
* 
*
* Created by xuxueli on '2018-12-10 15:04:12'.
*/
@Service
public class BusinessGuideOtherServicesTwoServiceImpl extends AbstractService<BusinessGuideOtherServicesTwo> implements BusinessGuideOtherServicesTwoService  {

	@Resource
	private BusinessGuideOtherServicesTwoDao businessGuideOtherServicesTwoDao;

	public BusinessGuideOtherServicesTwo findByEventId(String eventId){
		BusinessGuideOtherServicesTwo businessGuideFilingTwo = businessGuideOtherServicesTwoDao.findByEventId(eventId);
		return businessGuideFilingTwo;
	}

	public void deleteByEventId(String eventId){
		businessGuideOtherServicesTwoDao.deleteByEventId(eventId);
	}
}
