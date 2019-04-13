package com.runonce.service.impl.eventpublic;

import javax.annotation.Resource;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.runonce.ReturnCode;
import com.runonce.dao.base.CatalogueItemSubtypeDao;
import com.runonce.dao.eventversionone.CentralBusinessGuidanceDepartmentCodeDao;
import com.runonce.dao.eventversionone.DepartmentalMattersDao;
import com.runonce.dao.system.DepartmentDao;
import com.runonce.dao.system.mapper.DepartmentMapper;
import com.runonce.enums.ResultEnum;
import com.runonce.exception.CustomException;
import com.runonce.model.base.CatalogueItemSubtype;
import com.runonce.model.eventversionone.CentralBusinessGuidanceDepartmentCode;
import com.runonce.model.eventversionone.DepartmentalMatters;
import com.runonce.model.http.DepartmentalMattersRes;
import com.runonce.model.http.EventDistributionParam;
import com.runonce.model.http.SelectDepartmentalMattersListParam;
import com.runonce.model.system.Department;
import com.runonce.redis.RedisObjectService;
import com.runonce.util.SysUtil;
import com.sun.org.apache.xerces.internal.xs.StringList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.runonce.service.eventpublic.EventsTableService;
import com.runonce.dao.eventpublic.EventsTableDao;
import com.runonce.service.AbstractService;
import com.runonce.model.eventpublic.EventsTable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by xuxueli on '2018-12-10 15:05:36'.
 */
@Service
public class EventsTableServiceImpl extends AbstractService<EventsTable> implements EventsTableService {

    @Resource
    private EventsTableDao eventsTableDao;

    @Resource
    private DepartmentalMattersDao departmentalMattersDao;

    @Resource
    private DepartmentMapper departmentMapper;

    @Resource
    private CentralBusinessGuidanceDepartmentCodeDao centralBusinessGuidanceDepartmentCodeDao;

    @Resource
    private RedisObjectService redisObjectService;

    @Resource
    private CatalogueItemSubtypeDao catalogueItemSubtypeDao;

    private static final String  ADMINISTRATIVE_DIVISION_CODE  = "61";
    //子项代码
    private static final String subItemCode = "00Y";

//    @Override
//    public ReturnCode eventDistribution(EventDistributionParam param) {
//
//
//        List<String> EventMaxs = new ArrayList<>();
//        // 判断原有事项是否有已完善部分
//        List<List<EventsTable>> copyList = new ArrayList<>();
//        for (List<EventsTable> str : param.getEventIdList()) {
//            List<EventsTable> copy = new ArrayList<>();
//            for (EventsTable ss : str) {
//
//                copy.add(ss);
//                if ("0".equals(ss.getEventPid())) {
//
//                    EventMaxs.add(ss.getId());
//                }
//            }
//            copyList.add(copy);
//        }
//
//        Condition condition = new Condition(DepartmentalMatters.class);
//        Example.Criteria criteria = condition.createCriteria();
//        criteria.andEqualTo("deptId", param.getDeptId());
//        criteria.andIn("eventId", EventMaxs);
//
//
//        //旧值
//
//        List<DepartmentalMatters> list = departmentalMattersDao.selectByCondition(condition);
//        if (list != null && list.size() > 0) {
//            for (DepartmentalMatters matter : list) {
//
//                List<EventsTable> list2 = new ArrayList<>();
//
//                //新值
//                for (List<EventsTable> list1 : param.getEventIdList()) {
//
//                    list2.addAll(list1);
//                }
//
//                if (!isExsit(list2, matter.getEventId())) {
//
//                    if (matter.getPresentQuantitativeState() == 2 || matter.getFormThatState() == 2 || matter.getBusinessSituationState() == 2
//                            || matter.getApplicationTypeState() == 2 || matter.getBusinessGuideState() == 2 || matter.getMaterialGroupingState() == 2
//                            || matter.getPrequalificationState() == 2 || matter.getProcessMapState() == 2 || matter.getProcessDescriptionState() == 2) {
//                        return new ReturnCode.Builder().failed().msg("事项已有部分完善，不可取消分配").build();
//                    } else {
//                        departmentalMattersDao.deleteByPrimaryKey(matter.getId());
//                    }
//
//                } else {
//
//                    for (List<EventsTable> s : param.getEventIdList()) {
//
//                        if (isExsit(s, matter.getEventId())) {
//                            s = remove(s, matter.getEventId());
//                            //  s.remove(matter.getEventId());
//
//                        }
//                    }
//
//                }
//            }
//        }
//
//
//        List<DepartmentalMatters> departmentalMattersList = new ArrayList<DepartmentalMatters>();
//
//        for (int i = 0; i < param.getEventIdList().size(); i++) {
//            List<EventsTable> list1 = param.getEventIdList().get(i);
//            List<EventsTable> newList = new ArrayList<>();
//            newList = copyList.get(i);
//
//            List<String> eventIds = new ArrayList<>();
//            if (newList != null && newList.size() > 0) {
//                for (EventsTable eventsTable :
//                        newList) {
//                    eventIds.add(eventsTable.getId());
//                }
//            }
//
//
//            String uuid = null;
//
//            if (list1 != null && list1.size() > 0 && eventIds != null && eventIds.size() > 0) {
//
//                uuid = departmentalMattersDao.selectGroupIdByEventIds(eventIds, param.getDeptId());
//            }
//            if (uuid == null) {
//
//                uuid = SysUtil.getUUID();
//            }
//
//            for (EventsTable eventId : list1) {
//                DepartmentalMatters item = new DepartmentalMatters();
//                item.setId(SysUtil.getUUID());
//                item.setDeptId(param.getDeptId());
//                item.setEventId(eventId.getId());
//                item.setGroupId(uuid);
//                item.setState(1);
//                departmentalMattersDao.insertSelective(item);
//            }
//        }
//        return new ReturnCode.Builder().success().msg("事项分配成功").build();
//    }

