package com.runonce.service.impl.eventversionone;

import javax.annotation.Resource;

import com.runonce.model.eventversionone.ApplicationTypeField;
import org.springframework.stereotype.Service;
import com.runonce.service.eventversionone.SituationChildAndDmModelService;
import com.runonce.dao.eventversionone.SituationChildAndDmModelDao;
import com.runonce.service.AbstractService;
import com.runonce.model.eventversionone.SituationChildAndDmModel;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

/**
 * @author lxc
 * @date 2019-03-01 14:39:25
 * @description 情形引导子项
 */
@Service
public class SituationChildAndDmModelServiceImpl extends AbstractService<SituationChildAndDmModel> implements SituationChildAndDmModelService {

	@Resource
	private SituationChildAndDmModelDao situationChildAndDmModelDao;


	@Override
	public void deleteBySituationChildId(String situationChildId) {

		//删除原有的
		Condition fieldCondition = new Condition(SituationChildAndDmModel.class);
		Example.Criteria fieldCriteria = fieldCondition.createCriteria();
		fieldCriteria.andEqualTo("situationChildId", situationChildId);
		//删除中间表数据
		situationChildAndDmModelDao.deleteByCondition(fieldCondition);

	}
}