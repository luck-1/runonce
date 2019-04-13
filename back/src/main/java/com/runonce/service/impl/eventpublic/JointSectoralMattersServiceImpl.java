package com.runonce.service.impl.eventpublic;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.runonce.dao.eventpublic.EventsTableDao;
import com.runonce.dao.eventpublic.JointSectoralMattersDao;
import com.runonce.dao.eventversionone.DepartmentalMattersDao;
import com.runonce.dao.system.DepartmentDao;
import com.runonce.httpbean.assets.reqbean.*;
import com.runonce.model.eventpublic.EventsTable;
import com.runonce.model.eventpublic.JointSectoralMatterReq;
import com.runonce.model.eventversionone.*;
import com.runonce.model.system.Department;
import com.runonce.service.eventpublic.JointSectoralMattersService;
import com.runonce.service.eventversionone.*;
import com.runonce.service.system.DepartmentService;
import com.runonce.util.SysUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.Column;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JointSectoralMattersServiceImpl implements JointSectoralMattersService {

    @Autowired
    private DepartmentDao  departmentDao;
    @Resource
    private EventsTableDao eventsTableDao;
    @Resource
    private DepartmentalMattersDao departmentalMattersDao;
    @Autowired
    private DepartmentalMattersService departmentalMattersService;

    @Resource
    private JointSectoralMattersDao jointSectoralMattersDao;

    @Resource
    private EventDirectoryService  eventDirectoryService;

    @Resource
    private VersionmanagementService versionmanagementService;

    @Autowired
    private BusinessGuideAdministrativeLicensingOneService businessGuideAdministrativeLicensingOneService;

    @Autowired
    private BusinessGuideAuditAndForwardingCategoryOneService businessGuideAuditAndForwardingCategoryOneService;

    @Autowired
    private BusinessGuideFilingOneService businessGuideFilingOneService;

    @Autowired
    private BusinessGuideOtherServicesOneService businessGuideOtherServicesOneService;

    @Resource
    private TasktodoService tasktodoService;

    @Resource
    private DepartmentService departmentService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public EventsTable add(JointSectoralMattersReq jointSectoralMattersReq,HttpServletRequest request) {

        //部门ID
        String deptId=jointSectoralMattersReq.getDeptId();

        //组ID
        String groupId = jointSectoralMattersReq.getGroupId();

        //是否可复制
         Integer  isReproducible=jointSectoralMattersReq.getIsReproducible();

        //事项
        EventsTable eventsTable =jointSectoralMattersReq.getEventsTable();

        //目录最大目录
        //如果是目录
        // 子项
        //{ 判断是否是市级
        // 否
        // 新增 添加groupId
        //其他级区查询市级的菜单
        //
        //否
        //
        //
        // }

        //如果是子项
        //  设置ID 直接新增

        String thisDmId = null;
        String eventsTableId=eventsTable.getId();

        if(eventsTableId==null||"".equals(eventsTableId)){
            eventsTableId= SysUtil.getUUID();
        }

        if(groupId==null||"".equals(groupId)){
            groupId= SysUtil.getUUID();
        }

       Department department= departmentDao.getOne(deptId);
        if(!"2".equals(department.getRegion())&&"0".equals(eventsTable.getEventPid())){

            //插入事项
            EventsTable e= new EventsTable();
            BeanUtils.copyProperties(eventsTable, e);
            e.setId(eventsTableId);
            eventsTableDao.insertSelective(e);
            //插入事项
            DepartmentalMatters departmentalMatters = new DepartmentalMatters();
            String departmentalMattersId=SysUtil.getUUID();
            departmentalMatters.setId(departmentalMattersId);
            departmentalMatters.setDeptId(deptId);
            departmentalMatters.setEventId(eventsTableId);
            departmentalMatters.setGroupId(groupId);
            departmentalMatters.setIsReproducible(0);
            departmentalMatters.setMappingId(departmentalMattersId);
            departmentalMattersDao.insertSelective(departmentalMatters);
            setDmVersion(departmentalMattersId);

            //爬树找市级部门事项
           // departmentDao.
           List<JointSectoralMatters> eventsTableList= jointSectoralMattersDao.selectMunicipalEvent(eventsTable.getEventName());

           // eventsTableList.stream().filter( getEventType())
            List<JointSectoralMatters> pList = eventsTableList.stream().filter(item1 -> "0".equals(item1.getEventPid())).collect(Collectors.toList());

            if(pList!=null&&pList.size()>0){
            List<JointSectoralMatters> newList=new ArrayList<>();

            //递归插入
            getBelongEvent(pList.get(0).getId(),eventsTableId,eventsTableList,newList);

            if(newList!=null){
            for (JointSectoralMatters eventsTable1:
                    newList) {
                if(!"0".equals(eventsTable1.getEventPid())){


                    String eventIdSon=eventsTable1.getId();
                    //eventsTable.setId(eventIdSon);

                    EventsTable e1= new EventsTable();
                    BeanUtils.copyProperties(eventsTable1, e1);
                    //e1.getEventPid();
                    e1.setId(eventIdSon);
                    eventsTableDao.insertSelective(e1);
                    //插入事项
                    DepartmentalMatters departmentalMatters1 = new DepartmentalMatters();
                    String departmentalMattersId1=SysUtil.getUUID();
                    departmentalMatters1.setId(departmentalMattersId1);
                    departmentalMatters1.setDeptId(deptId);
                    departmentalMatters1.setEventId(eventIdSon);
                    departmentalMatters1.setGroupId(groupId);
                    departmentalMatters1.setMappingId(eventsTable1.getDepartmentalMattersId());
                    departmentalMatters1.setMaterialGroupingState(2);
                    departmentalMatters1.setApplicationTypeState(2);
                    departmentalMatters1.setPrequalificationState(2);
                    departmentalMatters1.setProcessDescriptionState(2);
                    departmentalMatters1.setProcessMapState(2);
                    departmentalMatters1.setIsReproducible(0);
                    departmentalMatters1.setBusinessSituationState(2);
                    departmentalMatters1.setFormThatState(2);
                    departmentalMatters1.setPresentQuantitativeState(2);
                    departmentalMatters1.setSystemDockingState(2);
                    departmentalMattersDao.insertSelective(departmentalMatters1);

                    thisDmId = departmentalMattersId1;
                    if(e1.getIsMin()) {
                        setDmVersion(departmentalMattersId1);
                        initBusinessGuide( e.getEventType(), departmentalMattersId1,request);
                        List<String> userIdList=departmentService.findDeptGly(deptId);
                        tasktodoService.sendTodo(departmentalMattersId1,e1.getEventName(),1,userIdList,"事项已分配，请及时完善");
                    }
                }
            }}}
        }else {

            if(isReproducible==null){

                EventDirectory returnEd=  eventDirectoryService.findById(eventsTable.getEventName());

                if(returnEd!=null){

                    if(returnEd.getIsPrivate()!=null&&returnEd.getIsPrivate()==true){

                        isReproducible=0;

                    }

                }

            }


            //插入事项
            EventsTable e= new EventsTable();
            BeanUtils.copyProperties(eventsTable, e);
            e.setId(eventsTableId);
            eventsTableDao.insertSelective(e);


            //插入事项
            DepartmentalMatters departmentalMatters = new DepartmentalMatters();
            String departmentalMattersId=SysUtil.getUUID();
            departmentalMatters.setId(departmentalMattersId);
            departmentalMatters.setDeptId(deptId);
            departmentalMatters.setEventId(eventsTableId);
            departmentalMatters.setIsReproducible(isReproducible);
            departmentalMatters.setGroupId(groupId);
            departmentalMatters.setMappingId(departmentalMattersId);
            departmentalMattersDao.insertSelective(departmentalMatters);
            if(e.getIsMin()) {
                setDmVersion(departmentalMattersId);
                initBusinessGuide( e.getEventType(), departmentalMattersId,request);
                //推送消息
                List<String> userIdList=departmentService.findDeptGly(deptId);
                tasktodoService.sendTodo(departmentalMattersId,e.getEventName(),1,userIdList,"事项已分配，请及时完善");
            }
            thisDmId = departmentalMattersId;
            //插入事项版本
        }

        if(thisDmId != null){
            //获取事项id 是否有父
            String departmentalMattersPid = departmentalMattersDao.selectPidById(thisDmId);
            //更新父部门状态
            departmentalMattersService.updateFatherState(departmentalMattersPid,1);
        }
        eventsTable.setGroupId(groupId);
        return eventsTable;
    }


    public void getBelongEvent(String eventId,String newEventId, List<JointSectoralMatters> allDept, List<JointSectoralMatters> departmentsIdList) {

        List<JointSectoralMatters> list = allDept.stream().filter(a -> a.getEventPid().equals(eventId)).collect(Collectors.toList());
        if (list != null && list.size() > 0) {


            for (JointSectoralMatters item : list) {
                String newid=SysUtil.getUUID();
                getBelongEvent(item.getId(),newid, allDept, departmentsIdList);
                item.setId(newid);
                item.setEventPid(newEventId);
                departmentsIdList.add(item);

            }

        }
    }


    @Transactional(rollbackFor = Exception.class)
    public void  initBusinessGuide(Integer code , String dmId,HttpServletRequest request){

        DepartmentalMatters departmentalMatters = departmentalMattersDao.selectByPrimaryKey(dmId);

        switch (code){
            case 1 :
                BusinessGuideAdministrativeLicensingOne businessGuideAdministrativeLicensingOne = new BusinessGuideAdministrativeLicensingOne();
                businessGuideAdministrativeLicensingOne.setEventId(dmId);

                if(departmentalMatters.getId().equals(departmentalMatters.getMappingId())){
                    businessGuideAdministrativeLicensingOne.setItemSmallType("");
                    businessGuideAdministrativeLicensingOne.setSourceOfPower("");
                    businessGuideAdministrativeLicensingOne.setNatureOfImplementingSubject("");
                    businessGuideAdministrativeLicensingOne.setServiceTopic("");
                    businessGuideAdministrativeLicensingOne.setTypeOfOffice("");
                    businessGuideAdministrativeLicensingOne.setExerciseHierarchy("");
                    businessGuideAdministrativeLicensingOne.setTimeLimitType("");
                    businessGuideAdministrativeLicensingOne.setPaymentMethod("");
                    businessGuideAdministrativeLicensingOne.setMethodOfDeliveryOfApplicationMaterials("");
                    businessGuideAdministrativeLicensingOne.setConcierge("");
                    businessGuideAdministrativeLicensingOne.setScopeOfOperation("");
                    businessGuideAdministrativeLicensingOne.setHandlingForm("");
                    businessGuideAdministrativeLicensingOne.setOnlineApplicationForm("");
                    businessGuideAdministrativeLicensingOne.setNumberOfVisitsToTheFieldHall("");
                    businessGuideAdministrativeLicensingOne.setEventStar("");
                    businessGuideAdministrativeLicensingOne.setPrePublicationPublicity("");
                    businessGuideAdministrativeLicensingOne.setResultType("");
                    businessGuideAdministrativeLicensingOne.setAnnualExaminationOrAnnualInspection("");
                    businessGuideAdministrativeLicensingOne.setDecidedToMakePublic("");
                    businessGuideAdministrativeLicensingOne.setAppointmentProcessing("");
                    businessGuideAdministrativeLicensingOne.setPermissionDivision("");
//                    businessGuideAdministrativeLicensingOne.setServiceObject(new ArrayList<>());
                }

                BusinessGuideAdministrativeLicensingOnePrivate businessGuideAdministrativeLicensingOnePrivate = new BusinessGuideAdministrativeLicensingOnePrivate();
                businessGuideAdministrativeLicensingOnePrivate.setDepartmentalMattersId(dmId);
                businessGuideAdministrativeLicensingOnePrivate.setState(1);
                businessGuideAdministrativeLicensingOneService.add(businessGuideAdministrativeLicensingOne,businessGuideAdministrativeLicensingOnePrivate,request);
                break;
            case 2 :
                BusinessGuideAuditAndForwardingCategoryOne businessGuideAuditAndForwardingCategoryOne = new BusinessGuideAuditAndForwardingCategoryOne();
                businessGuideAuditAndForwardingCategoryOne.setEventId(dmId);

                if(departmentalMatters.getId().equals(departmentalMatters.getMappingId())){

                    businessGuideAuditAndForwardingCategoryOne.setItemSmallType("");
                    businessGuideAuditAndForwardingCategoryOne.setServiceTopic("");
                    businessGuideAuditAndForwardingCategoryOne.setAnnualExaminationOrAnnualInspection("");
                    businessGuideAuditAndForwardingCategoryOne.setMethodOfDeliveryOfApplicationMaterials("");
                    businessGuideAuditAndForwardingCategoryOne.setHandlingForm("");
                    businessGuideAuditAndForwardingCategoryOne.setOnlineApplicationForm("");
                    businessGuideAuditAndForwardingCategoryOne.setEventStar("");
                    businessGuideAuditAndForwardingCategoryOne.setExerciseHierarchy("");
//                    businessGuideAuditAndForwardingCategoryOne.setServiceObject(new ArrayList<>());
                }


                BusinessGuideAuditAndForwardingCategoryOnePrivate businessGuideAuditAndForwardingCategoryOnePrivate=new BusinessGuideAuditAndForwardingCategoryOnePrivate();
                businessGuideAuditAndForwardingCategoryOnePrivate.setDepartmentalMattersId(dmId);
                businessGuideAuditAndForwardingCategoryOnePrivate.setState(1);
                businessGuideAuditAndForwardingCategoryOneService.add(businessGuideAuditAndForwardingCategoryOne,businessGuideAuditAndForwardingCategoryOnePrivate,request);
                break;
            case 3 :
                BusinessGuideFilingOne businessGuideFilingOne= new BusinessGuideFilingOne();
                businessGuideFilingOne.setEventId(dmId);

                if(departmentalMatters.getId().equals(departmentalMatters.getMappingId())){

                    businessGuideFilingOne.setItemSmallType("");
                    businessGuideFilingOne.setSourceOfPower("");
                    businessGuideFilingOne.setNatureOfImplementingSubject("");
                    businessGuideFilingOne.setServiceTopic("");
                    businessGuideFilingOne.setTypeOfOffice("");
                    businessGuideFilingOne.setExerciseHierarchy("");
                    businessGuideFilingOne.setTimeLimitType("");
                    businessGuideFilingOne.setPaymentMethod("");
                    businessGuideFilingOne.setMethodOfDeliveryOfApplicationMaterials("");
                    businessGuideFilingOne.setConcierge("");
                    businessGuideFilingOne.setScopeOfOperation("");
                    businessGuideFilingOne.setHandlingForm("");
                    businessGuideFilingOne.setOnlineApplicationForm("");
                    businessGuideFilingOne.setNumberOfVisitsToTheFieldHall("");
                    businessGuideFilingOne.setEventStar("");
                    businessGuideFilingOne.setPrePublicationPublicity("");
                    businessGuideFilingOne.setResultType("");
                    businessGuideFilingOne.setAnnualExaminationOrAnnualInspection("");
                    businessGuideFilingOne.setDecidedToMakePublic("");
                    businessGuideFilingOne.setAppointmentProcessing("");
                    businessGuideFilingOne.setPermissionDivision("");
//                    businessGuideFilingOne.setServiceObject(new ArrayList<>());
                }


                BusinessGuideFilingOnePrivate businessGuideFilingOnePrivate = new BusinessGuideFilingOnePrivate();
                businessGuideFilingOnePrivate.setDepartmentalMattersId(dmId);
                businessGuideFilingOnePrivate.setState(1);
                businessGuideFilingOneService.add(businessGuideFilingOne,businessGuideFilingOnePrivate,request);
                break;
            case 4 :
                BusinessGuideOtherServicesOne businessGuideOtherServicesOne= new BusinessGuideOtherServicesOne();
                businessGuideOtherServicesOne.setEventId(dmId);

                if(departmentalMatters.getId().equals(departmentalMatters.getMappingId())){

                    businessGuideOtherServicesOne.setItemSmallType("");
                    businessGuideOtherServicesOne.setServiceTopic("");
                    businessGuideOtherServicesOne.setExerciseHierarchy("");
                    businessGuideOtherServicesOne.setTypeOfOffice("");
                    businessGuideOtherServicesOne.setTimeLimitType("");
                    businessGuideOtherServicesOne.setMethodOfDeliveryOfApplicationMaterials("");
                    businessGuideOtherServicesOne.setHandlingForm("");
                    businessGuideOtherServicesOne.setNumberOfVisitsToTheFieldHall("");
                    businessGuideOtherServicesOne.setResultType("");
//                    businessGuideOtherServicesOne.setServiceObject(new ArrayList<>());
                }


                BusinessGuideOtherServicesOnePrivate  businessGuideOtherServicesOnePrivate = new BusinessGuideOtherServicesOnePrivate();
                businessGuideOtherServicesOnePrivate.setDepartmentalMattersId(dmId);
                businessGuideOtherServicesOnePrivate.setState(1);
                businessGuideOtherServicesOneService.add(businessGuideOtherServicesOne,businessGuideOtherServicesOnePrivate,request);
                break;
        }
    }


    @Transactional(rollbackFor = Exception.class)
    public void setDmVersion(String dmId){
        Versionmanagement  vm ;
        for(int i = 0 ; i <=10 ; i++){
                vm = new Versionmanagement();
            vm.setId(SysUtil.getUUID());
            vm.setDmId(dmId);
            vm.setSheetNumber(i+"");
            vm.setVersion(1);
            versionmanagementService.save(vm);
        }
    }






    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer deleteById(String id) {

        //获取事项id 是否有父
        String departmentalMattersPId = departmentalMattersDao.selectSonPidByParId(id);
        eventsTableDao.deleteByPrimaryKey(id);
        departmentalMattersDao.deleteByEventId(id);
        //更新父状态
        departmentalMattersService.updateFatherState(departmentalMattersPId);

        return 1;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JointSectoralMattersReq update(JointSectoralMattersReq jointSectoralMattersReq) {

        EventsTable eventsTable=jointSectoralMattersReq.getEventsTable();
        eventsTableDao.updateByPrimaryKeySelective(eventsTable);

        return jointSectoralMattersReq;
    }

    @Override
    public PageInfo<EventsTable> selectDistributedEvents(JointSectoralMatterReq jointSectoralMatterReq) {

       // PageHelper.startPage(jointSectoralMatterReq.getCurrentPage(), jointSectoralMatterReq.getPageSize());
        List<EventsTable> events = eventsTableDao.selectDistributedEvents(jointSectoralMatterReq);
        PageInfo<EventsTable> pageInfo = new PageInfo<>(events);
        pageInfo.setTotal(eventsTableDao.selectDistributedEventsCount(jointSectoralMatterReq));

        return pageInfo;
    }


}
