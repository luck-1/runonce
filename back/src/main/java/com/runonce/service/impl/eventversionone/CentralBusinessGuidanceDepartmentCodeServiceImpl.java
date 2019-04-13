package com.runonce.service.impl.eventversionone;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.runonce.service.eventversionone.CentralBusinessGuidanceDepartmentCodeService;
import com.runonce.dao.eventversionone.CentralBusinessGuidanceDepartmentCodeDao;
import com.runonce.service.AbstractService;
import com.runonce.model.eventversionone.CentralBusinessGuidanceDepartmentCode;

/**
 * @author lxc
 * @date 2019/3/18
 * @description 中央业务指导（实施）部门代码
 */
@Service
public class CentralBusinessGuidanceDepartmentCodeServiceImpl extends AbstractService<CentralBusinessGuidanceDepartmentCode> implements CentralBusinessGuidanceDepartmentCodeService  {

	@Resource
	private CentralBusinessGuidanceDepartmentCodeDao centralBusinessGuidanceDepartmentCodeDao;

	
}