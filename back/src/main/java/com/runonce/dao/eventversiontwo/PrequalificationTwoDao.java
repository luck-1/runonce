package com.runonce.dao.eventversiontwo;

import org.apache.ibatis.annotations.Mapper;

import com.runonce.dao.MyMapper;
import  com.runonce.model.eventversiontwo.PrequalificationTwo;

import java.util.List;


/**
* 
*
* Created by xuxueli on '2018-12-10 15:04:12'.
*/
@Mapper
public interface PrequalificationTwoDao  extends MyMapper<PrequalificationTwo> {
    void save(List<PrequalificationTwo> list);
    List<PrequalificationTwo> findByEventId(String eventId);
    void deleteByEventId(String eventId);
}
