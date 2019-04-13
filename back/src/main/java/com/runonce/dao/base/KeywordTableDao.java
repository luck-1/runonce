package com.runonce.dao.base;

import org.apache.ibatis.annotations.Mapper;

import com.runonce.dao.MyMapper;
import  com.runonce.model.base.KeywordTable;
import org.apache.ibatis.annotations.Param;


/**
* 关键字
*
* Created by xuxueli on '2019-02-27 14:10:47'.
*/
@Mapper
public interface KeywordTableDao  extends MyMapper<KeywordTable> {

    KeywordTable findKeywordTableByName(@Param("name") String name);
  

}
