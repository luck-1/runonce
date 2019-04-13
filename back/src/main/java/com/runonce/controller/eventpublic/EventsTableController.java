package com.runonce.controller.eventpublic;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.runonce.controller.eventversionone.*;

import com.runonce.dao.base.CatalogueItemSubtypeDao;
import com.runonce.dao.eventpublic.EventsTableDao;
import com.runonce.dao.eventversionone.DepartmentalMattersDao;
import com.runonce.dao.system.mapper.DepartmentMapper;
import com.runonce.exception.CustomException;
import com.runonce.model.base.CatalogueItemSubtype;
import com.runonce.model.eventversionone.DepartmentalMatters;
import com.runonce.model.http.EncodingParam;
import com.runonce.model.http.EventDistributionParam;
import com.runonce.model.http.SelectDepartmentalMattersListParam;
import com.runonce.util.SysUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import com.github.pagehelper.PageInfo;
import com.runonce.ReturnCode;
import com.runonce.model.eventpublic.EventsTable;
import com.runonce.service.eventpublic.EventsTableService;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * Created by xuxueli on '2018-12-10 15:05:36'.
 */

@RestController
@RequestMapping("events_table")
@Api(description = "事项库管理")
public class EventsTableController {


    @Autowired
    private EventsTableService eventsTableService;



//    /**
//     * 办事指南(备案类)
//     */
//    @Autowired
//    private BusinessGuideFilingOneController businessGuideFilingOneController;
//
//    /**
//     * 办事指南(行政许可类)
//     */
//    @Autowired
//    private BusinessGuideAdministrativeLicensingOneController businessGuideAdministrativeLicensingOneController;
//
//    /**
//     * 办事指南(审核转报类)
//     */
//    @Autowired
//    private BusinessGuideAuditAndForwardingCategoryOneController businessGuideAuditAndForwardingCategoryOneController;
//
//    /**
//     * 办事指南(其他服务类)
//     */
//    @Autowired
//    private BusinessGuideOtherServicesOneController businessGuideOtherServicesOneController;
//
//    /**
//     * 材料分组
//     */
//    @Autowired
//    private MaterialGroupingController materialGroupingController;
//
//    /**
//     * 申请类型
//     */
//    @Autowired
//    private ApplicationTypeFieldController applicationTypeFieldController;
//    /**
//     * 资格预审
//     */
//    @Autowired
//    private PrequalificationController prequalificationController;
//
//    /**
//     * 现有流程说明
//     */
//    @Autowired
//    private ProcessDescriptionController processDescriptionController;

    @Resource
    private DepartmentalMattersDao departmentalMattersDao;
    @Resource
    private EventsTableDao eventsTableDao;
    @Resource
    private CatalogueItemSubtypeDao catalogueItemSubtypeDao;
    @Resource
    private DepartmentMapper departmentMapper;

