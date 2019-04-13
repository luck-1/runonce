package com.runonce.dao.eventversionone;

import com.runonce.httpbean.assets.resbean.FromDescriptionInfoRes;
import com.runonce.model.eventpublic.BusinessFileRelation;
import com.runonce.model.eventversionone.MaterialGrouping;
import org.apache.ibatis.annotations.Mapper;

import com.runonce.dao.MyMapper;
import  com.runonce.model.eventversionone.FormDescription;
import org.apache.ibatis.annotations.Param;
import org.json.JSONObject;

import java.util.List;


/**
 * 表单说明
 *
 * Created by xuxueli on '2018-12-18 13:17:37'.
 */
@Mapper
public interface FormDescriptionDao  extends MyMapper<FormDescription> {

    List<MaterialGrouping> findApplicationForm(@Param("eventId") String eventId);

    List<BusinessFileRelation> findPhoto(String businessId);

    List<FormDescription> findMaterial(String materialId);

    List<FormDescription> findAllMaterialByEventId(String eventId);

    void deleteByMaterialId(String materialId);

    void deleteByEventId(String eventId);

    /**
     * 表单详情
     * @param eventId
     * @param materialType
     * @return
     */
    List<FromDescriptionInfoRes> selectFormDescriptionInfo(@Param("eventId") String eventId,@Param("materialType") String materialType);

}
