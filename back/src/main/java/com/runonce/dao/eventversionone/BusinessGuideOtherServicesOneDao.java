package com.runonce.dao.eventversionone;

import com.runonce.httpbean.assets.reqbean.BusinessGuideOtherServicesOneReq;
import org.apache.ibatis.annotations.Mapper;

import com.runonce.dao.MyMapper;
import  com.runonce.model.eventversionone.BusinessGuideOtherServicesOne;

import java.util.List;


/**
* 
*
* Created by xuxueli on '2018-12-10 11:31:13'.
*/
@Mapper
public interface BusinessGuideOtherServicesOneDao  extends MyMapper<BusinessGuideOtherServicesOne> {
    /**
     *  事件ID查询
     */
    BusinessGuideOtherServicesOneReq findByEventId(String EventId);

    BusinessGuideOtherServicesOneReq showInfo(String EventId);

    List<BusinessGuideOtherServicesOne> findBusinessGuide(String EventId);
    /**
     *  事件ID删除
     */
    void deleteByEventId(String eventId);
}