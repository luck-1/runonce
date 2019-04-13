package com.runonce.service.impl.eventversionone;
import javax.annotation.Resource;

import com.runonce.dao.eventversionone.ApplicationTypeValueDao;
import com.runonce.model.eventversionone.ApplicationTypeValue;
import com.runonce.model.http.RelatedMaterialsParam;
import com.runonce.util.SysUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.runonce.service.eventversionone.ApplicationTypeFieldService;
import com.runonce.dao.eventversionone.ApplicationTypeFieldDao;
import com.runonce.service.AbstractService;
import  com.runonce.model.eventversionone.ApplicationTypeField;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
* 
*
* Created by xuxueli on '2018-12-10 11:31:13'.
*/
@Service
public class ApplicationTypeFieldServiceImpl extends AbstractService<ApplicationTypeField> implements ApplicationTypeFieldService  {

	@Resource
	private ApplicationTypeFieldDao applicationTypeFieldDao;

	@Autowired
	private ApplicationTypeFieldServiceImpl applicationTypeFieldService;


//	@Autowired
//	private ApplicationTypeValueServiceImpl applicationTypeValueService;

	@Resource
	private ApplicationTypeValueDao applicationTypeValueDao;


	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveInfo(List<RelatedMaterialsParam> modelList) {
		for(RelatedMaterialsParam model:modelList){
			if(StringUtils.isEmpty(model.getId())){
				// 申请类型字段新增
				model.setId(SysUtil.getUUID());
				mapper.insertSelective(model);

				model.setApplicationTypeFieldId(model.getId());
			}else{
				model.setApplicationTypeFieldId(model.getId());
				mapper.updateByPrimaryKey(model);
			}

			// 申请类型字段关联值新增
			relatedMaterials(model);

		}
	}
	/**
	 * 按主键id删除记录
	 * @param id
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void  deleteById(String id){
		// 申请类型记录删除
		mapper.deleteByPrimaryKey(id);
		// 其下的申请记录删除
		List<ApplicationTypeField> fields = applicationTypeFieldDao.selectByParentId(id);
		if(fields != null && fields.size() >0){
			for(ApplicationTypeField field:fields){
				mapper.deleteByPrimaryKey(field.getId());
			}
		}
	}

	/**
	 * 申请类型关联材料
	 * @param relatedMaterialsParam
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void relatedMaterials(RelatedMaterialsParam relatedMaterialsParam){
		// 删除该类型关联
		List<ApplicationTypeValue> valueList =applicationTypeValueDao.selectByFieldId(relatedMaterialsParam.getApplicationTypeFieldId());

		if(valueList != null){
			for(ApplicationTypeValue value:valueList){
//				applicationTypeValueService.deleteById(value.getId());
				applicationTypeValueDao.deleteByPrimaryKey(value.getId());
			}
		}
		//新增关联
		if(relatedMaterialsParam.getMaterialId() != null &&  relatedMaterialsParam.getMaterialId().size() > 0){
			for(String materialId:relatedMaterialsParam.getMaterialId()){
				ApplicationTypeValue value = new ApplicationTypeValue();
				value.setId(SysUtil.getUUID());
				value.setMateriaId(materialId);
				value.setFieldId(relatedMaterialsParam.getApplicationTypeFieldId());
				value.setValue(true);
//				applicationTypeValueService.save(value);
				applicationTypeValueDao.insert(value);
			}
		}
	}
}
