package com.runonce.dao.eventversionone;

import com.runonce.model.eventversionone.SituationToGuideChild;
import org.apache.ibatis.annotations.Mapper;

import com.runonce.dao.MyMapper;
import  com.runonce.model.eventversionone.SituationToGuide;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
* 情形引导
*
* Created by xuxueli on '2019-02-27 14:09:17'.
*/
@Mapper
public interface SituationToGuideDao  extends MyMapper<SituationToGuide> {

    List<SituationToGuide> findByEventName(@Param("eventName") String eventName);

    List<SituationToGuide> findByKeyword(@Param("keyword") String keyword);

    Integer findEventNameCount(@Param("eventName")String eventName,@Param("thisId")String thisId);
}
