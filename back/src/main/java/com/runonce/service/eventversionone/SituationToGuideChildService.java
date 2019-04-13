package com.runonce.service.eventversionone;
import com.runonce.httpbean.assets.reqbean.ConfigurationMattersReq;
import com.runonce.httpbean.assets.reqbean.IdAndNameReq;
import com.runonce.httpbean.assets.reqbean.SituationToGuideChildSelectReq;
import com.runonce.model.http.DepartmentalMattersRes;
import org.springframework.stereotype.Component;
import com.runonce.service.Service;
import  com.runonce.model.eventversionone.SituationToGuideChild;

import java.util.List;
import java.util.Map;

/**
 * @author lxc
 * @date 2019-02-28 15:47:09
 * @description 情形引导子项
 */
@Component
public interface SituationToGuideChildService extends Service<SituationToGuideChild> {

    /**
     * 批量删除
     * @param ids idList
     */
    void deleteBySelect(List<String> ids);


    /**
     * 条件查询
     * @param situationToGuideChildSelectReq 条件
     * @return map
     */
    Map<String,Object> selectByCondition(SituationToGuideChildSelectReq situationToGuideChildSelectReq);


    /**
     * 添加/修改情形引导子项
     * @param situationToGuideChild 情形引导子项表单
     */
    void saveFrom(SituationToGuideChild situationToGuideChild);


    /**
     * 名称是否重复
     * @param idAndNameReq id和名称
     * @return boolean
     */
    boolean checkDuplicateName(IdAndNameReq idAndNameReq);


    /**
     * 配置事项
     * @param configurationMattersReq 配置参数
     */
    void configurationMatters(ConfigurationMattersReq configurationMattersReq);


    /**
     * 查询配置事项
     * @param id  ID
     * @return
     */
    List<DepartmentalMattersRes> selectConfigurationMatters(String id);


    /**
     * 单个删除
     * @param id
     */
    void delete(String id);
}