    @GetMapping("generate")
    @ApiOperation( value = "一键生成目录编码 或 办理项编码")
    @Transactional(rollbackFor = Exception.class)
    public ReturnCode generateEncoding(@RequestParam Boolean isDirectory){

        List<String> errData = new ArrayList<>();
        List<DepartmentalMatters> departmentalMattersList = departmentalMattersDao.selectAll();

        for (DepartmentalMatters departmentalMatters : departmentalMattersList) {
            //部门ID
            String deptId = departmentalMatters.getDeptId();
            EventsTable eventsTable = eventsTableDao.selectByPrimaryKey(departmentalMatters.getEventId());
            //部门不存在的dm表记录

            if(departmentMapper.selectByPrimaryKey(deptId) == null){
                String str = "groupId："+departmentalMatters.getGroupId();
                if( ! errData.contains(str)){
                    errData.add(str);
                }
                continue;
            }

            //是否办理项
            Boolean isMin = eventsTable.getIsMin();
            if(isMin == isDirectory){
                continue;
            }
            //事项小类型ID
            String smallItemTypeId = "";

            //事项小类型存的名称，先得到大类型，再和名字对比得到小类型ID
            String smallItemTypeName = eventsTable.getItemSmallType().trim();
            Integer bigTypeId = eventsTable.getEventType();
            List<CatalogueItemSubtype> catalogueItemSubtypeList = catalogueItemSubtypeDao.findByPid(bigTypeId.toString());
            for (CatalogueItemSubtype catalogueItemSubtype:catalogueItemSubtypeList) {
                if (smallItemTypeName.equals(catalogueItemSubtype.getName().trim())) {
                    smallItemTypeId = catalogueItemSubtype.getId();
                }
            }
            //基本目录编码，生成办理项编码使用
            String baseDirectoryEncoding = "";
            //不是目录
            if(! isDirectory){
                EventsTable eventsTableParent = eventsTableDao.selectByPrimaryKey(eventsTable.getEventPid());
                if(eventsTableParent == null ){
                    String str = "dir_null：—> etPid：" + eventsTable.getEventPid();
                    if( ! errData.contains(str)){
                        errData.add(str);
                    }
                    continue;
                }
                baseDirectoryEncoding = eventsTableParent.getBaseDirectoryEncoding();
            }
            Map<String,String> map = eventsTableService.getEncoding(deptId,smallItemTypeId,isMin,baseDirectoryEncoding);

            if(isDirectory){
                if(map.containsKey("baseDirectoryEncoding")){
                    baseDirectoryEncoding = map.get("baseDirectoryEncoding");
                    eventsTable.setBaseDirectoryEncoding(baseDirectoryEncoding);
                }else{
                    System.out.println("错误：dmId："+departmentalMatters.getId() + "  ->  etId："+ eventsTable.getId());
                    throw new CustomException(new ReturnCode.Builder().failed().msg("未生成目录编码").build());
                }
            }else{
                eventsTable.setBaseDirectoryEncoding(baseDirectoryEncoding);
                if(map.containsKey("directoryEncoding") && map.containsKey("codingImplementation")){
                    eventsTable.setDirectoryEncoding(map.get("directoryEncoding"));
                    eventsTable.setCodingImplementation(map.get("codingImplementation"));
                }else{
                    System.out.println("错误：dmId："+departmentalMatters.getId() + "  ->  etId："+ eventsTable.getId());
                    throw new CustomException(new ReturnCode.Builder().failed().msg("未生成实施编码").build());
                }
            }

            eventsTableDao.updateByPrimaryKey(eventsTable);
            System.out.println("dmId："+departmentalMatters.getId() + "  ->  etId："+ eventsTable.getId());
        }
        System.out.println("编码生成成功");
        errData.forEach(err -> System.out.println(err));
        System.out.println("错误数据输出成功");
        return new ReturnCode.Builder().success().msg("编码生成成功").build();
    }

    @PostMapping("getEncoding")
    @ApiOperation(value = "生成编码")
    public ReturnCode getEncoding(@RequestBody EncodingParam encodingParam) {
        String deptId = encodingParam.getDeptId();
        if (StringUtils.isEmpty(deptId)) {
            return new ReturnCode.Builder().failed().msg("部门ID为空").build();
        }
        String catalogueItemSubtypeId = encodingParam.getCatalogueItemSubtype();
        if (StringUtils.isEmpty(catalogueItemSubtypeId)) {
            return new ReturnCode.Builder().failed().msg("事项小类型为空").build();
        }
        Boolean isMin = encodingParam.getIsMin();
        if (isMin == null) {
            return new ReturnCode.Builder().failed().msg("是否办理项为空").build();
        }
        String directoryEncoding = encodingParam.getDirectoryEncoding();
        Map<String,String> map = eventsTableService.getEncoding(deptId, catalogueItemSubtypeId, isMin, directoryEncoding);
        return new ReturnCode.Builder().msg("编码生成成功").success().object(map).build();
    }

    @PostMapping("add")
    public ReturnCode add(@RequestBody EventsTable eventsTable) {
        eventsTable.setId(SysUtil.getUUID());
        eventsTableService.save(eventsTable);
        return new ReturnCode.Builder().msg("添加成功").success().object(eventsTable).build();
    }

    @DeleteMapping("delete")
    public ReturnCode delete(@RequestParam String id) {
        eventsTableService.deleteById(id);
        return new ReturnCode.Builder().msg("删除成功").success().build();
    }

    @PutMapping("update")
    public ReturnCode update(@RequestBody EventsTable eventsTable) {
        eventsTableService.update(eventsTable);
        return new ReturnCode.Builder().msg("更新成功").object(eventsTable).success().build();
    }

    @GetMapping("detail")
    public ReturnCode detail(@RequestParam String id) {
        EventsTable eventsTable = eventsTableService.findById(id);
        return new ReturnCode.Builder().msg("查询成功").object(eventsTable).success().build();
    }

    @PostMapping("list")
    public ReturnCode list(@RequestBody SelectDepartmentalMattersListParam param) {
        PageInfo pageInfo = eventsTableService.selectByParam(param);
        return new ReturnCode.Builder().msg("查询成功").object(pageInfo).success().build();
    }

