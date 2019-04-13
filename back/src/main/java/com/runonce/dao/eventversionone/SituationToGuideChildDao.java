package com.runonce.dao.eventversionone;

import org.apache.ibatis.annotations.Mapper;
import com.runonce.dao.MyMapper;
import  com.runonce.model.eventversionone.SituationToGuideChild;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * @author lxc
 * @date 2019-02-28 15:47:10
 * @description 情形引导子项
 */
@Mapper
public interface SituationToGuideChildDao  extends MyMapper<SituationToGuideChild> {

    Integer checkPresence(@Param("pid") String pid,@Param("eventName") String eventName);

    List<SituationToGuideChild> findByPid(@Param("pid")String pid);

    void deleteByPid(@Param("pid")String pid);
}