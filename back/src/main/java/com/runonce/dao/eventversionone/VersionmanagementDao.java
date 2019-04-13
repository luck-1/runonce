package com.runonce.dao.eventversionone;

import org.apache.ibatis.annotations.Mapper;

import com.runonce.dao.MyMapper;
import  com.runonce.model.eventversionone.Versionmanagement;
import org.apache.ibatis.annotations.Param;


/**
* 事项sheet页版本管理
*
* Created by xuxueli on '2019-01-28 15:06:51'.
*/
@Mapper
public interface VersionmanagementDao  extends MyMapper<Versionmanagement> {


    Integer getSheetVersion(@Param("dmId") String dmId, @Param("sheetNumber")Integer sheetNumber);

    Integer updateSheetVersion(@Param("dmId") String dmId, @Param("sheetNumber")Integer sheetNumber);


    /**
     * 重置状态
     */

    Integer updateAllSheetVersion(@Param("dmId") String dmId);

}
