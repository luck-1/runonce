package com.runonce.service.impl.eventversiontwo;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import com.runonce.service.eventversiontwo.BusinessGuideAuditAndForwardingCategoryTwoService;
import com.runonce.dao.eventversiontwo.BusinessGuideAuditAndForwardingCategoryTwoDao;
import com.runonce.service.AbstractService;
import  com.runonce.model.eventversiontwo.BusinessGuideAuditAndForwardingCategoryTwo;

/**
* 
*
* Created by xuxueli on '2018-12-10 15:04:12'.
*/
@Service
public class BusinessGuideAuditAndForwardingCategoryTwoServiceImpl extends AbstractService<BusinessGuideAuditAndForwardingCategoryTwo> implements BusinessGuideAuditAndForwardingCategoryTwoService  {

	@Resource
	private BusinessGuideAuditAndForwardingCategoryTwoDao businessGuideAuditAndForwardingCategoryTwoDao;

	public BusinessGuideAuditAndForwardingCategoryTwo findByEventId(String eventId){
		BusinessGuideAuditAndForwardingCategoryTwo businessGuideAuditAndForwardingCategoryTwo = businessGuideAuditAndForwardingCategoryTwoDao.findByEventId(eventId);
		return businessGuideAuditAndForwardingCategoryTwo;
	}

	public void deleteByEventId(String eventId){
		businessGuideAuditAndForwardingCategoryTwoDao.deleteByEventId(eventId);
	}


}
