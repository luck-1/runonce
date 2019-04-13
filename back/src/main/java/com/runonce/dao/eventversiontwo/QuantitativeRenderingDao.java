package com.runonce.dao.eventversiontwo;

import org.apache.ibatis.annotations.Mapper;

import com.runonce.dao.MyMapper;
import  com.runonce.model.eventversiontwo.QuantitativeRendering;

import java.util.List;


/**
* 
*
* Created by xuxueli on '2018-12-10 15:04:12'.
*/
@Mapper
public interface QuantitativeRenderingDao  extends MyMapper<QuantitativeRendering> {

    /**
     * 根据dmId(eventID)查询
     * @param dmId
     * @return
     */
    QuantitativeRendering findBydmId(String dmId);



}
