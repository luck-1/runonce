package com.runonce.service.impl.eventversiontwo;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import com.runonce.service.eventversiontwo.BusinessGuideAdministrativeLicensingTwoService;
import com.runonce.dao.eventversiontwo.BusinessGuideAdministrativeLicensingTwoDao;
import com.runonce.service.AbstractService;
import  com.runonce.model.eventversiontwo.BusinessGuideAdministrativeLicensingTwo;

/**
*
*
* Created by xuxueli on '2018-12-10 15:04:12'.
*/
@Service
public class BusinessGuideAdministrativeLicensingTwoServiceImpl extends AbstractService<BusinessGuideAdministrativeLicensingTwo> implements BusinessGuideAdministrativeLicensingTwoService  {

	@Resource
	private BusinessGuideAdministrativeLicensingTwoDao businessGuideAdministrativeLicensingTwoDao;

	public BusinessGuideAdministrativeLicensingTwo findByEventId(String eventId){
		BusinessGuideAdministrativeLicensingTwo businessGuideAdministrativeLicensingTwo = businessGuideAdministrativeLicensingTwoDao.findByEventId(eventId);
		return businessGuideAdministrativeLicensingTwo;
	}

	public void deleteByEventId(String eventId){
		businessGuideAdministrativeLicensingTwoDao.deleteByEventId(eventId);
	}

}
