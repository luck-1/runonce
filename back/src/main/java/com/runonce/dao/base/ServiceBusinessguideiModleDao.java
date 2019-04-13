package com.runonce.dao.base;

import com.runonce.dao.MyMapper;
import com.runonce.model.base.ServiceBusinessguideiModle;
import com.runonce.model.base.ServiceObject;
import org.apache.ibatis.annotations.Param;
import org.hibernate.annotations.Parameter;

import java.util.List;

public interface ServiceBusinessguideiModleDao extends MyMapper<ServiceBusinessguideiModle> {

    List<String> findServiceObject (@Param("businessGuideId")String businessGuideId);

    List<ServiceBusinessguideiModle> findBusinessguideiModle(@Param("businessGuideId") String businessGuideId);
}
