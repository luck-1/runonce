package com.runonce.dao.eventversionone;

import com.runonce.httpbean.assets.reqbean.BusinessGuideAuditAndForwardingCategoryOneReq;
import org.apache.ibatis.annotations.Mapper;

import com.runonce.dao.MyMapper;
import  com.runonce.model.eventversionone.BusinessGuideAuditAndForwardingCategoryOne;

import java.util.List;


/**
* 
*
* Created by xuxueli on '2018-12-10 11:31:13'.
*/
@Mapper
public interface BusinessGuideAuditAndForwardingCategoryOneDao  extends MyMapper<BusinessGuideAuditAndForwardingCategoryOne> {
    /**
     *  事件ID查询
     */
    BusinessGuideAuditAndForwardingCategoryOneReq findByEventId(String EventId);

    BusinessGuideAuditAndForwardingCategoryOneReq showInfo(String EventId);

    List<BusinessGuideAuditAndForwardingCategoryOne> findBusinessGuide(String EventId);

    /**
     *  事件ID删除
     */
    void deleteByEventId(String eventId);
}