   /**
    * 基本目录编码结构：共12位
    * 2位行政区划代码、2位事项类型代码、5位主项、3位子项代码
    * 主项代码：
    *       前2位用中央业务指导（实施）部门代码标识，后3位在该部门中按事项类型顺序编制，
    *       顺序码范围为 001～999，用于标识具体政务服务事项；其中公共服务事项可将顺序码
    *       第1位作为业务分类代码使用，后2位作为分类后的业务排序码，排序码范围为01~99，
    *       无法满足业务分类代码和业务排序码按数字排序使用时，应按照大写英文字母顺序继续排序
    *       不使用( I、O、Z、S、V）
    * 子项代码：
    *       在同一政务服务事项按照其法定情形的不同或实施对象的不同被细分为若干个子项时使用。
    *       当政务服务事项无子项时，子项代码用 000 表示；有子项时，子项代码用 00Y 表示。
    *       对于含有子项的主项，其所有子项的编码范围为 001～999，用于标注同一事项主项所包括的不同子项。
    *       子项代码为顺序码，应按照同一主项中包括的子项顺序进行
    * @return
    */
    public String getBaseDirectoryEncoding(String deptId,String catalogueItemSubtype){

        //事项类型代码
        String eventTypeCode = catalogueItemSubtypeDao.selectByPrimaryKey(catalogueItemSubtype).getRemarks().trim();
        if(eventTypeCode.trim().length() != 2){
            throw new CustomException(new ReturnCode.Builder().failed().msg("\"事项类型代码\" 不规范").build());
        }
        Department department = departmentMapper.selectByPrimaryKey(deptId);
        //主项前两位（中央业务指导（实施）部门代码）
        CentralBusinessGuidanceDepartmentCode centralBusinessGuidanceDepartmentCode = centralBusinessGuidanceDepartmentCodeDao.selectByPrimaryKey(department.getCentralDepartments());
        if(centralBusinessGuidanceDepartmentCode == null ){
            throw new CustomException(new ReturnCode.Builder().failed().msg("中央业务指导（实施）部门 为空").build());
        }
        String frontDominantTermCode = centralBusinessGuidanceDepartmentCode.getCode().trim();
        //主项代码后三位代码
        String backDominantTermCode = "";
        //自增操作
        Long count = redisObjectService.LongIncrement(deptId + eventTypeCode,1l);
        //不区分公共服务的结果
        if(count > 999){
            count = 0l;
            redisObjectService.set(deptId + eventTypeCode,0);
        }
        backDominantTermCode = String.format("%3d",count).replaceAll(" ","0").trim();       //主项代码
        //主项代码
        String dominantTermCode = frontDominantTermCode + backDominantTermCode;
        if(dominantTermCode.trim().length() != 5){
            throw new CustomException(new ReturnCode.Builder().failed().msg("\"主项代码\" 格式错误！").build());
        }
        String encoding = ADMINISTRATIVE_DIVISION_CODE + eventTypeCode + dominantTermCode + subItemCode;
        if(encoding.trim().length() != 12){
            throw new CustomException(new ReturnCode.Builder().failed().msg("\"基本目录编码\" 格式错误！").build());
        }
        return encoding;
   }

