package com.runonce.dao.eventpublic;

import org.apache.ibatis.annotations.Mapper;

import com.runonce.dao.MyMapper;
import  com.runonce.model.eventpublic.BusinessFileRelation;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
* 
*
* Created by xuxueli on '2018-12-11 09:36:41'.
*/
@Mapper
public interface BusinessFileRelationDao  extends MyMapper<BusinessFileRelation> {

    public List<BusinessFileRelation> selectByBusinessIdAndType(@Param("businessId") String businessId, @Param("businessType") Integer businessType);

    public List<BusinessFileRelation> selectByBusinessId(@Param("businessId") String businessId);

    String findFilepath(String businessId);
}
