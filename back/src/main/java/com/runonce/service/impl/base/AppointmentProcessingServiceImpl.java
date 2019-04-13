package com.runonce.service.impl.base;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import com.runonce.service.base.AppointmentProcessingService;
import com.runonce.dao.base.AppointmentProcessingDao;
import com.runonce.service.AbstractService;
import  com.runonce.model.base.AppointmentProcessing;

/**
* 
*
* Created by xuxueli on '2018-12-10 14:57:46'.
*/
@Service
public class AppointmentProcessingServiceImpl extends AbstractService<AppointmentProcessing> implements AppointmentProcessingService  {

	@Resource
	private AppointmentProcessingDao appointmentProcessingDao;

	
}