    /**
     * 事项分配接口
     *
     * @param param
     * @return
     */
    @PostMapping("distribution")
    public ReturnCode eventDistribution(@RequestBody @Valid EventDistributionParam param) {
        ReturnCode returnCode;
        returnCode = eventsTableService.eventDistribution(param);
        return returnCode;
    }
    /**
     * 按部门id查询已分配的事项
     * @param deptId
     * @return
     */
   /* @GetMapping("selectbydeptid/{deptid}")
    public ReturnCode selectDistributedEvents(@PathVariable("deptid") String deptId){
        //验证是否有错，如果有错就输出错误信息
        if(StringUtils.isEmpty(deptId)){
            return new ReturnCode.Builder().failed().msg("参数为空").build();
        }
        List<EventsTable> events = null;
        try {
            events =  eventsTableService.selectDistributedEvents(deptId);
        }catch (Exception e){
            return new ReturnCode.Builder().failed().msg("事项分配查询异常").object(events).build();
        }
        return new ReturnCode.Builder().msg("事项分配查询成功").object(events).success().build();
    }*/
//    /**
//     * 事项填写内容暂存
//     * @param eventInfoParam
//     * @return
//     */
//    @PostMapping("save")
//    public ReturnCode save(@RequestBody EventInfoParam eventInfoParam){
//        // 办事指南save
//
//
//        if(eventInfoParam.getBusinessGuideFilingOne() != null){
//            businessGuideFilingOneController.save(eventInfoParam.getBusinessGuideFilingOne());
//        }
//        if(eventInfoParam.getBusinessGuideAdministrativeLicensingOne() != null){
//            businessGuideAdministrativeLicensingOneController.save(eventInfoParam.getBusinessGuideAdministrativeLicensingOne());
//        }
//        if(eventInfoParam.getBusinessGuideAuditAndForwardingCategoryOne() != null){
//            businessGuideAuditAndForwardingCategoryOneController.save(eventInfoParam.getBusinessGuideAuditAndForwardingCategoryOne());
//        }
//        if(eventInfoParam.getBusinessGuideOtherServicesOne() != null){
//            businessGuideOtherServicesOneController.save(eventInfoParam.getBusinessGuideOtherServicesOne());
//        }
//        // 材料分组save
//        if(eventInfoParam.getMaterialGroupAddParamList() != null){
////            materialGroupingController.save(eventInfoParam.getMaterialGroupAddParamList());
//        }
//        // 申请类型save
//        if(eventInfoParam.getRelatedMaterialsParamList() != null){
//            applicationTypeFieldController.add(eventInfoParam.getRelatedMaterialsParamList());
//        }
//        // 办事情形(申请类型)思维导图对象
//
//        // 资格预审
//        if(eventInfoParam.getPrequalificationList() != null){
//            prequalificationController.adds(eventInfoParam.getPrequalificationList());
//        }
//        // 现有流程说明
//        if(eventInfoParam.getProcessDescriptionList() != null){
//            processDescriptionController.adds(eventInfoParam.getProcessDescriptionList());
//        }
//        // 流程图对象
//
//        return new ReturnCode.Builder().msg("保存成功").object(eventInfoParam).success().build();
//    }

//    /**
//     * 事项填写内容提交
//     * @return
//     */
//    @PostMapping("submit")
//    public ReturnCode submit(@RequestBody EventInfoParam eventInfoParam){
//        // 办事指南
//        if(eventInfoParam.getBusinessGuideFilingOne() != null){
//            businessGuideFilingOneController.save(eventInfoParam.getBusinessGuideFilingOne());
//        }
//        if(eventInfoParam.getBusinessGuideAdministrativeLicensingOne() != null){
//            businessGuideAdministrativeLicensingOneController.save(eventInfoParam.getBusinessGuideAdministrativeLicensingOne());
//        }
//        if(eventInfoParam.getBusinessGuideAuditAndForwardingCategoryOne() != null){
//            businessGuideAuditAndForwardingCategoryOneController.save(eventInfoParam.getBusinessGuideAuditAndForwardingCategoryOne());
//        }
//        if(eventInfoParam.getBusinessGuideOtherServicesOne() != null){
//            businessGuideOtherServicesOneController.save(eventInfoParam.getBusinessGuideOtherServicesOne());
//        }
//        // 材料分组save
//        if(eventInfoParam.getMaterialGroupAddParamList() != null){
////            materialGroupingController.add(eventInfoParam.getMaterialGroupAddParamList());
//        }
//        // 申请类型save
//        if(eventInfoParam.getRelatedMaterialsParamList() != null){
//            applicationTypeFieldController.add(eventInfoParam.getRelatedMaterialsParamList());
//        }
//        // 办事情形(申请类型)思维导图对象
//
//        // 资格预审
//        if(eventInfoParam.getPrequalificationList() != null){
//            prequalificationController.adds(eventInfoParam.getPrequalificationList());
//        }
//        // 现有流程说明
//        if(eventInfoParam.getProcessDescriptionList() != null){
//            processDescriptionController.adds(eventInfoParam.getProcessDescriptionList());
//        }
//        // 流程图对象
//
//        return new ReturnCode.Builder().msg("提交成功").object(null).success().build();
//    }
}