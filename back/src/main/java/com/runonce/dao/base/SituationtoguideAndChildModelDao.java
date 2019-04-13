package com.runonce.dao.base;

import com.runonce.model.eventversionone.SituationToGuideChild;
import org.apache.ibatis.annotations.Mapper;
import com.runonce.dao.MyMapper;
import  com.runonce.model.base.SituationtoguideAndChildModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author lxc
 * @date 2019-03-01 10:22:57
 * @description 情形引导父项和子项中间表
 */
@Mapper
public interface SituationtoguideAndChildModelDao  extends MyMapper<SituationtoguideAndChildModel> {

    List<SituationToGuideChild> findChildNameByPid(@Param("pid")String pid);

    void deleteByPid(@Param("pid")String pid);

    List<SituationToGuideChild> findChildByPid(@Param("pid")String pid);

    Integer findChildCount(@Param("pid")String pid);
}