package com.runonce.service.impl.eventversionone;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.runonce.ReturnCode;
import com.runonce.dao.eventpublic.BusinessFileRelationDao;
import com.runonce.dao.eventversionone.DepartmentalMattersDao;
import com.runonce.dao.eventversionone.VersionmanagementDao;
import com.runonce.exception.CustomException;
import com.runonce.model.eventpublic.BusinessFileRelation;
import com.runonce.model.eventversionone.AuditRules;
import com.runonce.model.eventversionone.DepartmentalMatters;
import com.runonce.model.eventversionone.MaterialGrouping;
import com.runonce.model.http.MaterialGroupAddParam;
import com.runonce.model.http.MaterialGroupParam;
import com.runonce.model.http.MaterialGroupVersionParam;
import com.runonce.service.eventversionone.AuditRulesService;
import com.runonce.service.impl.eventpublic.BusinessFileRelationServiceImpl;
import com.runonce.util.SysUtil;
import com.runonce.util.WebTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.runonce.service.eventversionone.MaterialGroupingService;
import com.runonce.dao.eventversionone.MaterialGroupingDao;
import com.runonce.service.AbstractService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

/**
* 
*
* Created by xuxueli on '2018-12-10 11:31:13'.
*/
@Service
public class MaterialGroupingServiceImpl extends AbstractService<MaterialGroupAddParam> implements MaterialGroupingService  {

	@Resource
	private MaterialGroupingDao materialGroupingDao;

    @Autowired
	private BusinessFileRelationServiceImpl fileService;
	@Resource
	private BusinessFileRelationDao businessFileRelationDao;

	@Autowired
	private DepartmentalMattersServiceImpl departmentalMattersService;
	@Autowired
	private AuditRulesService auditRulesService;

	@Autowired
	private AuditRulesServiceImpl auditRulesServiceImpl;

	@Resource
	private VersionmanagementDao versionmanagementDao;

