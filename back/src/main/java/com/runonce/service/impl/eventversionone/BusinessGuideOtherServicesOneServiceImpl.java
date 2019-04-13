package com.runonce.service.impl.eventversionone;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.runonce.ReturnCode;
import com.runonce.dao.base.CatalogueEventTypeDao;
import com.runonce.dao.base.ServiceObjectDao;
import com.runonce.dao.eventpublic.EventsTableDao;
import com.runonce.dao.eventversionone.BusinessGuideOtherServicesOnePrivateDao;
import com.runonce.dao.eventversionone.DepartmentalMattersDao;
import com.runonce.dao.eventversionone.VersionmanagementDao;
import com.runonce.exception.CustomException;
import com.runonce.httpbean.assets.reqbean.BusinessGuideOtherServicesOneReq;
import com.runonce.model.base.ServiceBusinessguideiModle;
import com.runonce.model.base.ServiceObject;
import com.runonce.model.eventpublic.EventsTable;
import com.runonce.model.eventversionone.BusinessGuideOtherServicesOnePrivate;
import com.runonce.model.eventversionone.DepartmentalMatters;
import com.runonce.model.http.EventsTableParam;
import com.runonce.service.base.ServiceBusinessguideiModleService;
import com.runonce.service.eventversionone.BusinessGuideAdministrativeLicensingOneService;
import com.runonce.service.eventversionone.DepartmentalMattersService;
import com.runonce.util.SysUtil;
import com.runonce.util.WebTokenUtil;
import org.springframework.stereotype.Service;
import com.runonce.service.eventversionone.BusinessGuideOtherServicesOneService;
import com.runonce.dao.eventversionone.BusinessGuideOtherServicesOneDao;
import com.runonce.service.AbstractService;
import  com.runonce.model.eventversionone.BusinessGuideOtherServicesOne;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 *
 * Created by xuxueli on '2018-12-10 11:31:13'.
 */
@Service
public class BusinessGuideOtherServicesOneServiceImpl extends AbstractService<BusinessGuideOtherServicesOne> implements BusinessGuideOtherServicesOneService  {

	@Resource
	private BusinessGuideOtherServicesOneDao businessGuideOtherServicesOneDao;

	@Resource
	private DepartmentalMattersDao departmentalMattersDao;

	@Resource
	private DepartmentalMattersService departmentalMattersService;

	@Resource
	private BusinessGuideOtherServicesOnePrivateDao businessGuideOtherServicesOnePrivateDao;

	@Resource
	private VersionmanagementDao versionmanagementDao;

	/**
	 * 	从事件ID查询
	 * @param eventId
	 */
	@Override
	public BusinessGuideOtherServicesOneReq findByEventId(String eventId){

		BusinessGuideOtherServicesOneReq businessGuideOtherServicesOneReq = businessGuideOtherServicesOneDao.findByEventId(eventId);
		if(businessGuideOtherServicesOneReq == null){
			return null;
		}
		Integer version = versionmanagementDao.getSheetVersion(eventId,1);
		if(version == null ){
			throw new CustomException(new ReturnCode.Builder().failed().msg("未知错误：未查到当前版本！").build());
		}

		BusinessGuideOtherServicesOnePrivate businessGuideOtherServicesOnePrivate = businessGuideOtherServicesOneReq.getBusinessGuideOtherServicesOnePrivate();
		if(businessGuideOtherServicesOnePrivate == null){
			businessGuideOtherServicesOnePrivate = new BusinessGuideOtherServicesOnePrivate();
		}
		businessGuideOtherServicesOnePrivate.setVersion(version);

		return businessGuideOtherServicesOneReq;
	}

