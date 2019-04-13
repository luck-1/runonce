package com.runonce.dao.eventversionone;

import com.runonce.httpbean.assets.reqbean.BusinessGuideFilingOneReq;
import org.apache.ibatis.annotations.Mapper;

import com.runonce.dao.MyMapper;
import  com.runonce.model.eventversionone.BusinessGuideFilingOne;

import java.util.List;


/**
* 
*
* Created by xuxueli on '2018-12-10 11:31:13'.
*/
@Mapper
public interface BusinessGuideFilingOneDao  extends MyMapper<BusinessGuideFilingOne> {
    /**
     *  事件ID查询
     */
    BusinessGuideFilingOneReq findByEventId(String EventId);

    BusinessGuideFilingOneReq showInfo(String EventId);

    List<BusinessGuideFilingOne> findBusinessGuide(String EventId);

    /**
     *  事件ID删除
     */
     void deleteByEventId(String eventId);
}
