package com.runonce.service.impl.eventversionone;

import javax.annotation.Resource;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.runonce.dao.eventversionone.DepartmentalMattersDao;
import com.runonce.dao.eventversionone.SituationChildAndDmModelDao;
import com.runonce.httpbean.assets.reqbean.ConfigurationMattersReq;
import com.runonce.httpbean.assets.reqbean.IdAndNameReq;
import com.runonce.httpbean.assets.reqbean.SituationToGuideChildSelectReq;
import com.runonce.model.eventversionone.ApplicationTypeField;
import com.runonce.model.eventversionone.SituationChildAndDmModel;
import com.runonce.model.http.DepartmentalMattersRes;
import com.runonce.service.eventversionone.SituationChildAndDmModelService;
import com.runonce.util.SysUtil;
import org.springframework.stereotype.Service;
import com.runonce.service.eventversionone.SituationToGuideChildService;
import com.runonce.dao.eventversionone.SituationToGuideChildDao;
import com.runonce.service.AbstractService;
import com.runonce.model.eventversionone.SituationToGuideChild;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.security.PrivateKey;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lxc
 * @date 2019-02-28 15:47:10
 * @description 情形引导子项
 */
@Service
public class SituationToGuideChildServiceImpl extends AbstractService<SituationToGuideChild> implements SituationToGuideChildService {

    @Resource
    private SituationToGuideChildDao situationToGuideChildDao;
    @Resource
    private SituationChildAndDmModelService situationChildAndDmModelService;


    @Resource
    private DepartmentalMattersDao departmentalMattersDao;

    /**
     * 批量删除
     *
     * @param ids idList
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBySelect(List<String> ids) {


        ids.forEach(id -> {
            //删除子项表
            situationToGuideChildDao.deleteByPrimaryKey(id);
            //删除子项配置事项表
            situationChildAndDmModelService.deleteBySituationChildId(id);
        });
    }

    /**
     * 条件查询
     *
     * @param situationToGuideChildSelectReq 条件
     * @return map
     */
    @Override
    public Map<String, Object> selectByCondition(SituationToGuideChildSelectReq situationToGuideChildSelectReq) {

        if (situationToGuideChildSelectReq.getPageBean() != null) {
            PageHelper.startPage(situationToGuideChildSelectReq.getPageBean().getCurrentPage(), situationToGuideChildSelectReq.getPageBean().getPageSize());
        }
        // 删除原有的
        Condition condition = new Condition(SituationToGuideChild.class);
        //条件
        Example.Criteria fieldCriteria = condition.createCriteria();

        if (!StringUtils.isEmpty(situationToGuideChildSelectReq.getEventName())) {
            fieldCriteria.andLike("eventName", "%"+situationToGuideChildSelectReq.getEventName()+"%");
        }
        if (!StringUtils.isEmpty(situationToGuideChildSelectReq.getServiceObject())) {
            fieldCriteria.andLike("serviceObject", "%"+situationToGuideChildSelectReq.getServiceObject()+"%");
        }
        if (!StringUtils.isEmpty(situationToGuideChildSelectReq.getExerciseHierarchy())) {
            fieldCriteria.andLike("exerciseHierarchy","%"+ situationToGuideChildSelectReq.getExerciseHierarchy()+"%");
        }
        if (!StringUtils.isEmpty(situationToGuideChildSelectReq.getHandlingForm())) {
            fieldCriteria.andLike("handlingForm", "%"+situationToGuideChildSelectReq.getHandlingForm()+"%");
        }

        //查询
        List<SituationToGuideChild> situationToGuideChildrenList = situationToGuideChildDao.selectByCondition(condition);

        PageInfo<SituationToGuideChild> pageInfo = new PageInfo<>(situationToGuideChildrenList);
        Map<String, Object> map = new HashMap<>();
        map.put("total", pageInfo.getTotal());
        map.put("list", pageInfo.getList());
        PageHelper.clearPage();
        return map;
    }

    /**
     * 添加/修改情形引导子项
     *
     * @param situationToGuideChild 情形引导子项表单
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveFrom(SituationToGuideChild situationToGuideChild) {

        //新增/修改
        if (StringUtils.isEmpty(situationToGuideChild.getId())) {
            situationToGuideChild.setId(SysUtil.getUUID());
            situationToGuideChildDao.insertSelective(situationToGuideChild);

        } else {
            situationToGuideChildDao.updateByPrimaryKeySelective(situationToGuideChild);
        }
    }


    /**
     * 名称是否重复
     *
     * @param idAndNameReq id和名称
     * @return boolean
     */
    @Override
    public boolean checkDuplicateName(IdAndNameReq idAndNameReq) {

        boolean bool = true;
        // 删除原有的
        Condition fieldCondition = new Condition(SituationToGuideChild.class);
        Example.Criteria fieldCriteria = fieldCondition.createCriteria();
        fieldCriteria.andEqualTo("eventName", idAndNameReq.getName());

        if (StringUtils.isEmpty(idAndNameReq.getId())) {
            fieldCriteria.andNotEqualTo("id", idAndNameReq.getId());
        }
        int count = situationToGuideChildDao.selectCountByCondition(fieldCondition);

        if (count > 0) {
            bool = false;
        }
        return bool;
    }

    /**
     * 配置事项
     *
     * @param configurationMattersReq 配置参数
     */
    @Override
    public void configurationMatters(ConfigurationMattersReq configurationMattersReq) {

        //删除中间表数据
        situationChildAndDmModelService.deleteBySituationChildId(configurationMattersReq.getId());


        //新增
        configurationMattersReq.getDepartmentalMattersList()
                .forEach(departmentalMattersId -> {
                    SituationChildAndDmModel situationChildAndDmModel = new SituationChildAndDmModel();

                    situationChildAndDmModel.setId(SysUtil.getUUID());
                    situationChildAndDmModel.setSituationChildId(configurationMattersReq.getId());
                    situationChildAndDmModel.setDepartmentalMattersId(departmentalMattersId);
                    situationChildAndDmModelService.save(situationChildAndDmModel);
                });
    }

    @Override
    public List<DepartmentalMattersRes> selectConfigurationMatters(String id) {

        //查询列表返回事项
        return departmentalMattersDao.selectEventIdListBySituationChildId(id);
    }

    /**
     * 单个删除
     *
     * @param id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String id) {

        //删除子项表
        situationToGuideChildDao.deleteByPrimaryKey(id);
        //删除子项配置事项表
        situationChildAndDmModelService.deleteBySituationChildId(id);
    }
}