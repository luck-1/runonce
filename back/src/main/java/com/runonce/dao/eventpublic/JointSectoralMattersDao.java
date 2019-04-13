package com.runonce.dao.eventpublic;

import com.runonce.httpbean.assets.reqbean.JointSectoralMatters;
import com.runonce.model.eventpublic.EventsTable;
import com.runonce.model.eventversionone.DepartmentalMatters;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface JointSectoralMattersDao {


    List<JointSectoralMatters> selectMunicipalEvent(String catalogName);

}
