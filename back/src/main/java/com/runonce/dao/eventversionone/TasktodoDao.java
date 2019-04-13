package com.runonce.dao.eventversionone;

import org.apache.ibatis.annotations.Mapper;

import com.runonce.dao.MyMapper;
import  com.runonce.model.eventversionone.Tasktodo;

import java.util.List;


/**
* taskToDo
*
* Created by xuxueli on '2019-02-21 11:20:11'.
*/
@Mapper
public interface TasktodoDao  extends MyMapper<Tasktodo> {


    List<Tasktodo> findByUserId(String userId);
}
