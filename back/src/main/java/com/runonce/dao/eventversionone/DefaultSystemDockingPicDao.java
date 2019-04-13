package com.runonce.dao.eventversionone;

import org.apache.ibatis.annotations.Mapper;

import com.runonce.dao.MyMapper;
import  com.runonce.model.eventversionone.DefaultSystemDockingPic;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
* 
*
* Created by xuxueli on '2019-01-04 10:40:30'.
*/
@Mapper
public interface DefaultSystemDockingPicDao  extends MyMapper<DefaultSystemDockingPic> {

  
    List<DefaultSystemDockingPic> selectAllRecord();

    DefaultSystemDockingPic selectByPrimaryKeyOvrride(@Param("id") String id);
}
