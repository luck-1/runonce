package com.runonce.service.impl.eventversionone;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import com.runonce.service.eventversionone.AuditRulesService;
import com.runonce.dao.eventversionone.AuditRulesDao;
import com.runonce.service.AbstractService;
import  com.runonce.model.eventversionone.AuditRules;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
* 审核细则
*
* Created by xuxueli on '2018-12-18 12:32:51'.
*/
@Service
public class AuditRulesServiceImpl extends AbstractService<AuditRules> implements AuditRulesService  {

	@Resource
	private AuditRulesDao auditRulesDao;



	@Transactional(rollbackFor = Exception.class)
	public  void deleteByMaterialId(String MaterialId){

		auditRulesDao.deleteByMaterialId(MaterialId);

	}

	/**
	 * 根据材料名称和类型查询审核细则
	 * @param MaterialId
	 * @param type
	 * @return
	 */
	@Override
	public List<AuditRules> selectByMaterialId(String MaterialId, Integer type){

		return auditRulesDao.selectByMaterialId(MaterialId,type);

	}

	
}
