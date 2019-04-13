package com.runonce.dao.eventversionone;

import com.runonce.model.http.MaterialGroupAddParam;
import org.apache.ibatis.annotations.Mapper;

import com.runonce.dao.MyMapper;
import  com.runonce.model.eventversionone.MaterialGrouping;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
* 
*
* Created by xuxueli on '2018-12-10 11:31:13'.
*/
@Mapper
public interface MaterialGroupingDao  extends MyMapper<MaterialGroupAddParam> {

    String findEventIdByMaterialId(@Param("id") String materialId);

    List<MaterialGroupAddParam> selectByEventIdAndIo(@Param("eventId") String eventId,@Param("io") String io);
}
