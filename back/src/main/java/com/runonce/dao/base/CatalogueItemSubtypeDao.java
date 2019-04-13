package com.runonce.dao.base;

import org.apache.ibatis.annotations.Mapper;

import com.runonce.dao.MyMapper;
import  com.runonce.model.base.CatalogueItemSubtype;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
* 
*
* Created by xuxueli on '2018-12-10 14:57:46'.
*/
@Mapper
public interface CatalogueItemSubtypeDao  extends MyMapper<CatalogueItemSubtype> {

  List<CatalogueItemSubtype> findByPid(String Pid);

  String findByNameAndPid(@Param("pid") Integer pid,@Param("name") String name);

}
