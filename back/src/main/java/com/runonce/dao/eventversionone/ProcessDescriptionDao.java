package com.runonce.dao.eventversionone;

import org.apache.ibatis.annotations.Mapper;

import com.runonce.dao.MyMapper;
import  com.runonce.model.eventversionone.ProcessDescription;

import java.util.List;


/**
* 
*
* Created by xuxueli on '2018-12-10 11:31:13'.
*/
@Mapper
public interface ProcessDescriptionDao  extends MyMapper<ProcessDescription> {

   List<ProcessDescription> findByEventId(String eventId);

   int deleteByEventId(String eventId);

}