    /**
     * 实施编码，共31位
     * 由18位统一社会信用代码、1位行使层级代码以及12位事项项基本编码
     * @return
     */
    public String getBusinessTransaction(String deptId,String baseDirectoryEncoding){

        Department department = departmentMapper.selectByPrimaryKey(deptId);
        //由18位统一社会信用代码
        String socialCreditCode = department.getSocialCreditCode().trim();
        if(socialCreditCode.length() != 18){
            throw new CustomException(new ReturnCode.Builder().failed().msg("\"部门征信编码\"格式错误！").build());
        }
        //行使层级代码
        String region = String.valueOf(Integer.parseInt(department.getRegion()) + 1).trim();
        if(region.length() != 1){
            throw new CustomException(new ReturnCode.Builder().failed().msg("\"层级编码\"格式错误！").build());
        }
        Long count = redisObjectService.LongIncrement(baseDirectoryEncoding.substring(0,9),1l);
        if(count > 999){
            count = 0l;
            redisObjectService.set(baseDirectoryEncoding.substring(0,9),0);
        }
        String counter = String.format("%3d",count).replaceAll(" ","0").trim();
        //事项项基本编码
        StringBuffer stringBuffer = new StringBuffer(baseDirectoryEncoding);
        stringBuffer.replace(9,12,counter);
        baseDirectoryEncoding = stringBuffer.toString().trim();

        if(baseDirectoryEncoding.length() != 12){
            throw new CustomException(new ReturnCode.Builder().failed().msg("\"事项项基本编码\"格式错误！").build());
        }
        String encoding = socialCreditCode + region + baseDirectoryEncoding;
        if(encoding.length() != 31){
            throw new CustomException(new ReturnCode.Builder().failed().msg("\"实施编码\"格式错误").build());
        }
        return encoding;
    }

    /**
     * 业务办理项编码:共33位
     * 31位事项实施编码、2位业务项编码
     * @param businessTransaction
     * @return
     */
    public String getCodingImplementation(String businessTransaction){
        //实施编码
        businessTransaction = businessTransaction.trim();
        if(businessTransaction.length() != 31){
            throw new CustomException(new ReturnCode.Builder().failed().msg("\"实施编码格式\"错误！").build());
        }
        //业务项编码
        Long count = redisObjectService.LongIncrement(businessTransaction,1L);
        if(count > 99){
            count = 0L;
            redisObjectService.set(businessTransaction,0);
        }
        String businessCode = String.format("%2d",count).replaceAll(" ","0").trim();
        if(businessCode.length() != 2){
            throw new CustomException(new ReturnCode.Builder().failed().msg("\"业务项编码\"格式错误！").build());
        }
        String encoding = businessTransaction + businessCode;
        if(encoding.length() != 33){
            throw new CustomException(new ReturnCode.Builder().failed().msg("\"业务办理项编码\"格式错误！").build());
        }
        return encoding;
    }

