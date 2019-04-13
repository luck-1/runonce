package com.runonce.service.impl.eventversionone;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.runonce.ReturnCode;
import com.runonce.dao.base.*;
import com.runonce.dao.eventversionone.BusinessGuideAdministrativeLicensingOnePrivateDao;
import com.runonce.dao.eventversionone.DepartmentalMattersDao;
import com.runonce.dao.eventversionone.VersionmanagementDao;
import com.runonce.exception.CustomException;
import com.runonce.httpbean.assets.reqbean.BusinessGuideAdministrativeLicensingOneReq;
import com.runonce.model.eventversionone.BusinessGuideAdministrativeLicensingOnePrivate;
import com.runonce.model.eventversionone.DepartmentalMatters;
import com.runonce.service.base.CatalogueItemSubtypeService;
import com.runonce.service.eventversionone.DepartmentalMattersService;
import com.runonce.util.SysUtil;
import com.runonce.util.WebTokenUtil;
import org.springframework.stereotype.Service;
import com.runonce.service.eventversionone.BusinessGuideAdministrativeLicensingOneService;
import com.runonce.dao.eventversionone.BusinessGuideAdministrativeLicensingOneDao;
import com.runonce.service.AbstractService;
import com.runonce.model.eventversionone.BusinessGuideAdministrativeLicensingOne;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by xuxueli on '2018-12-10 11:31:13'.
 */
@Service
public class BusinessGuideAdministrativeLicensingOneServiceImpl extends AbstractService<BusinessGuideAdministrativeLicensingOne> implements BusinessGuideAdministrativeLicensingOneService {

    @Resource
    private BusinessGuideAdministrativeLicensingOneDao businessGuideAdministrativeLicensingOneDao;

    @Resource
    private DepartmentalMattersDao departmentalMattersDao;

    @Resource
    private DepartmentalMattersService departmentalMattersService;

    @Resource
    private BusinessGuideAdministrativeLicensingOnePrivateDao businessGuideAdministrativeLicensingOnePrivateDao;

    @Resource
    private VersionmanagementDao versionmanagementDao;

    @Resource
    private CatalogueEventTypeDao catalogueEventTypeDao;

    @Resource
    private CatalogueItemSubtypeService catalogueItemSubtypeService;

    @Resource
    private ServiceTopicDao serviceTopicDao;

    @Resource
    private SourceOfPowerDao sourceOfPowerDao;

    @Resource
    private PermissionDivisionDao permissionDivisionDao;

    @Resource
    private NatureOfImplementingSubjectDao natureOfImplementingSubjectDao;

    @Resource
    private ExerciseHierarchyDao exerciseHierarchyDao;

    @Resource
    private TypeOfOfficeDao typeOfOfficeDao;

    @Resource
    private AnnualReviewOrViewDao annualReviewOrViewDao;

    @Resource
    private ConciergeDao conciergeDao;

    @Resource
    private ScopeOfOperationDao scopeOfOperationDao;

    @Resource
    private OnlineApplicationFormDao onlineApplicationFormDao;

    @Resource
    private EventStarDao eventStarDao;

    @Resource
    private AppointmentProcessingDao appointmentProcessingDao;

    @Resource
    private PrePublicationPublicityDao prePublicationPublicityDao;

    @Resource
    private DecidedToMakePublicDao decidedToMakePublicDao;

    @Resource
    private IsLogisticsDao isLogisticsDao;

    @Resource
    private HandlingFormDao handlingFormDao;
//
//    @Resource
//    private MaterialTypeDao materialTypeDao;
//
//    @Resource
//    private MaterialSpecificationDao materialSpecificationDao;
//
//    @Resource
//    private SourceChannelDao sourceChannelDao;
//
//    @Resource
//    private ChannelDescriptionDao channelDescriptionDao;
//
//    @Resource
//    private BusinessAttachmentTypeDao businessAttachmentTypeDao;
//
//    @Resource
//    private CatalogueItemSubtypeDao catalogueItemSubtypeDao;
//
//
//


