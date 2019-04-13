package com.runonce.dao.base;

import com.runonce.service.base.ExerciseHierarchyService;
import org.apache.ibatis.annotations.Mapper;

import com.runonce.dao.MyMapper;
import  com.runonce.model.base.ExerciseHierarchy;

import java.util.List;


/**
* 
*
* Created by xuxueli on '2018-12-10 14:57:46'.
*/
@Mapper
public interface ExerciseHierarchyDao  extends MyMapper<ExerciseHierarchy> {

    List<ExerciseHierarchy> findProvinceAll();

}
