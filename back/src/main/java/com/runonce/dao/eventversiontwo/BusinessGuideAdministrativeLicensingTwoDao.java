package com.runonce.dao.eventversiontwo;

import org.apache.ibatis.annotations.Mapper;

import com.runonce.dao.MyMapper;
import  com.runonce.model.eventversiontwo.BusinessGuideAdministrativeLicensingTwo;

import java.util.List;


/**
* 
*
* Created by xuxueli on '2018-12-10 15:04:12'.
*/
@Mapper
public interface BusinessGuideAdministrativeLicensingTwoDao  extends MyMapper<BusinessGuideAdministrativeLicensingTwo> {

    /**
     *  事件ID查询
     */
    BusinessGuideAdministrativeLicensingTwo findByEventId(String EventId);

    /**
     *  事件ID删除
     */
    void deleteByEventId(String eventId);
  

}