    /**
     * 从事件ID查询
     *
     * @param eventId
     */
    @Override
    public BusinessGuideAdministrativeLicensingOneReq findByEventId(String eventId) {

        BusinessGuideAdministrativeLicensingOneReq businessGuideAdministrativeLicensingOneReq = businessGuideAdministrativeLicensingOneDao.findByEventId(eventId);
        if(businessGuideAdministrativeLicensingOneReq == null){
            return null;
        }
        Integer version = versionmanagementDao.getSheetVersion(eventId,1);
        if(version == null ){
            throw new CustomException(new ReturnCode.Builder().failed().msg("未知错误：未查到当前版本！").build());
        }
        BusinessGuideAdministrativeLicensingOnePrivate businessGuideAdministrativeLicensingOnePrivate = businessGuideAdministrativeLicensingOneReq.getBusinessGuideAdministrativeLicensingOnePrivate();
        if(businessGuideAdministrativeLicensingOnePrivate == null){
            businessGuideAdministrativeLicensingOnePrivate = new BusinessGuideAdministrativeLicensingOnePrivate();
        }
        businessGuideAdministrativeLicensingOnePrivate.setVersion(version);


        return businessGuideAdministrativeLicensingOneReq;
    }
    /**
     * 从事件ID删除
     *
     * @param eventId
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByEventId(String eventId) {
        businessGuideAdministrativeLicensingOneDao.deleteByEventId(eventId);
    }

    /**
     * 保存
     * @param businessGuideAdministrativeLicensingOne
     * @param businessGuideAdministrativeLicensingOnePrivate
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ReturnCode add(BusinessGuideAdministrativeLicensingOne businessGuideAdministrativeLicensingOne,BusinessGuideAdministrativeLicensingOnePrivate businessGuideAdministrativeLicensingOnePrivate,HttpServletRequest request) {

        //获得对象数据
        String id = businessGuideAdministrativeLicensingOne.getId();
        String eventId = businessGuideAdministrativeLicensingOne.getEventId();

        //是否收费（特殊环节） 与  收费依据及描述（特殊环节）对应关系
        if ( businessGuideAdministrativeLicensingOne.getChargeSpecial() != null){
            if ("否".equals(businessGuideAdministrativeLicensingOne.getChargeSpecial())) {
                businessGuideAdministrativeLicensingOne.setChargeBasisAndDescriptionSpecial(null);
            }
        }
        //是否网上服务与（网上服务地址）对应关系
        if(businessGuideAdministrativeLicensingOne.getOnlineServiceSystem() != null ){
            if ("否".equals(businessGuideAdministrativeLicensingOne.getOnlineServiceSystem())) {
                businessGuideAdministrativeLicensingOne.setWebSiteOfOnlineServiceSystem(null);
            }
        }
        //是否收费和（收费标准，收费依据，支付方式）对应关系
        if(businessGuideAdministrativeLicensingOne.getCharge() != null ){
            if ("否".equals(businessGuideAdministrativeLicensingOne.getCharge())) {
                businessGuideAdministrativeLicensingOne.setChargingStandard(null);
                businessGuideAdministrativeLicensingOne.setChargingBasis(null);
                businessGuideAdministrativeLicensingOne.setPaymentMethod(null);
            }
        }
        //判断ID是否存在，存在更新，不存在新增
        if (id == null || "".equals(id)) {
            BusinessGuideAdministrativeLicensingOneReq businessGuideAdministrativeLicensingOneReq = businessGuideAdministrativeLicensingOneDao.findByEventId(eventId);
            if(businessGuideAdministrativeLicensingOneReq != null ){
                businessGuideAdministrativeLicensingOneDao.deleteByEventId(eventId);
            }
            //此事件ID第一次请求，新增数据
            businessGuideAdministrativeLicensingOne.setId(SysUtil.getUUID());
            businessGuideAdministrativeLicensingOneDao.insert(businessGuideAdministrativeLicensingOne);
        } else {
            //正常带ID ，更改
            businessGuideAdministrativeLicensingOneDao.updateByPrimaryKey(businessGuideAdministrativeLicensingOne);
        }
        //保存私有字段
        savePrivate(businessGuideAdministrativeLicensingOnePrivate,request);

        return new ReturnCode.Builder().success().msg("办事指南保存成功").object(businessGuideAdministrativeLicensingOne).build();
    }

    /**
     * 保存Privte信息
     *
     * @param businessGuideAdministrativeLicensingOnePrivate
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void savePrivate(BusinessGuideAdministrativeLicensingOnePrivate businessGuideAdministrativeLicensingOnePrivate,HttpServletRequest request) {

        String id = businessGuideAdministrativeLicensingOnePrivate.getDepartmentalMattersId();
        BusinessGuideAdministrativeLicensingOnePrivate selectBusinessGuideAdministrativeLicensingOnePrivate = businessGuideAdministrativeLicensingOnePrivateDao.selectByPrimaryKey(id);

        //事项取消分配情况（）
        DepartmentalMatters departmentalMatters = departmentalMattersDao.findState(id);
        if (departmentalMatters == null) {
            throw new CustomException(new ReturnCode.Builder().failed().msg("该事项不存在").build());
        }

        //数据库中存储的版本号
        Integer version = versionmanagementDao.getSheetVersion(id,1);

        if (selectBusinessGuideAdministrativeLicensingOnePrivate == null) {
            //新增
            businessGuideAdministrativeLicensingOnePrivateDao.insert(businessGuideAdministrativeLicensingOnePrivate);
        } else {
            //前端得到的版本号（点击配置按钮时的版本）
            Integer version1 = businessGuideAdministrativeLicensingOnePrivate.getVersion();
            if(version1 == null ){
                version1 = version;
            }
            //版本号相同，修改，不相同回滚
            if(version1.equals(version)){
                //修改
                businessGuideAdministrativeLicensingOnePrivateDao.updateByPrimaryKey(businessGuideAdministrativeLicensingOnePrivate);
            }else{
                throw new CustomException(new ReturnCode.Builder().failed().msg("当前已有人在编辑，更改失败！").build());
            }
        }
        //更新办事指南状态
        departmentalMatters.setBusinessGuideState(businessGuideAdministrativeLicensingOnePrivate.getState());
        //更新修改用户
        departmentalMatters.setOperator(WebTokenUtil.getUserByWebToken(request));
        departmentalMattersDao.updateByPrimaryKey(departmentalMatters);
        //更新数据库中的版本号
        versionmanagementDao.updateSheetVersion(id,1);
        //获取事项id 是否有父
        String departmentalMattersPId = departmentalMattersDao.selectPidById(businessGuideAdministrativeLicensingOnePrivate.getDepartmentalMattersId());
        //更新父部门状态
        departmentalMattersService.updateFatherState(departmentalMattersPId);
    }

    @Override
    public Map<String,List> getAllList(String eventType){

        Map<String,List> map = new HashMap<>();
        //四种类型公共下拉

        //事项大类型
        map.put("majorTypesOfEvents",catalogueEventTypeDao.selectAll());

        //事项小类型
        map.put("itemSmallType",catalogueItemSubtypeService.findByPid(eventType));

        //服务主题
        map.put("serviceTopic",serviceTopicDao.selectAll());

        //行使层级
        map.put("exerciseHierarchy",exerciseHierarchyDao.selectAll());

        //申请材料递送方式
        map.put("methodOfDeliveryOfApplicationMaterials",isLogisticsDao.selectAll());

        //申请材料递送方式
        map.put("handlingForm",handlingFormDao.selectAll());

        if("3".equals(eventType)){
            eventType = "1";
        }
        //非公有类型
        switch(eventType){
            case "1":
                //权力来源
                map.put("sourceOfPower",sourceOfPowerDao.selectAll());

                //实施主体性质
                map.put("natureOfImplementingSubject",natureOfImplementingSubjectDao.selectAll());

                //办件类型
                map.put("typeOfOffice",typeOfOfficeDao.selectAll());

//                //时限类型

//                //支付方式

                //委托代办
                map.put("concierge",conciergeDao.selectAll());

                //通办范围
                map.put("scopeOfOperation",scopeOfOperationDao.selectAll());

                //网上申请形式
                map.put("onlineApplicationForm",onlineApplicationFormDao.selectAll());

//                //到现场大厅（窗口）次数

                //事项星级
                map.put("eventStar",eventStarDao.selectAll());

                //批前公示
                map.put("prePublicationPublicity",prePublicationPublicityDao.selectAll());

//                //结果类型

                //年审或年检
                map.put("annualExaminationOrAnnualInspection",annualReviewOrViewDao.selectAll());

                //决定公开
                map.put("decidedToMakePublic",decidedToMakePublicDao.selectAll());

                //预约办理
                map.put("appointmentProcessing",appointmentProcessingDao.selectAll());

                //权限划分
                map.put("permissionDivision",permissionDivisionDao.selectAll());

                break;
            case "2":
                //网上申请形式
                map.put("onlineApplicationForm",onlineApplicationFormDao.selectAll());

                //事项星级
                map.put("eventStar",eventStarDao.selectAll());

                //年审或年检
                map.put("annualExaminationOrAnnualInspection",annualReviewOrViewDao.selectAll());

                break;
            case "4":
                //办件类型
                map.put("typeOfOffice",typeOfOfficeDao.selectAll());

//                //时限类型

//                //到现场大厅（窗口）次数

//                //结果类型

                break;
            default:
                throw new CustomException(new ReturnCode.Builder().failed().msg("类型错误").build());

        }

        return map;
    }
}
