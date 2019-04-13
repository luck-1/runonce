package com.runonce.dao.base;

import org.apache.ibatis.annotations.Mapper;

import com.runonce.dao.MyMapper;
import  com.runonce.model.base.SituationtoguideAndKeywordModle;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
* 情形引导关键字中间表
*
* Created by xuxueli on '2019-02-27 14:10:47'.
*/
@Mapper
public interface SituationtoguideAndKeywordModleDao  extends MyMapper<SituationtoguideAndKeywordModle> {

    List<String> findKeywordNameBySituationToGuideId(@Param("situationToGuideId") String situationToGuideId);

    void deleteBySituationToGuideId(@Param("situationToGuideId") String situationToGuideId);
}
