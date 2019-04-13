package com.runonce.dao.eventversionone;

import org.apache.ibatis.annotations.Mapper;

import com.runonce.dao.MyMapper;
import  com.runonce.model.eventversionone.EventDirectory;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
* 三级四同目录
*
* Created by xuxueli on '2019-01-16 12:00:31'.
*/
@Mapper
public interface EventDirectoryDao  extends MyMapper<EventDirectory> {

    List<EventDirectory> findEventDirectory(@Param("implementatioDepartment") String implementatioDepartment,
                                            @Param("genericName") String genericName,
                                            @Param("provincialName")String provincialName,
                                            @Param("bigTypesOfEvents") String bigTypesOfEvents,
                                            @Param("itemSubtype") String itemSubtype);

    /**
     * 查询区县独有目录
     * @return
     */
    List<EventDirectory> findPrivate();

    /**
     * 省级目录
     * @return
     */
    List<EventDirectory> findCatalogListByProvince();

    /**
     * 市级目录
     * @return
     */
    List<EventDirectory> findCatalogListByCity();

    /**
     * 区级目录
     * @return
     */
    List<EventDirectory> findCatalogListByArea();

    /**
     * 通用目录
     * @return
     */
    List<EventDirectory> findCatalogListByCurrency();

    Integer findGroupIdCount(@Param("municipalName")String municipalName,@Param("id")String id);

    Integer checkDirectoryIsUsed(String id);
}
