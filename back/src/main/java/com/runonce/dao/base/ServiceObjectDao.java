package com.runonce.dao.base;

import org.apache.ibatis.annotations.Mapper;

import com.runonce.dao.MyMapper;
import  com.runonce.model.base.ServiceObject;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
* 
*
* Created by xuxueli on '2018-12-10 14:57:47'.
*/
@Mapper
public interface ServiceObjectDao  extends MyMapper<ServiceObject> {


    /**
     * 查询名称
     * @param serviceObjectList
     * @return
     */
    List<String> selectNameByIds(@Param("serviceObjectList") List<String> serviceObjectList);

}
