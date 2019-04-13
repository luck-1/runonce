package com.runonce.service.impl.eventversionone;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import com.runonce.service.eventversionone.VersionmanagementService;
import com.runonce.dao.eventversionone.VersionmanagementDao;
import com.runonce.service.AbstractService;
import  com.runonce.model.eventversionone.Versionmanagement;
import org.springframework.transaction.annotation.Transactional;

/**
* 事项sheet页版本管理
*
* Created by xuxueli on '2019-01-28 15:06:51'.
*/
@Service
public class VersionmanagementServiceImpl extends AbstractService<Versionmanagement> implements VersionmanagementService  {

	@Resource
	private VersionmanagementDao versionmanagementDao;


	@Override
	public Integer getSheetVersion(String dmId, Integer sheetNumber) {
		return versionmanagementDao.getSheetVersion(dmId,sheetNumber);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Integer updateSheetVersion(String dmId, Integer sheetNumber) {
		return versionmanagementDao.updateSheetVersion(dmId,sheetNumber);
	}

}