	/**
	 * 	从事件ID删除
	 * @param eventId
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteByEventId(String eventId){
		businessGuideOtherServicesOneDao.deleteByEventId(eventId);
	}

	/**
	 * 	保存
	 * @param businessGuideOtherServicesOne
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public ReturnCode add(BusinessGuideOtherServicesOne  businessGuideOtherServicesOne,BusinessGuideOtherServicesOnePrivate businessGuideOtherServicesOnePrivate, HttpServletRequest request) {
		//获得对象数据
		String id = businessGuideOtherServicesOne.getId();
		String eventId = businessGuideOtherServicesOne.getEventId();

		//是否收费和（收费标准，收费依据，支付方式）对应关系
		if(businessGuideOtherServicesOne.getCharge() != null ){
			if("否".equals(businessGuideOtherServicesOne.getCharge())){
				businessGuideOtherServicesOne.setChargingStandard(null);
				businessGuideOtherServicesOne.setChargingBasis(null);
				businessGuideOtherServicesOne.setPaymentMethod(null);
			}
		}

		//是否有结果样本与（结果样本）对应关系
		if(businessGuideOtherServicesOne.getHavaResultSample() != null ){
			if("否".equals(businessGuideOtherServicesOne.getHavaResultSample())){
				businessGuideOtherServicesOne.setResultSample(null);
			}
		}

		//判断ID是否存在，存在更新，不存在新增
		if(id == null || "".equals(id)){
			//是否存在这个事件的ID，测试忘传ID情况BUG，存在则删除
			BusinessGuideOtherServicesOneReq businessGuideOtherServicesOneReq = businessGuideOtherServicesOneDao.findByEventId(eventId);
			if(businessGuideOtherServicesOneReq != null ){
				businessGuideOtherServicesOneDao.deleteByEventId(eventId);
			}
			businessGuideOtherServicesOne.setId(SysUtil.getUUID());
			//此事件ID第一次请求，新增数据
			businessGuideOtherServicesOneDao.insert( businessGuideOtherServicesOne);
		}else{
			//正常带ID ，更改
			businessGuideOtherServicesOneDao.updateByPrimaryKey( businessGuideOtherServicesOne);
		}
		//保存Private表信息
		savePrivate(businessGuideOtherServicesOnePrivate,request);

		return new ReturnCode.Builder().success().msg("办事指南保存成功").object(businessGuideOtherServicesOne).build();
	}

	/**
	 * 保存Private表信息
	 * @param businessGuideOtherServicesOnePrivate
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void savePrivate(BusinessGuideOtherServicesOnePrivate businessGuideOtherServicesOnePrivate, HttpServletRequest request) {

		String id = businessGuideOtherServicesOnePrivate.getDepartmentalMattersId();
		BusinessGuideOtherServicesOnePrivate selectBusinessGuideOtherServicesOnePrivate = businessGuideOtherServicesOnePrivateDao.selectByPrimaryKey(id);

		//事项取消分配情况（）
		DepartmentalMatters departmentalMatters = departmentalMattersDao.findState(id);
		if (departmentalMatters == null) {
			throw new CustomException(new ReturnCode.Builder().failed().msg("该事项不存在").build());
		}

		//数据库中存储的版本号
		Integer version = versionmanagementDao.getSheetVersion(id,1);
		if (selectBusinessGuideOtherServicesOnePrivate == null) {
			//新增
			businessGuideOtherServicesOnePrivateDao.insert(businessGuideOtherServicesOnePrivate);
		} else {
			//前端得到的版本号（点击配置按钮时的版本）
			Integer version1 = businessGuideOtherServicesOnePrivate.getVersion();
			if(version1 == null ){
				version1 = version;
			}
			//版本号相同，修改，不相同回滚
			if(version1.equals(version)){
				//修改
				businessGuideOtherServicesOnePrivateDao.updateByPrimaryKey(businessGuideOtherServicesOnePrivate);
			}else{
				throw new CustomException(new ReturnCode.Builder().failed().msg("当前已有人在编辑，更改失败！").build());
			}
		}
		//更新办事指南状态
		departmentalMatters.setBusinessGuideState(businessGuideOtherServicesOnePrivate.getState());
		//更新修改用户
		departmentalMatters.setOperator(WebTokenUtil.getUserByWebToken(request));
		departmentalMattersDao.updateByPrimaryKey(departmentalMatters);
		//更新数据库中的版本号
		versionmanagementDao.updateSheetVersion(id,1);
		//获取事项id 是否有父
		String departmentalMattersPId = departmentalMattersDao.selectPidById(businessGuideOtherServicesOnePrivate.getDepartmentalMattersId());
		//更新父部门状态
		departmentalMattersService.updateFatherState(departmentalMattersPId);

	}

}