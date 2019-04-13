package com.runonce.service.impl.eventversionone;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.runonce.ReturnCode;
import com.runonce.dao.eventversionone.DepartmentalMattersDao;
import com.runonce.dao.eventversionone.VersionmanagementDao;
import com.runonce.exception.CustomException;
import com.runonce.model.eventversionone.DepartmentalMatters;
import com.runonce.model.http.ProcessDescriptionParam;
import com.runonce.util.SysUtil;
import com.runonce.util.WebTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.runonce.service.eventversionone.ProcessDescriptionService;
import com.runonce.dao.eventversionone.ProcessDescriptionDao;
import com.runonce.service.AbstractService;
import  com.runonce.model.eventversionone.ProcessDescription;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
* 
*
* Created by xuxueli on '2018-12-10 11:31:13'.
*/
@Service
public class ProcessDescriptionServiceImpl extends AbstractService<ProcessDescription> implements ProcessDescriptionService  {

	@Resource
	private ProcessDescriptionDao processDescriptionDao;

	@Autowired
	private DepartmentalMattersServiceImpl departmentalMattersService;

	@Resource
	private VersionmanagementDao versionmanagementDao;

	@Resource
	private DepartmentalMattersDao departmentalMattersDao;

	@Override
	public List<ProcessDescription> findByEventId(String eventId) {
		return processDescriptionDao.findByEventId(eventId);
	}



	@Override
	public void deleteByEventId(String eventId) {
		processDescriptionDao.deleteByEventId(eventId);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public ReturnCode saveInfo(ProcessDescriptionParam processDescriptionParam, HttpServletRequest request){
		// 事项存在校验
		DepartmentalMatters event = departmentalMattersService.findById(processDescriptionParam.getEventId());
		if(event == null){
			return new ReturnCode.Builder().failed().msg("该事项不存在").build();
		}
		//数据库中存储的版本号
		Integer version1 = versionmanagementDao.getSheetVersion(processDescriptionParam.getEventId(),5);
		if(version1 == null ){
			throw new CustomException(new ReturnCode.Builder().failed().msg("未知错误：未查到当前版本！").build());
		}
		//前端得到的版本号（点击配置按钮时的版本）
		Integer version2 = processDescriptionParam.getVersion();

		if(version1.equals(version2)){
			// 按事项id删除记录
			deleteByEventId(processDescriptionParam.getEventId());
			// 审核记录新增
			if(processDescriptionParam.getProcessDescriptionList() != null && processDescriptionParam.getProcessDescriptionList().size() > 0){
				for(ProcessDescription item:processDescriptionParam.getProcessDescriptionList()){
					item.setId(SysUtil.getUUID());
					mapper.insert(item);
				}
			}
		}else{
			throw new CustomException(new ReturnCode.Builder().failed().msg("当前已有人在编辑，更改失败！").build());
		}
		//更新数据库中的版本号
		versionmanagementDao.updateSheetVersion(processDescriptionParam.getEventId(),5);
		//更新修改用户
		event.setOperator(WebTokenUtil.getUserByWebToken(request));
		// 事项相关状态修改
		event.setProcessDescriptionState(processDescriptionParam.getState());
		departmentalMattersService.update(event);
		//获取事项id 是否有父
		String departmentalMattersPId = departmentalMattersDao.selectPidById(event.getId());
		//更新父部门状态
		departmentalMattersService.updateFatherState(departmentalMattersPId);
		return new ReturnCode.Builder().success().msg("现有流程保存成功").build();
	}
}
