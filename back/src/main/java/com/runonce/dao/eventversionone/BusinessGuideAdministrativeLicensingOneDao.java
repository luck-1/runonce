package com.runonce.dao.eventversionone;

import com.runonce.httpbean.assets.reqbean.BusinessGuideAdministrativeLicensingOneReq;
import org.apache.ibatis.annotations.Mapper;

import com.runonce.dao.MyMapper;
import  com.runonce.model.eventversionone.BusinessGuideAdministrativeLicensingOne;

import java.util.List;


/**
* 
*
* Created by xuxueli on '2018-12-10 11:31:13'.
*/
@Mapper
public interface BusinessGuideAdministrativeLicensingOneDao  extends MyMapper<BusinessGuideAdministrativeLicensingOne> {

    /**
     *  事件ID查询
     */
    BusinessGuideAdministrativeLicensingOneReq findByEventId(String EventId);

    BusinessGuideAdministrativeLicensingOneReq showInfo(String EventId);

    List<BusinessGuideAdministrativeLicensingOne> findBusinessGuide(String EventId);

    String findEventBigType(String eventId);
    /**
     *  事件ID删除
     */
    void deleteByEventId(String eventId);
}
