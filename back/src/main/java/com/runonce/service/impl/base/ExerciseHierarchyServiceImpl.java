package com.runonce.service.impl.base;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import com.runonce.service.base.ExerciseHierarchyService;
import com.runonce.dao.base.ExerciseHierarchyDao;
import com.runonce.service.AbstractService;
import  com.runonce.model.base.ExerciseHierarchy;

import java.util.ArrayList;
import java.util.List;

/**
* 
*
* Created by xuxueli on '2018-12-10 14:57:46'.
*/
@Service
public class ExerciseHierarchyServiceImpl extends AbstractService<ExerciseHierarchy> implements ExerciseHierarchyService  {

	@Resource
	private ExerciseHierarchyDao exerciseHierarchyDao;
}
