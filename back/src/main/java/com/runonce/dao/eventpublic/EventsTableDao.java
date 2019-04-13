package com.runonce.dao.eventpublic;

import com.github.pagehelper.Page;
import com.runonce.model.eventpublic.JointSectoralMatterReq;
import com.runonce.model.http.SelectDepartmentalMattersListParam;
import org.apache.ibatis.annotations.Mapper;

import com.runonce.dao.MyMapper;
import  com.runonce.model.eventpublic.EventsTable;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
* 
*
* Created by xuxueli on '2018-12-10 15:05:36'.
*/
@Mapper
public interface EventsTableDao  extends MyMapper<EventsTable> {

    /**
     * 查询一级事项
     * @param param
     * @return
     */
    Page<EventsTable> selectByParam(@Param("param") SelectDepartmentalMattersListParam param);

    /**
     * 根据父级id查询子集事项
     * @param parentId
     * @return
     */
    List<EventsTable> selectSecondLevelEvent(@Param("parentId") String parentId,@Param("param") SelectDepartmentalMattersListParam param);

    /**
     * 根据部门id查询分配的事项

     * @return
     */
    List<EventsTable> selectDistributedEvents(JointSectoralMatterReq jointSectoralMatterReq);

    Integer selectDistributedEventsCount(JointSectoralMatterReq jointSectoralMatterReq);

    List<EventsTable> findDirectory(@Param("groupId") String groupId);
}