    @Override
    public Map<String,String> getEncoding(String deptId, String catalogueItemSubtypeId, Boolean isMin,String directoryEncoding){
        Map<String,String> map = new HashMap<>();
        //是目录直接返回目录编码
        if(!isMin){
            map.put("baseDirectoryEncoding",getBaseDirectoryEncoding(deptId,catalogueItemSubtypeId));
        }else{
            if(StringUtils.isEmpty(directoryEncoding)){
                throw new CustomException(new ReturnCode.Builder().failed().msg("目录编码为空").build());
            }
            String businessTransaction = getBusinessTransaction(deptId,directoryEncoding);
            map.put("codingImplementation",businessTransaction);
            map.put("directoryEncoding",getCodingImplementation(businessTransaction));
        }
        return map;
    }

//    @Override
//    public EventsTable updateEncoding(String deptId,String etId){
//        EventsTable eventsTable = eventsTableDao.selectByPrimaryKey(etId);
//        if(eventsTable == null){
//            throw new CustomException(new ReturnCode.Builder().failed().msg("该事项不存在").build());
//        }
//        Department department = departmentMapper.selectByPrimaryKey(deptId);
//        if(department == null){
//            throw new CustomException(new ReturnCode.Builder().failed().msg("该部门不存在").build());
//        }
//        //目录编码
//        String baseDirectoryEncoding = eventsTable.getBaseDirectoryEncoding();
//        if(baseDirectoryEncoding != null && baseDirectoryEncoding.length() == 12){
//            String centralBusinessGuidanceDepartmentId = department.getCentralDepartments();
//            String frontDominantTermCode = centralBusinessGuidanceDepartmentCodeDao.selectByPrimaryKey(centralBusinessGuidanceDepartmentId).getCode();
//            StringBuffer stringBuffer = new StringBuffer(baseDirectoryEncoding);
//            baseDirectoryEncoding = stringBuffer.replace(4,6,frontDominantTermCode).toString().trim();
//            eventsTable.setBaseDirectoryEncoding(baseDirectoryEncoding);
//        }else{
//            return eventsTable;
//        }
//        //实施编码
//        String codingImplementation = eventsTable.getCodingImplementation();
//        if(codingImplementation != null && codingImplementation.length() == 31){
//            String socialCreditCode = department.getSocialCreditCode().trim();
//            String region = String.valueOf(Integer.parseInt(department.getRegion()) + 1).trim();
//
//            StringBuffer stringBuffer = new StringBuffer(codingImplementation);
//            codingImplementation = stringBuffer.replace(0,19,socialCreditCode + region).toString().trim();
//
//            eventsTable.setCodingImplementation(codingImplementation);
//        }else{
//            return eventsTable;
//        }
//        //办理项编码
//        String directoryEncoding = eventsTable.getDirectoryEncoding();
//        if(directoryEncoding != null && directoryEncoding.length() == 33){
//            StringBuffer stringBuffer = new StringBuffer(directoryEncoding);
//            directoryEncoding = stringBuffer.replace(0,31,codingImplementation).toString();
//            eventsTable.setDirectoryEncoding(directoryEncoding);
//        }else{
//            return eventsTable;
//        }
//        return eventsTable;
//    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ReturnCode eventDistribution(EventDistributionParam param) {

