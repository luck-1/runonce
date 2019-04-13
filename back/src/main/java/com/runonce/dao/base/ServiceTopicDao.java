package com.runonce.dao.base;

import com.runonce.httpbean.assets.reqbean.ObjectTopic;
import org.apache.ibatis.annotations.Mapper;

import com.runonce.dao.MyMapper;
import  com.runonce.model.base.ServiceTopic;

import java.util.List;


/**
* 
*
* Created by xuxueli on '2018-12-11 09:37:24'.
*/
@Mapper
public interface ServiceTopicDao  extends MyMapper<ServiceTopic> {

    List<String> findByPid(String pid);

    ServiceTopic selectByCode(String code);

    List<com.runonce.httpbean.assets.reqbean.ServiceTopic> sendTopic();

    List<ObjectTopic> sendObjectTopic();
}
