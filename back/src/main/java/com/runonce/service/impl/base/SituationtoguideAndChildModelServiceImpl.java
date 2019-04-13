package com.runonce.service.impl.base;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.runonce.service.base.SituationtoguideAndChildModelService;
import com.runonce.dao.base.SituationtoguideAndChildModelDao;
import com.runonce.service.AbstractService;
import  com.runonce.model.base.SituationtoguideAndChildModel;

/**
 * @author lxc
 * @date 2019-03-01 10:22:57
 * @description 情形引导父项和子项中间表
 */
@Service
public class SituationtoguideAndChildModelServiceImpl extends AbstractService<SituationtoguideAndChildModel> implements SituationtoguideAndChildModelService  {

	@Resource
	private SituationtoguideAndChildModelDao situationtoguideAndChildModelDao;

	
}