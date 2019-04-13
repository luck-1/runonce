package com.runonce.service.impl.eventversiontwo;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import com.runonce.service.eventversiontwo.AuditConfirmationTwoService;
import com.runonce.dao.eventversiontwo.AuditConfirmationTwoDao;
import com.runonce.service.AbstractService;
import  com.runonce.model.eventversiontwo.AuditConfirmationTwo;

/**
* 
*
* Created by xuxueli on '2018-12-10 15:04:12'.
*/
@Service
public class AuditConfirmationTwoServiceImpl extends AbstractService<AuditConfirmationTwo> implements AuditConfirmationTwoService  {

	@Resource
	private AuditConfirmationTwoDao auditConfirmationTwoDao;

	
}
