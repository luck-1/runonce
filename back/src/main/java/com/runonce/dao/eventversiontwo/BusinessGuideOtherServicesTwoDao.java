package com.runonce.dao.eventversiontwo;

import org.apache.ibatis.annotations.Mapper;

import com.runonce.dao.MyMapper;
import  com.runonce.model.eventversiontwo.BusinessGuideOtherServicesTwo;

import java.util.List;


/**
* 
*
* Created by xuxueli on '2018-12-10 15:04:12'.
*/
@Mapper
public interface BusinessGuideOtherServicesTwoDao  extends MyMapper<BusinessGuideOtherServicesTwo> {

    /**
     *  事件ID查询
     */
    BusinessGuideOtherServicesTwo findByEventId(String EventId);

    /**
     *  事件ID删除
     */
    void deleteByEventId(String eventId);

}
