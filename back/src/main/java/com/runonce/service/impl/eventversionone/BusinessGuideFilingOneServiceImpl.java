package com.runonce.service.impl.eventversionone;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.runonce.ReturnCode;
import com.runonce.dao.base.CatalogueEventTypeDao;
import com.runonce.dao.base.ServiceObjectDao;
import com.runonce.dao.eventpublic.EventsTableDao;
import com.runonce.dao.eventversionone.BusinessGuideFilingOnePrivateDao;
import com.runonce.dao.eventversionone.DepartmentalMattersDao;
import com.runonce.dao.eventversionone.VersionmanagementDao;
import com.runonce.exception.CustomException;
import com.runonce.httpbean.assets.reqbean.BusinessGuideFilingOneReq;
import com.runonce.model.base.ServiceBusinessguideiModle;
import com.runonce.model.base.ServiceObject;
import com.runonce.model.eventpublic.EventsTable;
import com.runonce.model.eventversionone.BusinessGuideFilingOnePrivate;
import com.runonce.model.eventversionone.DepartmentalMatters;
import com.runonce.model.http.EventsTableParam;
import com.runonce.service.base.ServiceBusinessguideiModleService;
import com.runonce.service.eventversionone.BusinessGuideAdministrativeLicensingOneService;
import com.runonce.service.eventversionone.DepartmentalMattersService;
import com.runonce.util.SysUtil;
import com.runonce.util.WebTokenUtil;
import org.springframework.stereotype.Service;
import com.runonce.service.eventversionone.BusinessGuideFilingOneService;
import com.runonce.dao.eventversionone.BusinessGuideFilingOneDao;
import com.runonce.service.AbstractService;
import com.runonce.model.eventversionone.BusinessGuideFilingOne;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xuxueli on '2018-12-10 11:31:13'.
 */
@Service
public class BusinessGuideFilingOneServiceImpl extends AbstractService<BusinessGuideFilingOne> implements BusinessGuideFilingOneService {

    @Resource
    private BusinessGuideFilingOneDao businessGuideFilingOneDao;

    @Resource
    private DepartmentalMattersDao departmentalMattersDao;

    @Resource
    private DepartmentalMattersService departmentalMattersService;

    @Resource
    private BusinessGuideFilingOnePrivateDao businessGuideFilingOnePrivateDao;

    @Resource
    private VersionmanagementDao versionmanagementDao;

    /**
     * 从事件ID查询
     *
     * @param eventId
     */
    @Override
    public BusinessGuideFilingOneReq findByEventId(String eventId) {

        BusinessGuideFilingOneReq businessGuideFilingOneReq = businessGuideFilingOneDao.findByEventId(eventId);
        if(businessGuideFilingOneReq == null){
            return null;
        }
        Integer version = versionmanagementDao.getSheetVersion(eventId,1);
        if(version == null ){
            throw new CustomException(new ReturnCode.Builder().failed().msg("未知错误：未查到当前版本！").build());
        }

        BusinessGuideFilingOnePrivate businessGuideFilingOnePrivate = businessGuideFilingOneReq.getBusinessGuideFilingOnePrivate();
        if(businessGuideFilingOnePrivate == null){
            businessGuideFilingOnePrivate = new BusinessGuideFilingOnePrivate();
        }
        businessGuideFilingOnePrivate.setVersion(version);
        return businessGuideFilingOneReq;
    }

