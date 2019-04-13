package com.runonce.dao.eventversionone;

import com.runonce.model.eventpublic.BusinessFileRelation;
import org.apache.ibatis.annotations.Mapper;

import com.runonce.dao.MyMapper;
import  com.runonce.model.eventversionone.ApplicationTypeField;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
* 
*
* Created by xuxueli on '2018-12-10 11:31:13'.
*/
@Mapper
public interface ApplicationTypeFieldDao  extends MyMapper<ApplicationTypeField> {

    public List<ApplicationTypeField> selectByParentId(@Param("parentId") String parentId);

    int selectIsExsit(@Param("materiaId")String materiaId,@Param("fieldName")String fieldName);

}
