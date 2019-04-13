package com.runonce.service.impl.eventversionone;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import com.runonce.service.eventversionone.AuditConfirmationService;
import com.runonce.dao.eventversionone.AuditConfirmationDao;
import com.runonce.service.AbstractService;
import  com.runonce.model.eventversionone.AuditConfirmation;

/**
* 
*
* Created by xuxueli on '2018-12-10 11:31:13'.
*/
@Service
public class AuditConfirmationServiceImpl extends AbstractService<AuditConfirmation> implements AuditConfirmationService  {

	@Resource
	private AuditConfirmationDao auditConfirmationDao;

	
}