    /**
     * 从事件ID删除
     *
     * @param eventId
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByEventId(String eventId) {
        businessGuideFilingOneDao.deleteByEventId(eventId);
    }

    /**
     * 保存
     *
     * @param businessGuideFilingOne
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ReturnCode add(BusinessGuideFilingOne businessGuideFilingOne, BusinessGuideFilingOnePrivate businessGuideFilingOnePrivate, HttpServletRequest request) {
        //获得对象数据
        String id = businessGuideFilingOne.getId();
        String eventId = businessGuideFilingOne.getEventId();

        //是否收费（特殊环节） 与  收费依据及描述（特殊环节）对应关系
        if(businessGuideFilingOne.getChargeSpecial() != null ){
            if ("否".equals(businessGuideFilingOne.getChargeSpecial())) {
                businessGuideFilingOne.setChargeBasisAndDescriptionSpecial(null);
            }
        }

        //是否网上服务与（网上服务地址）对应关系
        if(businessGuideFilingOne.getOnlineServiceSystem() != null ){
            if ("否".equals(businessGuideFilingOne.getOnlineServiceSystem())) {
                businessGuideFilingOne.setWebSiteOfOnlineServiceSystem(null);
            }
        }

        //是否收费和（收费标准，收费依据，支付方式）对应关系
        if(businessGuideFilingOne.getCharge() != null ){
            if ("否".equals(businessGuideFilingOne.getCharge())) {
                businessGuideFilingOne.setChargingStandard(null);
                businessGuideFilingOne.setChargingBasis(null);
                businessGuideFilingOne.setPaymentMethod(null);
            }
        }


        //事项取消分配情况
        if (departmentalMattersDao.findState(eventId) == null) {
            return new ReturnCode.Builder().failed().msg("该事项不存在").build();
        }

        //判断ID是否存在，存在更新，不存在新增
        if (id == null || "".equals(id)) {
			//是否存在这个事件的ID，测试忘传ID情况BUG，存在则删除
            BusinessGuideFilingOneReq businessGuideFilingOneReq = businessGuideFilingOneDao.findByEventId(eventId);
            if(businessGuideFilingOneReq != null ){
                businessGuideFilingOneDao.deleteByEventId(eventId);
            }
            //此事件ID第一次请求，新增数据
            businessGuideFilingOne.setId(SysUtil.getUUID());
            businessGuideFilingOneDao.insert(businessGuideFilingOne);
        } else {
            //正常带ID ，更改
            businessGuideFilingOneDao.updateByPrimaryKey(businessGuideFilingOne);
        }
        //保存私有字段
        savePrivate(businessGuideFilingOnePrivate,request);

        return new ReturnCode.Builder().success().msg("办事指南保存成功").object(businessGuideFilingOne).build();
    }

    /**
     * 保存Private表信息
     *
     * @param businessGuideFilingOnePrivate
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void savePrivate(BusinessGuideFilingOnePrivate businessGuideFilingOnePrivate, HttpServletRequest request) {

        String id = businessGuideFilingOnePrivate.getDepartmentalMattersId();
        BusinessGuideFilingOnePrivate selectBusinessGuideFilingOnePrivate = businessGuideFilingOnePrivateDao.selectByPrimaryKey(id);

        //事项取消分配情况（）
        DepartmentalMatters departmentalMatters = departmentalMattersDao.findState(id);
        if (departmentalMatters == null) {
            throw new CustomException(new ReturnCode.Builder().failed().msg("该事项不存在").build());
        }
        //数据库中存储的版本号
        Integer version = versionmanagementDao.getSheetVersion(id,1);
        if (selectBusinessGuideFilingOnePrivate == null) {
            //新增
            businessGuideFilingOnePrivateDao.insert(businessGuideFilingOnePrivate);
        } else {
            //前端得到的版本号（点击配置按钮时的版本）
            Integer version1 = businessGuideFilingOnePrivate.getVersion();
            if(version1 == null ){
                version1 = version;
            }
            //版本号相同，修改，不相同回滚
            if(version1.equals(version)){
                //修改
                businessGuideFilingOnePrivateDao.updateByPrimaryKey(businessGuideFilingOnePrivate);
            }else{
                throw new CustomException(new ReturnCode.Builder().failed().msg("当前已有人在编辑，更改失败！").build());
            }
        }
        //更新办事指南状态
        departmentalMatters.setBusinessGuideState(businessGuideFilingOnePrivate.getState());
        //更新修改用户
        departmentalMatters.setOperator(WebTokenUtil.getUserByWebToken(request));
        departmentalMattersDao.updateByPrimaryKey(departmentalMatters);
        //更新数据库中的版本号
        versionmanagementDao.updateSheetVersion(id,1);
        //获取事项id 是否有父
        String departmentalMattersPId = departmentalMattersDao.selectPidById(businessGuideFilingOnePrivate.getDepartmentalMattersId());
        //更新父部门状态
        departmentalMattersService.updateFatherState(departmentalMattersPId);
    }


}
