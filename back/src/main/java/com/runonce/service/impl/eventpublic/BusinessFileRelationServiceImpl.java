package com.runonce.service.impl.eventpublic;
import javax.annotation.Resource;

import com.runonce.ReturnCode;
import com.runonce.util.SysUtil;
import org.springframework.stereotype.Service;
import com.runonce.service.eventpublic.BusinessFileRelationService;
import com.runonce.dao.eventpublic.BusinessFileRelationDao;
import com.runonce.service.AbstractService;
import  com.runonce.model.eventpublic.BusinessFileRelation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.util.List;


/**
* 
*
* Created by xuxueli on '2018-12-11 09:36:41'.
*/
@Service
public class BusinessFileRelationServiceImpl extends AbstractService<BusinessFileRelation> implements BusinessFileRelationService  {

	@Resource
	private BusinessFileRelationDao businessFileRelationDao;

	@Transactional(rollbackFor = Exception.class)
	public void save(BusinessFileRelation model) {
//		BusinessFileRelation item = findBy("businessId",model.getBusinessId());
		Condition condition = new Condition(BusinessFileRelation.class);
		Example.Criteria criteria = condition.createCriteria();
		criteria.andEqualTo("businessId",model.getBusinessId());
		List<BusinessFileRelation> files = findByCondition(condition);
		if(files != null){
			for(BusinessFileRelation item:files){
				mapper.deleteByPrimaryKey(item.getId());
			}
		}
		model.setId(SysUtil.getUUID());
		mapper.insertSelective(model);
    }
}

