package com.runonce.service.impl.eventversionone;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import com.runonce.service.eventversionone.ApprovalrecordService;
import com.runonce.dao.eventversionone.ApprovalrecordDao;
import com.runonce.service.AbstractService;
import  com.runonce.model.eventversionone.Approvalrecord;

/**
* 审批记录
*
* Created by xuxueli on '2019-01-22 09:46:27'.
*/
@Service
public class ApprovalrecordServiceImpl extends AbstractService<Approvalrecord> implements ApprovalrecordService  {

	@Resource
	private ApprovalrecordDao approvalrecordDao;

	
}
