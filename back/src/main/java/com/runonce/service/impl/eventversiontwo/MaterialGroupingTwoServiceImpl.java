package com.runonce.service.impl.eventversiontwo;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import com.runonce.service.eventversiontwo.MaterialGroupingTwoService;
import com.runonce.dao.eventversiontwo.MaterialGroupingTwoDao;
import com.runonce.service.AbstractService;
import  com.runonce.model.eventversiontwo.MaterialGroupingTwo;

/**
* 
*
* Created by xuxueli on '2018-12-10 15:04:12'.
*/
@Service
public class MaterialGroupingTwoServiceImpl extends AbstractService<MaterialGroupingTwo> implements MaterialGroupingTwoService  {

	@Resource
	private MaterialGroupingTwoDao materialGroupingTwoDao;

	
}
