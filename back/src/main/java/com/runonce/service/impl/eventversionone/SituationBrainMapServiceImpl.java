package com.runonce.service.impl.eventversionone;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSONObject;
import com.runonce.ReturnCode;
import com.runonce.enums.SheetNumEnum;
import com.runonce.exception.CustomException;
import com.runonce.httpbean.assets.reqbean.MinderDataReq;
import com.runonce.httpbean.assets.reqbean.SituationBrainMapReq;
import com.runonce.model.eventpublic.BusinessFileRelation;
import com.runonce.model.eventpublic.MinderData;
import com.runonce.model.eventversionone.ApplicationTypeField;
import com.runonce.model.eventversionone.ApplicationTypeValue;
import com.runonce.model.eventversionone.DepartmentalMatters;
import com.runonce.model.http.MindeRoot;
import com.runonce.service.eventpublic.BusinessFileRelationService;
import com.runonce.util.SysUtil;
import org.springframework.stereotype.Service;
import com.runonce.service.eventversionone.SituationBrainMapService;
import com.runonce.dao.eventversionone.SituationBrainMapDao;
import com.runonce.service.AbstractService;
import com.runonce.model.eventversionone.SituationBrainMap;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * 情形引导-脑图
 * <p>
 * Created by xuxueli on '2019-02-28 14:53:56'.
 */
@Service
public class SituationBrainMapServiceImpl extends AbstractService<SituationBrainMap> implements SituationBrainMapService {

    @Resource
    private SituationBrainMapDao situationBrainMapDao;

    @Resource
    private BusinessFileRelationService businessFileRelationService;


    @Transactional
    @Override
    public void saveInfo(SituationBrainMapReq situationBrainMapReq) {
        SituationBrainMap situationBrainMap = situationBrainMapReq.getSituationBrainMap();

        Condition mindCondition = new Condition(SituationBrainMap.class);
        Example.Criteria mindCriteria = mindCondition.createCriteria();
        mindCriteria.andEqualTo("situationId", situationBrainMap.getSituationId());
        mindCriteria.andEqualTo("type", situationBrainMap.getType());
        mapper.deleteByCondition(mindCondition);

        // 导图数据新增
        situationBrainMap.setId(SysUtil.getUUID());
        mapper.insertSelective(situationBrainMap);

        // 思维导图保存
        BusinessFileRelation file = new BusinessFileRelation();
        file.setId(SysUtil.getUUID());
        file.setBusinessId(situationBrainMap.getSituationId());
        // 申请类型状态更新
        if (situationBrainMap.getType() == 1) {
            file.setBusinessType(5);
        } else {
            file.setBusinessType(6);
        }

        file.setFilePath(situationBrainMap.getPicPath());

        //更新图片
        businessFileRelationService.save(file);
    }


    @Override
    public SituationBrainMapReq selectByEventId(String situationId, Integer type) {

        SituationBrainMapReq situationBrainMapReq = new SituationBrainMapReq();

        Condition condition = new Condition(SituationBrainMap.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("situationId", situationId);
        criteria.andEqualTo("type", type);
        List<SituationBrainMap> SituationBrainList = findByCondition(condition);
        if (SituationBrainList == null || SituationBrainList.size() == 0) {
            return situationBrainMapReq;
        }
        List<BusinessFileRelation> businessFileRelationList = null;

        Condition condition1 = new Condition(BusinessFileRelation.class);
        Example.Criteria criteria1 = condition1.createCriteria();
        criteria1.andEqualTo("businessId", situationId);

        if (type == 1) {
            criteria1.andEqualTo("businessType", 5);
        } else {
            criteria1.andEqualTo("businessType", 6);
        }
        businessFileRelationList = businessFileRelationService.findByCondition(condition1);

        SituationBrainMap situationBrainMap = SituationBrainList.get(0);
        if (businessFileRelationList != null && businessFileRelationList.size() > 0) {
            situationBrainMap.setPicPath(businessFileRelationList.get(0).getFilePath());
        }

        situationBrainMapReq.setSituationBrainMap(situationBrainMap);

        return situationBrainMapReq;
    }


}
