package com.runonce.dao.eventversionone;

import org.apache.ibatis.annotations.Mapper;

import com.runonce.dao.MyMapper;
import  com.runonce.model.eventversionone.AuditRules;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
* 审核细则
*
* Created by xuxueli on '2018-12-18 12:32:51'.
*/
@Mapper
public interface AuditRulesDao  extends MyMapper<AuditRules> {


    int deleteByMaterialId(String  MaterialId);

    List<AuditRules> selectByMaterialId(@Param("MaterialId") String  MaterialId,@Param("type") Integer type);

}