        //找到新增和取消的
        String deptId = param.getDeptId();
        Condition condition = new Condition(DepartmentalMatters.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("deptId", deptId);

        List<DepartmentalMatters> hisEventlist = departmentalMattersDao.selectByCondition(condition);

        //查询历史数据
       Condition hisCcondition = new Condition(DepartmentalMatters.class);
       Example.Criteria hisCriteria = hisCcondition.createCriteria();
       hisCriteria.andEqualTo("deptId", deptId);



        List<List<EventsTable>> eventIdList = param.getEventIdList();

        // List<EventsTable> returnCancelFall= new ArrayList<>();
        // List<EventsTable> removeEventsTable = new ArrayList<>();


        List<String> strEventNeedDelete=new ArrayList<>();
        if (eventIdList != null && eventIdList.size() > 0) {

            for (List<EventsTable> eventsTable : eventIdList
            ) {

                //获取组id
                List<String> resultList = eventsTable.stream().map(EventsTable::getId).collect(Collectors.toList());

                String groupId = null;

                if (resultList != null && resultList.size() > 0) {

                    groupId = departmentalMattersDao.selectGroupIdByEventIds(resultList, deptId);
                }
                if (groupId == null) {

                    groupId = SysUtil.getUUID();
                }


                for (EventsTable event : eventsTable
                ) {
                    //取消
                    if (event.getChecked() != null && event.getHistoryCheck() != null && event.getHistoryCheck() == true && event.getChecked() == false) {

                        for (DepartmentalMatters departmentalMatters :
                                hisEventlist) {

                            if (event.getId().equals(departmentalMatters.getEventId())) {
                                if (departmentalMatters.getPresentQuantitativeState() == 2 || departmentalMatters.getFormThatState() == 2 || departmentalMatters.getBusinessSituationState() == 2
                                        || departmentalMatters.getApplicationTypeState() == 2 || departmentalMatters.getBusinessGuideState() == 2 || departmentalMatters.getMaterialGroupingState() == 2
                                        || departmentalMatters.getPrequalificationState() == 2 || departmentalMatters.getProcessMapState() == 2 || departmentalMatters.getProcessDescriptionState() == 2) {

                                  //  return new ReturnCode.Builder().failed().msg("事项已有部分完善，不可取消分配").build();
                                    throw  new CustomException(new ReturnCode.Builder().success().msg("事项已有部分完善，不可取消分配").build());

                                } else {
                                    strEventNeedDelete.add(departmentalMatters.getId());
                                }

                            }

                        }

                    }
                    //新增
                    if (event.getChecked() != null && event.getHistoryCheck() != null && event.getHistoryCheck() == false && event.getChecked() == true) {
                        hisCriteria.andEqualTo("eventId",event.getId());
                        List<DepartmentalMatters> hisEventByIdlist = departmentalMattersDao.selectByCondition(hisCcondition);
                        if(hisEventByIdlist!=null&&hisEventByIdlist.size()>0){
                            continue;
                        }
                        DepartmentalMatters item = new DepartmentalMatters();
                        item.setId(SysUtil.getUUID());
                        item.setDeptId(param.getDeptId());
                        item.setEventId(event.getId());
                        item.setGroupId(groupId);
                        item.setState(1);
                        departmentalMattersDao.insertSelective(item);

                    }
                }

            }

        }

        for (String needDelete:
        strEventNeedDelete) {
            departmentalMattersDao.deleteByPrimaryKey(needDelete);
        }


        return new ReturnCode.Builder().success().msg("事项分配成功").build();
    }


   /* public boolean isExsit(List<EventsTable> list, String eventId) {

        for (EventsTable eventsTable : list) {


            if (eventsTable.getChecked() != null && eventsTable.getHistoryCheck() != null && eventsTable.getHistoryCheck() == true && eventsTable.getChecked() == false) {

                return false;

            }

            if (eventsTable.getId().equals(eventId)) {

                return true;
            }
        }
        return false;
    }*/


   /* public List<EventsTable> remove(List<EventsTable> list, String eventId) {

        for (EventsTable eventsTable : list) {

            if (eventsTable.getId().equals(eventId)) {
                list.remove(eventsTable);
                return list;
            }
        }
        return list;

    }*/


    @Override
    public PageInfo selectByParam(SelectDepartmentalMattersListParam param) {
        PageHelper.startPage(param.getPage(), param.getSize());
        // 查询父级事项
        Page<EventsTable> result = eventsTableDao.selectByParam(param);


        List<EventsTable> allEvents = eventsTableDao.selectAll();

        // 查询二级事项关联
        List<EventsTable> secondLevelEvent = new ArrayList<>();
        for (EventsTable item : result) {
            List<EventsTable> eventIdIdList = new ArrayList<>();
            getBelongEvent(item.getId(), allEvents, eventIdIdList);
            secondLevelEvent.addAll(eventIdIdList);
        }
        result.addAll(secondLevelEvent);
        PageInfo pageInfo = new PageInfo(result);
        return pageInfo;
    }

    /**
     * 按部门id查询已分配的事项
     *
     * @param
     * @return
     */
 /*   @Override
    public List<EventsTable> selectDistributedEvents(String deptId) {
        List<EventsTable> events = eventsTableDao.selectDistributedEvents(deptId);
        return events;
    }*/

    public void getBelongEvent(String eventId, List<EventsTable> allEvents, List<EventsTable> eventIdIdList) {

        List<EventsTable> list = allEvents.stream().filter(a -> a.getEventPid().equals(eventId)).collect(Collectors.toList());
        if (list != null && list.size() > 0) {
            for (EventsTable item : list) {
                eventIdIdList.add(item);
            }
            for (EventsTable item : list) {
                getBelongEvent(item.getId(), allEvents, eventIdIdList);
            }
        }
    }

}