	@Resource
	private DepartmentalMattersDao departmentalMattersDao;
	/**
	 * 保存材料记录
	 * @param materialGroupParam 材料对象
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public ReturnCode saveInfo(MaterialGroupParam materialGroupParam,HttpServletRequest request) {
		// 事项存在校验
		DepartmentalMatters event = departmentalMattersService.findById(materialGroupParam.getEventId());
		if(event == null){
			return  new ReturnCode.Builder().failed().msg("该事项不存在!").object(materialGroupParam).build();
		}
		Condition condition = new Condition(MaterialGroupAddParam.class);
		Example.Criteria criteria = condition.createCriteria();
		criteria.andEqualTo("eventId",materialGroupParam.getEventId());
		mapper.deleteByCondition(condition);
		//数据库中存储的版本号
		Integer version = versionmanagementDao.getSheetVersion(materialGroupParam.getEventId(),2);
		if(version == null ){
			throw new CustomException(new ReturnCode.Builder().failed().msg("未知错误：未查到当前版本！").build());
		}
		//前端得到的版本号（点击配置按钮时的版本）
		Integer version1 = materialGroupParam.getVersion();
		//版本号相同，修改，不相同回滚
		if(version1.equals(version)){
			// 参数整理
			if(materialGroupParam.getMaterialGroupingParamList() != null && materialGroupParam.getMaterialGroupingParamList().size() > 0){
				for(MaterialGroupAddParam model:materialGroupParam.getMaterialGroupingParamList()){

					//model
					String thatId=model.getId();
					String thisId;
					if(thatId!=null) {
						auditRulesService.deleteByMaterialId(thatId);
						thisId=model.getId();
					}else{
						thisId=SysUtil.getUUID();
					}


					List<AuditRules> approval=model.getApproval();
					List<AuditRules> acceptance=model.getAcceptance();

					//保存审批人员审核要点
					if(approval != null && approval.size() > 0){
						for (AuditRules  oneApproval:approval ) {
							oneApproval.setMaterialId(thisId);
							oneApproval.setId(SysUtil.getUUID());
							auditRulesService.save(oneApproval);
						}
					}

					//保存受理人员审核要点
					if(acceptance != null && acceptance.size() > 0){
						for (AuditRules  oneAcceptance:acceptance ) {
							oneAcceptance.setMaterialId(thisId);
							oneAcceptance.setId(SysUtil.getUUID());
							auditRulesService.save(oneAcceptance);
						}
					}
					model.setId(thisId);
					mapper.insert(model);
				}
			}
		}else{
			throw new CustomException(new ReturnCode.Builder().failed().msg("当前已有人在编辑，更改失败！").build());
		}
		//更新数据库中的版本号
		versionmanagementDao.updateSheetVersion(materialGroupParam.getEventId(),2);
		//更新修改用户
		event.setOperator(WebTokenUtil.getUserByWebToken(request));
		// 更新事项状态
		event.setMaterialGroupingState(materialGroupParam.getState());
		departmentalMattersService.update(event);

		//获取事项id 是否有父
		String departmentalMattersPId = departmentalMattersDao.selectPidById(event.getId());
		//更新父部门状态
		departmentalMattersService.updateFatherState(departmentalMattersPId);
		return  new ReturnCode.Builder().success().msg("材料分组保存成功").object(materialGroupParam).build();
    }

	/**
	 * 更新材料记录
	 * @param model 材料对象
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void update (MaterialGroupAddParam model) {
		// 原关联文件删除
//		List<BusinessFileRelation> files = businessFileRelationDao.selectByBusinessId(model.getId());
//		if(files != null && files.size() > 0){
//			for(BusinessFileRelation file:files){
//				fileService.deleteById(file.getId());
//			}
//		}
//		List<BusinessFileRelation> fileList = new ArrayList<>();
//		if(model.getSampleTableMap() != null){
//			for(BusinessFileRelation file:model.getSampleTableMap()){
//				file.setId(SysUtil.getUUID());
//				file.setBusinessId(model.getId());
//				file.setBusinessType(1);
//				fileList.add(file);
//			}
//		}
//		if(model.getEmptyTableMap() != null){
//			for(BusinessFileRelation file:model.getEmptyTableMap()){
//				file.setId(SysUtil.getUUID());
//				file.setBusinessId(model.getId());
//				file.setBusinessType(2);
//				fileList.add(file);
//			}
//		}
//		// 文件关联新增
//		for(BusinessFileRelation file:fileList){
//			fileService.save(file);
//		}
		mapper.updateByPrimaryKey(model);
	}

	@Override
	public MaterialGroupAddParam findById(String id){
		MaterialGroupAddParam result = mapper.selectByPrimaryKey(id);
//		List<BusinessFileRelation> sampleTableMap = businessFileRelationDao.selectByBusinessIdAndType(id,1);
//		List<BusinessFileRelation> emptyTableMap = businessFileRelationDao.selectByBusinessIdAndType(id,2);
//		result.setEmptyTableMap(emptyTableMap);
//		result.setSampleTableMap(sampleTableMap);
		return result;
	}

	@Override
	public List<MaterialGroupAddParam> findAll(){
		List<MaterialGroupAddParam> materials = mapper.selectAll();
//		if(materials != null && materials.size() > 0){
//			for(MaterialGroupAddParam material:materials){
//				List<BusinessFileRelation> sampleTableMap = businessFileRelationDao.selectByBusinessIdAndType(material.getId(),1);
//				List<BusinessFileRelation> emptyTableMap = businessFileRelationDao.selectByBusinessIdAndType(material.getId(),2);
//				material.setEmptyTableMap(emptyTableMap);
//				material.setSampleTableMap(sampleTableMap);
//			}
//		}

		return materials;
	}

	@Override
	public MaterialGroupVersionParam selectByEventId(String eventId){

		MaterialGroupVersionParam materialGroupVersionParam = new MaterialGroupVersionParam();
		Integer version = versionmanagementDao.getSheetVersion(eventId,2);
		if(version == null ){
			throw new CustomException(new ReturnCode.Builder().failed().msg("未查到当前版本号，未知错误！").build());
		}
		materialGroupVersionParam.setVersion(version);

		Condition condition = new Condition(MaterialGroupAddParam.class);
		Example.Criteria criteria = condition.createCriteria();
		criteria.andEqualTo("eventId",eventId);
		condition.setOrderByClause("orderNum");
		List<MaterialGroupAddParam> materials = findByCondition(condition);
		if(materials == null || materials.isEmpty()){
			return materialGroupVersionParam;
		}
		for(MaterialGroupAddParam item:materials){
			item.setAcceptance(auditRulesService.selectByMaterialId(item.getId(),1));
			item.setApproval(auditRulesService.selectByMaterialId(item.getId(),2));
		}
		materialGroupVersionParam.setList(materials);
//		if(materials != null && materials.size() > 0){
//			for(MaterialGroupAddParam material:materials){
//				List<BusinessFileRelation> sampleTableMap = businessFileRelationDao.selectByBusinessIdAndType(material.getId(),1);
//				List<BusinessFileRelation> emptyTableMap = businessFileRelationDao.selectByBusinessIdAndType(material.getId(),2);
//				material.setEmptyTableMap(emptyTableMap);
//				material.setSampleTableMap(sampleTableMap);
//			}
//		}
		return materialGroupVersionParam;
	}


	@Override
	public List<MaterialGroupAddParam> selectByEventIdAndIo(String eventId,String io){

//		Condition condition = new Condition(MaterialGroupAddParam.class);
//		Example.Criteria criteria = condition.createCriteria();
//		criteria.andEqualTo("eventId",eventId);
//		criteria.andEqualTo("io",io);
//		condition.setOrderByClause("orderNum");
		List<MaterialGroupAddParam> materials = materialGroupingDao.selectByEventIdAndIo( eventId,io );
		if(materials == null || materials.isEmpty()){
			return new ArrayList<>();
		}
		for(MaterialGroupAddParam item:materials){
			item.setAcceptance(auditRulesService.selectByMaterialId(item.getId(),1));
			item.setApproval(auditRulesService.selectByMaterialId(item.getId(),2));
		}
		return materials;
	}

	@Override
	public List<MaterialGroupAddParam> selectByEventIdOrderByType(String eventId){
		Condition condition = new Condition(MaterialGroupAddParam.class);
		Example.Criteria criteria = condition.createCriteria();
		criteria.andEqualTo("eventId",eventId);
		condition.setOrderByClause("type");
		List<MaterialGroupAddParam> materials = findByCondition(condition);
		if(materials.isEmpty()){
			return materials;
		}
		for(MaterialGroupAddParam item:materials){
			item.setAcceptance(auditRulesService.selectByMaterialId(item.getId(),1));
			item.setApproval(auditRulesService.selectByMaterialId(item.getId(),2));
		}
//		if(materials != null && materials.size() > 0){
//			for(MaterialGroupAddParam material:materials){
//				List<BusinessFileRelation> sampleTableMap = businessFileRelationDao.selectByBusinessIdAndType(material.getId(),1);
//				List<BusinessFileRelation> emptyTableMap = businessFileRelationDao.selectByBusinessIdAndType(material.getId(),2);
//				material.setEmptyTableMap(emptyTableMap);
//				material.setSampleTableMap(sampleTableMap);
//			}
//		}
		return materials;
	}
}
