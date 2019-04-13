package com.runonce.service.impl.base;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import com.runonce.service.base.CatalogueItemSubtypeService;
import com.runonce.dao.base.CatalogueItemSubtypeDao;
import com.runonce.service.AbstractService;
import  com.runonce.model.base.CatalogueItemSubtype;

import java.util.List;

/**
* 
*
* Created by xuxueli on '2018-12-10 14:57:46'.
*/
@Service
public class CatalogueItemSubtypeServiceImpl extends AbstractService<CatalogueItemSubtype> implements CatalogueItemSubtypeService  {

	@Resource
	private CatalogueItemSubtypeDao catalogueItemSubtypeDao;

	public List<CatalogueItemSubtype> findByPid(String Pid){
		List<CatalogueItemSubtype> list = catalogueItemSubtypeDao.findByPid(Pid);
		return list;
	}
	
}
