package com.runonce.dao.eventversionone;

import org.apache.ibatis.annotations.Mapper;

import com.runonce.dao.MyMapper;
import  com.runonce.model.eventversionone.Prequalification;

import java.util.List;


/**
* 
*
* Created by xuxueli on '2018-12-10 11:31:13'.
*/
@Mapper
public interface PrequalificationDao  extends MyMapper<Prequalification> {

    List<Prequalification> findByEventId(String eventId);

    int deleteByEventId(String eventId);
}
