package com.runonce.dao.eventversionone;

import org.apache.ibatis.annotations.Mapper;

import com.runonce.dao.MyMapper;
import com.runonce.model.eventversionone.RevisionHistory;

import java.util.List;


/**
* 变更履历
*
* Created by xuxueli on '2019-02-19 10:28:47'.
*/
@Mapper
public interface RevisionHistoryDao extends MyMapper<RevisionHistory> {


    RevisionHistory selectVersionByDmId(String dmId);


    List<RevisionHistory> findByDmId(String id);

    RevisionHistory selectById(String id);


    Integer selectCountByDmId(String dmId);

    RevisionHistory selectRevisionhistoryById(String id);

    RevisionHistory selectLastInfo(String dmId);
}
