package com.runonce.service.impl.importdata;

import cn.hutool.core.bean.BeanUtil;
import com.aliyun.oss.OSSClient;
import com.runonce.ReturnCode;
import com.runonce.dao.base.ServiceObjectDao;
import com.runonce.dao.eventversionone.*;
import com.runonce.dao.system.DepartmentDao;
import com.runonce.enums.SheetNumEnum;
import com.runonce.exception.CustomException;
import com.runonce.httpbean.assets.reqbean.BusinessGuideAdministrativeLicensingOneReq;
import com.runonce.httpbean.assets.reqbean.BusinessGuideAuditAndForwardingCategoryOneReq;
import com.runonce.httpbean.assets.reqbean.BusinessGuideFilingOneReq;
import com.runonce.httpbean.assets.reqbean.BusinessGuideOtherServicesOneReq;
import com.runonce.httpbean.assets.resbean.DepartmentalMattersAndEventRes;
import com.runonce.httpbean.assets.resbean.ImportExcelRes;
import com.runonce.model.common.SystemBean;
import com.runonce.model.eventversionone.*;
import com.runonce.model.system.Department;
import com.runonce.service.eventversionone.MaterialGroupingImportService;
import com.runonce.service.importdata.DataImportService;
import com.runonce.util.ExcelUtil;
import com.runonce.util.ImportUtil;
import com.runonce.util.OssClientUtil;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import org.apache.poi.ss.extractor.EmbeddedData;
import org.apache.poi.ss.extractor.EmbeddedExtractor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.*;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author swq
 * @date 2019/2/14 0014
 */
@Service
public class DataImportServiceImpl implements DataImportService {
    @Resource
    private DepartmentalMattersDao departmentalMattersDao;
    @Resource
    private DepartmentDao departmentDao;
    @Resource
    private ServiceObjectDao serviceObjectDao;

    @Resource
    private VersionmanagementDao versionmanagementDao;
    @Resource
    private MaterialGroupingImportService materialGroupingImportService;

    @Resource
    private BusinessGuideAdministrativeLicensingOneDao businessGuideAdministrativeLicensingOneDao;
    @Resource
    private BusinessGuideAuditAndForwardingCategoryOneDao businessGuideAuditAndForwardingCategoryOneDao;
    @Resource
    private BusinessGuideFilingOneDao BusinessGuideFilingOneDao;
    @Resource
    private BusinessGuideOtherServicesOneDao businessGuideOtherServicesOneDao;


    /**
     * 导入excel
     */
    @Override
    public ImportExcelRes importExcel(MultipartFile file, Integer type, String version, String departmentalMattersId) throws Exception {
        //判断事项是否存在
        DepartmentalMattersAndEventRes departmentalMattersAndEventRes = departmentalMattersDao.selectDepartmentalMattersAndEvent(departmentalMattersId);

        if (departmentalMattersAndEventRes == null) {

            throw new CustomException(new ReturnCode.Builder().failed().msg("该事项不存在").build());
        }

        if (departmentalMattersAndEventRes.getDeptId() == null) {
            throw new CustomException(new ReturnCode.Builder().failed().msg("该事项的未配置部门").build());
        }


        if (departmentalMattersAndEventRes.getState() == 3) {

            throw new CustomException(new ReturnCode.Builder().failed().msg("该事项已提交无法导入").build());

        }

        //判断部门是否存在
        Department department = departmentDao.findOne(departmentalMattersAndEventRes.getDeptId());

        if (department == null) {
            throw new CustomException(new ReturnCode.Builder().failed().msg("未找到该事项部门信息").build());
        }
        if (department.getRegion() == null) {
            throw new CustomException(new ReturnCode.Builder().failed().msg("该事项单位未配置部门层级信息").build());
        }
        //返回给前端的对象
        ImportExcelRes<Object> importExcelRes = new ImportExcelRes<>();

        // 获取文件流
        InputStream inputStream = file.getInputStream();
        Workbook workbook = ExcelUtil.getWorkbook(inputStream, file);

        if (workbook == null) {
            throw new CustomException(new ReturnCode.Builder().failed().msg("读取Excel失败，请检查文件类型。").build());

        }
        OSSClient ossClient = OssClientUtil.getOssClient();
        //判断是否可以导出办事指南
        if (SystemBean.REGION_AREA.equals(department.getRegion())) {
            throw new CustomException(new ReturnCode.Builder().failed().msg("区县级无法进行导入数据。").build());
        }
        Sheet sheet;
        //办事指南;
        sheet = getSheet(workbook, SheetNumEnum.BUSINESS_GUIDE.getName());

        if (sheet == null) {
            throw new CustomException(new ReturnCode.Builder().failed().msg("未找到办事指南sheet页。").build());
        }
        //办事指南导入
        importBusinessGuide(workbook, sheet, departmentalMattersAndEventRes, importExcelRes);
        //办事指南图片导入
        importExcelObjectData(sheet, SheetNumEnum.BUSINESS_GUIDE.getId(), importExcelRes, ossClient);

        //材料分组
        sheet = getSheet(workbook, SheetNumEnum.MATERIAL_GROUPING.getName());
        //材料分组其他名字  办事材料
        if(sheet == null){
            sheet = getSheet(workbook, SheetNumEnum.MATERIALS_SERVICE.getName());
        }
        if (sheet == null) {
            throw new CustomException(new ReturnCode.Builder().failed().msg("未找到材料分组sheet页。").build());
        }
        //材料分组导入
        materialGroupingImportService.materialGroupingImport(workbook, sheet, version, departmentalMattersAndEventRes, importExcelRes);
        //材料分组图片导入
        importExcelObjectData(sheet, SheetNumEnum.MATERIAL_GROUPING.getId(), importExcelRes, ossClient);
        ossClient.shutdown();

        return importExcelRes;
    }

    private Sheet getSheet(Workbook workbook, String sheetName) {
//
//        workbook.getAllNames()
        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            SheetVisibility sheetVisibility = workbook.getSheetVisibility(i);
            if (sheetVisibility == SheetVisibility.VISIBLE) {
                Sheet sheet = workbook.getSheetAt(i);
                String s = sheet.getSheetName();
                if (s.contains(sheetName)) {
                    return sheet;
                }
            }
        }
        //读取index的sheet
        return null;
    }

    /**
     * 导入办事指南
     */
    private void importBusinessGuide(Workbook workbook,
                                     Sheet sheet,
                                     DepartmentalMattersAndEventRes departmentalMattersAndEventRes,
                                     ImportExcelRes<Object> importExcelRes) {

        Map<String, Object> map = new LinkedHashMap<>();


        //要素名称在sheet中的列index
        int keyIndex = 0;

        for (int rowNum = 0; rowNum <= sheet.getLastRowNum(); rowNum++) {
            Row row = sheet.getRow(rowNum);
            if(row == null){
                continue;
            }

            if (keyIndex == 0) {
                for (int cellNum = 0; cellNum < row.getLastCellNum(); cellNum++) {
                    Cell cell = row.getCell(cellNum);
                    if ("要素名称".equals(ImportUtil.getValue(cell))) {
                        keyIndex = cellNum;
                        break;
                    }
                }
            } else {
                //取得mapKey
                Cell cell = row.getCell(keyIndex);
                String stringKey = ImportUtil.getValue(cell);

                Cell cell1 = row.getCell(keyIndex + 1);
                String stringValue = ImportUtil.getValue(cell1);
                map.put(stringKey, stringValue);
            }
        }

        Integer version = versionmanagementDao.getSheetVersion(departmentalMattersAndEventRes.getId(), 1);
        if (version == null) {
            throw new CustomException(new ReturnCode.Builder().failed().msg("未知错误：未查到当前版本！").build());
        }
//        String reg = "[`~!@#$%^&*()\\-+={}':;,\\[\\].<>/?￥%…（）_+|【】‘；：”“’。，、？\\s+]";
        //转换对象前的值处理
        //事项大类型
        if (!map.containsKey("事项大类型")) {
            throw new CustomException(new ReturnCode.Builder().failed().msg("导出模板中事项大类型不存在。").build());
        }
        String majorTypesOfEvents = map.get("事项大类型").toString();
        //判断事项大类型是否和当前事项相同
        if (departmentalMattersAndEventRes.getEventTypeName() == null) {
            throw new CustomException(new ReturnCode.Builder().failed().msg("导入的事项，事项大类型不存在。").build());
        }
        if (majorTypesOfEvents.contains(departmentalMattersAndEventRes.getEventTypeName())) {
            majorTypesOfEvents = departmentalMattersAndEventRes.getEventType();
        } else {
            throw new CustomException(new ReturnCode.Builder().failed().msg("导入的事项和导入模板事项类型不统一。").build());
        }

        Map<String, Object> newMap;
        switch (departmentalMattersAndEventRes.getEventType()) {
            //1.行政许可
            case "1":
                BusinessGuideAdministrativeLicensingOne businessGuideAdministrativeLicensingOne = new BusinessGuideAdministrativeLicensingOne();
                BusinessGuideAdministrativeLicensingOnePrivate businessGuideAdministrativeLicensingOnePrivate = new BusinessGuideAdministrativeLicensingOnePrivate();
                BusinessGuideAdministrativeLicensingOneReq businessGuideAdministrativeLicensingOneReq = new BusinessGuideAdministrativeLicensingOneReq();
                BusinessGuideAdministrativeLicensingOneReq selectBusinessGuideAdministrativeLicensingOneReq = businessGuideAdministrativeLicensingOneDao.findByEventId(departmentalMattersAndEventRes.getId());
                //判断事项存在
                if (selectBusinessGuideAdministrativeLicensingOneReq == null) {
                    throw new CustomException(new ReturnCode.Builder().failed().msg("该事项不存在办事指南。").build());
                }
                //判断事项存在
                if (selectBusinessGuideAdministrativeLicensingOneReq.getBusinessGuideAdministrativeLicensingOne() == null) {
                    throw new CustomException(new ReturnCode.Builder().failed().msg("该事项不存在办事指南数据。").build());
                }

                //反射取得字段的ColumnName
                newMap = ImportUtil.getNameEverseColumn(BusinessGuideAdministrativeLicensingOne.class, map);
                //反射取得私有字段的ColumnName
                newMap.putAll(ImportUtil.getNameEverseColumn(BusinessGuideAdministrativeLicensingOnePrivate.class, map));
                //取得公有数据对象
                BeanUtil.fillBeanWithMap(newMap, businessGuideAdministrativeLicensingOne, true);
                //设置图片为默认值
                businessGuideAdministrativeLicensingOne.setResultSample("");
                businessGuideAdministrativeLicensingOne.setAddendumToProcessingTimeOrPlace("");
                //放入事项id
                businessGuideAdministrativeLicensingOne.setEventId(departmentalMattersAndEventRes.getId());
                //放入目录名称
                businessGuideAdministrativeLicensingOne.setTransactionDirectory(departmentalMattersAndEventRes.getDirectoryName());
                //事项名称
                businessGuideAdministrativeLicensingOne.setItemName(departmentalMattersAndEventRes.getEventName());
                //放入事项大类型
                businessGuideAdministrativeLicensingOne.setMajorTypesOfEvents(majorTypesOfEvents);
                //放入事项小类型
                businessGuideAdministrativeLicensingOne.setItemSmallType(departmentalMattersAndEventRes.getItemSmallType());
                //放入服务对象
                businessGuideAdministrativeLicensingOne.setServiceObject(departmentalMattersAndEventRes.getServiceObject());
                //放入基本编码
                businessGuideAdministrativeLicensingOne.setBasicCoding(departmentalMattersAndEventRes.getDirectoryEncoding());
                //放入行使层级
                businessGuideAdministrativeLicensingOne.setExerciseHierarchy(departmentalMattersAndEventRes.getExerciseHierarchy());
                //放入实施(服务)编码
                businessGuideAdministrativeLicensingOne.setCodingImplementation(departmentalMattersAndEventRes.getCodingImplementation());
                //放入页面禁止使用的字段
                //投资项目申报地址 declarationAddressOfEnterpriseInvestmentProjects
                businessGuideAdministrativeLicensingOne.setDeclarationAddressOfEnterpriseInvestmentProjects(selectBusinessGuideAdministrativeLicensingOneReq.getBusinessGuideAdministrativeLicensingOne().getDeclarationAddressOfEnterpriseInvestmentProjects());
                //办理时间或地点类型 typeOfProcessingTimeOrPlace
                businessGuideAdministrativeLicensingOne.setTypeOfProcessingTimeOrPlace(selectBusinessGuideAdministrativeLicensingOneReq.getBusinessGuideAdministrativeLicensingOne().getTypeOfProcessingTimeOrPlace());
                //受理通过 acceptAndApprove
                businessGuideAdministrativeLicensingOne.setAcceptAndApprove(selectBusinessGuideAdministrativeLicensingOneReq.getBusinessGuideAdministrativeLicensingOne().getAcceptAndApprove());
                //时限(受理) timeLimitForAcceptance
                businessGuideAdministrativeLicensingOne.setTimeLimitForAcceptance(selectBusinessGuideAdministrativeLicensingOneReq.getBusinessGuideAdministrativeLicensingOne().getTimeLimitForAcceptance());
                //补正或更正 correctionOrCorrection
                businessGuideAdministrativeLicensingOne.setCorrectionOrCorrection(selectBusinessGuideAdministrativeLicensingOneReq.getBusinessGuideAdministrativeLicensingOne().getCorrectionOrCorrection());
                //时限(补正或更正)timeLimitForCorrection
                businessGuideAdministrativeLicensingOne.setTimeLimitForCorrection(selectBusinessGuideAdministrativeLicensingOneReq.getBusinessGuideAdministrativeLicensingOne().getTimeLimitForCorrection());
                //审查方式 modeOfExamination
                businessGuideAdministrativeLicensingOne.setModeOfExamination(selectBusinessGuideAdministrativeLicensingOneReq.getBusinessGuideAdministrativeLicensingOne().getModeOfExamination());
                //审查标准 reviewStandard
                businessGuideAdministrativeLicensingOne.setReviewStandard(selectBusinessGuideAdministrativeLicensingOneReq.getBusinessGuideAdministrativeLicensingOne().getReviewStandard());
                //审查结果 examinationResult
                businessGuideAdministrativeLicensingOne.setExaminationResult(selectBusinessGuideAdministrativeLicensingOneReq.getBusinessGuideAdministrativeLicensingOne().getExaminationResult());
                //审查时限 timeLimitRorReview
                businessGuideAdministrativeLicensingOne.setTimeLimitRorReview(selectBusinessGuideAdministrativeLicensingOneReq.getBusinessGuideAdministrativeLicensingOne().getTimeLimitRorReview());
                //决定时限 decisionTimeLimit
                businessGuideAdministrativeLicensingOne.setDecisionTimeLimit(selectBusinessGuideAdministrativeLicensingOneReq.getBusinessGuideAdministrativeLicensingOne().getDecisionTimeLimit());
                //法律依据及描述(特殊) legalBasisAndDescriptionSpecial
                businessGuideAdministrativeLicensingOne.setLegalBasisAndDescriptionSpecial(selectBusinessGuideAdministrativeLicensingOneReq.getBusinessGuideAdministrativeLicensingOne().getLegalBasisAndDescriptionSpecial());
                //所需时限(特殊) requiredTimeLimitSpecial
                businessGuideAdministrativeLicensingOne.setRequiredTimeLimitSpecial(selectBusinessGuideAdministrativeLicensingOneReq.getBusinessGuideAdministrativeLicensingOne().getRequiredTimeLimitSpecial());
                //计算法定办结时限内 statutoryCompletionTimeLimitSpecial
                businessGuideAdministrativeLicensingOne.setStatutoryCompletionTimeLimitSpecial(selectBusinessGuideAdministrativeLicensingOneReq.getBusinessGuideAdministrativeLicensingOne().getStatutoryCompletionTimeLimitSpecial());
                //办理流程图 processFlowChart
                businessGuideAdministrativeLicensingOne.setProcessFlowChart(selectBusinessGuideAdministrativeLicensingOneReq.getBusinessGuideAdministrativeLicensingOne().getProcessFlowChart());
                //特殊处理字段
                //是否自贸区事项 ftaMatters
                businessGuideAdministrativeLicensingOne.setFtaMatters(checkTrueAndFalse(businessGuideAdministrativeLicensingOne.getFtaMatters()));
//                是否收费 charge
                businessGuideAdministrativeLicensingOne.setCharge(checkTrueAndFalse(businessGuideAdministrativeLicensingOne.getCharge()));
//                是否有网上服务系统 onlineServiceSystem
                businessGuideAdministrativeLicensingOne.setOnlineServiceSystem(checkTrueAndFalse(businessGuideAdministrativeLicensingOne.getOnlineServiceSystem()));
//                是否有中介服务 intermediaryService
                businessGuideAdministrativeLicensingOne.setIntermediaryService(checkTrueAndFalse(businessGuideAdministrativeLicensingOne.getIntermediaryService()));
//                 是否收费（特殊）chargeSpecial
                businessGuideAdministrativeLicensingOne.setChargeSpecial(checkTrueAndFalse(businessGuideAdministrativeLicensingOne.getChargeSpecial()));

                //取得私有数据
                BeanUtil.fillBeanWithMap(newMap, businessGuideAdministrativeLicensingOnePrivate, true);
                //放入事项id
                businessGuideAdministrativeLicensingOnePrivate.setDepartmentalMattersId(departmentalMattersAndEventRes.getId());
                //特殊处理字段
//               是否有网上服务系统 onlineServiceSystem
                businessGuideAdministrativeLicensingOnePrivate.setOnlineServiceSystem(checkTrueAndFalse(businessGuideAdministrativeLicensingOnePrivate.getOnlineServiceSystem()));
                //放入版本
                businessGuideAdministrativeLicensingOnePrivate.setVersion(version);
                //封装传给前端的数据
                businessGuideAdministrativeLicensingOneReq.setBusinessGuideAdministrativeLicensingOne(businessGuideAdministrativeLicensingOne);
                businessGuideAdministrativeLicensingOneReq.setBusinessGuideAdministrativeLicensingOnePrivate(businessGuideAdministrativeLicensingOnePrivate);
                //将给前端的数据放入泛型类
                importExcelResSet(businessGuideAdministrativeLicensingOneReq, importExcelRes);

                break;
            //2.审核转报
            case "2":
                BusinessGuideAuditAndForwardingCategoryOne businessGuideAuditAndForwardingCategoryOne = new BusinessGuideAuditAndForwardingCategoryOne();
                BusinessGuideAuditAndForwardingCategoryOnePrivate businessGuideAuditAndForwardingCategoryOnePrivate = new BusinessGuideAuditAndForwardingCategoryOnePrivate();
                BusinessGuideAuditAndForwardingCategoryOneReq businessGuideAuditAndForwardingCategoryOneReq = new BusinessGuideAuditAndForwardingCategoryOneReq();

                BusinessGuideAuditAndForwardingCategoryOneReq selectBusinessGuideAuditAndForwardingCategoryOneReq = businessGuideAuditAndForwardingCategoryOneDao.findByEventId(departmentalMattersAndEventRes.getId());
                //判断事项存在
                if (selectBusinessGuideAuditAndForwardingCategoryOneReq == null) {
                    throw new CustomException(new ReturnCode.Builder().failed().msg("该事项不存在办事指南。").build());
                }
                //判断事项存在
                if (selectBusinessGuideAuditAndForwardingCategoryOneReq.getBusinessGuideAuditAndForwardingCategoryOne() == null) {
                    throw new CustomException(new ReturnCode.Builder().failed().msg("该事项不存在办事指南数据。").build());
                }
                //反射取得字段的ColumnName
                newMap = ImportUtil.getNameEverseColumn(BusinessGuideAuditAndForwardingCategoryOne.class, map);
                //反射取得私有字段的ColumnName
                newMap.putAll(ImportUtil.getNameEverseColumn(BusinessGuideAuditAndForwardingCategoryOnePrivate.class, map));
                //取得公有数据对象
                BeanUtil.fillBeanWithMap(newMap, businessGuideAuditAndForwardingCategoryOne, true);
                //addendumToProcessingTimeOrPlace
                businessGuideAuditAndForwardingCategoryOne.setAddendumToProcessingTimeOrPlace("");
                //放入事项id
                businessGuideAuditAndForwardingCategoryOne.setEventId(departmentalMattersAndEventRes.getId());
                //放入目录名称
                businessGuideAuditAndForwardingCategoryOne.setItemCatalogue(departmentalMattersAndEventRes.getDirectoryName());
                //事项名称
                businessGuideAuditAndForwardingCategoryOne.setItemName(departmentalMattersAndEventRes.getEventName());
                //放入事项大类型
                businessGuideAuditAndForwardingCategoryOne.setMajorTypesOfEvents(majorTypesOfEvents);
                //放入事项小类型
                businessGuideAuditAndForwardingCategoryOne.setItemSmallType(departmentalMattersAndEventRes.getItemSmallType());
                //放入服务对象
                businessGuideAuditAndForwardingCategoryOne.setServiceObject(departmentalMattersAndEventRes.getServiceObject());
                //放入基本编码
                businessGuideAuditAndForwardingCategoryOne.setBasicCoding(departmentalMattersAndEventRes.getDirectoryEncoding());
                //放入行使层级
                businessGuideAuditAndForwardingCategoryOne.setExerciseHierarchy(departmentalMattersAndEventRes.getExerciseHierarchy());
                //放入实施(服务)编码
                businessGuideAuditAndForwardingCategoryOne.setCodingImplementation(departmentalMattersAndEventRes.getCodingImplementation());
                //放入页面禁止使用的字段
                //办理时间或地点类型typeOfProcessingTimeOrPlace
                businessGuideAuditAndForwardingCategoryOne.setTypeOfProcessingTimeOrPlace(selectBusinessGuideAuditAndForwardingCategoryOneReq.getBusinessGuideAuditAndForwardingCategoryOne().getTypeOfProcessingTimeOrPlace());
                //特殊处理字段
                //是否收费           charge
                businessGuideAuditAndForwardingCategoryOne.setCharge(checkTrueAndFalse(businessGuideAuditAndForwardingCategoryOne.getCharge()));
                //是否有网上服务系统     onlineServiceSystem
                businessGuideAuditAndForwardingCategoryOne.setOnlineServiceSystem(checkTrueAndFalse(businessGuideAuditAndForwardingCategoryOne.getOnlineServiceSystem()));

                //取得私有数据
                BeanUtil.fillBeanWithMap(newMap, businessGuideAuditAndForwardingCategoryOnePrivate, true);
                //放入事项id
                businessGuideAuditAndForwardingCategoryOnePrivate.setDepartmentalMattersId(departmentalMattersAndEventRes.getId());
                //addendumToProcessingTimeOrPlace
                businessGuideAuditAndForwardingCategoryOnePrivate.setAddendumToProcessingTimeOrPlace("");
                //特殊处理字段
//                是否有网上服务系统     onlineServiceSystem
                businessGuideAuditAndForwardingCategoryOnePrivate.setOnlineServiceSystem(checkTrueAndFalse(businessGuideAuditAndForwardingCategoryOnePrivate.getOnlineServiceSystem()));
                businessGuideAuditAndForwardingCategoryOnePrivate.setAddendumToProcessingTimeOrPlace("");
                //放入版本
                businessGuideAuditAndForwardingCategoryOnePrivate.setVersion(version);
                //封装传给前端的数据
                businessGuideAuditAndForwardingCategoryOneReq.setBusinessGuideAuditAndForwardingCategoryOne(businessGuideAuditAndForwardingCategoryOne);
                businessGuideAuditAndForwardingCategoryOneReq.setBusinessGuideAuditAndForwardingCategoryOnePrivate(businessGuideAuditAndForwardingCategoryOnePrivate);
                //将给前端的数据放入泛型类
                importExcelResSet(businessGuideAuditAndForwardingCategoryOneReq, importExcelRes);
                break;
            //3.备案类
            case "3":
                BusinessGuideFilingOne businessGuideFilingOne = new BusinessGuideFilingOne();
                BusinessGuideFilingOnePrivate businessGuideFilingOnePrivate = new BusinessGuideFilingOnePrivate();
                BusinessGuideFilingOneReq businessGuideFilingOneReq = new BusinessGuideFilingOneReq();
                BusinessGuideFilingOneReq selectBusinessGuideFilingOneReq = BusinessGuideFilingOneDao.findByEventId(departmentalMattersAndEventRes.getId());
                //判断事项存在
                if (selectBusinessGuideFilingOneReq == null) {
                    throw new CustomException(new ReturnCode.Builder().failed().msg("该事项不存在办事指南。").build());
                }
                //判断事项存在
                if (selectBusinessGuideFilingOneReq.getBusinessGuideFilingOne() == null) {
                    throw new CustomException(new ReturnCode.Builder().failed().msg("该事项不存在办事指南数据。").build());
                }

                //反射取得字段的ColumnName
                newMap = ImportUtil.getNameEverseColumn(BusinessGuideFilingOne.class, map);
                //反射取得私有字段的ColumnName
                newMap.putAll(ImportUtil.getNameEverseColumn(BusinessGuideFilingOnePrivate.class, map));
                //取得公有数据对象
                BeanUtil.fillBeanWithMap(newMap, businessGuideFilingOne, true);
                //设置图片为默认值
                businessGuideFilingOne.setResultSample("");
                businessGuideFilingOne.setAddendumToProcessingTimeOrPlace("");
                //放入事项id
                businessGuideFilingOne.setEventId(departmentalMattersAndEventRes.getId());
                //放入目录名称
                businessGuideFilingOne.setTransactionDirectory(departmentalMattersAndEventRes.getDirectoryName());
                //事项名称
                businessGuideFilingOne.setItemName(departmentalMattersAndEventRes.getEventName());
                //放入事项大类型
                businessGuideFilingOne.setMajorTypesOfEvents(majorTypesOfEvents);
                //放入事项小类型
                businessGuideFilingOne.setItemSmallType(departmentalMattersAndEventRes.getItemSmallType());
                //放入服务对象
                businessGuideFilingOne.setServiceObject(departmentalMattersAndEventRes.getServiceObject());
                //放入基本编码
                businessGuideFilingOne.setBasicCoding(departmentalMattersAndEventRes.getDirectoryEncoding());
                //放入行使层级
                businessGuideFilingOne.setExerciseHierarchy(departmentalMattersAndEventRes.getExerciseHierarchy());
                //放入实施(服务)编码
                businessGuideFilingOne.setCodingImplementation(departmentalMattersAndEventRes.getCodingImplementation());
                //放入页面禁止使用的字段
                //企业投资项目申报地址 declarationAddressOfEnterpriseInvestmentProjects
                businessGuideFilingOne.setDeclarationAddressOfEnterpriseInvestmentProjects(selectBusinessGuideFilingOneReq.getBusinessGuideFilingOne().getDeclarationAddressOfEnterpriseInvestmentProjects());
                //办理时间或地点类型 typeOfProcessingTimeOrPlace
                businessGuideFilingOne.setTypeOfProcessingTimeOrPlace(selectBusinessGuideFilingOneReq.getBusinessGuideFilingOne().getTypeOfProcessingTimeOrPlace());
                //受理通过 acceptAndApprove
                businessGuideFilingOne.setAcceptAndApprove(selectBusinessGuideFilingOneReq.getBusinessGuideFilingOne().getAcceptAndApprove());
                //时限(受理) timeLimitForAcceptance
                businessGuideFilingOne.setTimeLimitForAcceptance(selectBusinessGuideFilingOneReq.getBusinessGuideFilingOne().getTimeLimitForAcceptance());
                //补正或更正 correctionOrCorrection
                businessGuideFilingOne.setCorrectionOrCorrection(selectBusinessGuideFilingOneReq.getBusinessGuideFilingOne().getCorrectionOrCorrection());
                //时限(补正或更正) timeLimitForCorrection
                businessGuideFilingOne.setTimeLimitForCorrection(selectBusinessGuideFilingOneReq.getBusinessGuideFilingOne().getTimeLimitForCorrection());
                //审查方式 modeOfExamination
                businessGuideFilingOne.setModeOfExamination(selectBusinessGuideFilingOneReq.getBusinessGuideFilingOne().getModeOfExamination());
                //审查标准 reviewStandard
                businessGuideFilingOne.setReviewStandard(selectBusinessGuideFilingOneReq.getBusinessGuideFilingOne().getReviewStandard());
                //审查结果 examinationResult
                businessGuideFilingOne.setExaminationResult(selectBusinessGuideFilingOneReq.getBusinessGuideFilingOne().getExaminationResult());
                //审查时限 timeLimitRorReview
                businessGuideFilingOne.setTimeLimitRorReview(selectBusinessGuideFilingOneReq.getBusinessGuideFilingOne().getTimeLimitRorReview());
                //决定时限 decisionTimeLimit
                businessGuideFilingOne.setDecisionTimeLimit(selectBusinessGuideFilingOneReq.getBusinessGuideFilingOne().getDecisionTimeLimit());
                //法律依据及描述(特殊) legalBasisAndDescriptionSpecial
                businessGuideFilingOne.setLegalBasisAndDescriptionSpecial(selectBusinessGuideFilingOneReq.getBusinessGuideFilingOne().getLegalBasisAndDescriptionSpecial());
                //所需时限(特殊) requiredTimeLimitSpecial
                businessGuideFilingOne.setRequiredTimeLimitSpecial(selectBusinessGuideFilingOneReq.getBusinessGuideFilingOne().getRequiredTimeLimitSpecial());
                //计算法定办结时限内 statutoryCompletionTimeLimitSpecial
                businessGuideFilingOne.setStatutoryCompletionTimeLimitSpecial(selectBusinessGuideFilingOneReq.getBusinessGuideFilingOne().getStatutoryCompletionTimeLimitSpecial());
                //办理流程图 processFlowChart
                businessGuideFilingOne.setProcessFlowChart(selectBusinessGuideFilingOneReq.getBusinessGuideFilingOne().getProcessFlowChart());
                //特殊处理字段
                //是否自贸区事项 ftaMatters
                businessGuideFilingOne.setFtaMatters(checkTrueAndFalse(businessGuideFilingOne.getFtaMatters()));
//                是否收费 charge
                businessGuideFilingOne.setCharge(checkTrueAndFalse(businessGuideFilingOne.getCharge()));
//                是否有网上服务系统 onlineServiceSystem
                businessGuideFilingOne.setOnlineServiceSystem(checkTrueAndFalse(businessGuideFilingOne.getOnlineServiceSystem()));
//                是否有中介服务 intermediaryService
                businessGuideFilingOne.setIntermediaryService(checkTrueAndFalse(businessGuideFilingOne.getIntermediaryService()));
//                 是否收费（特殊）chargeSpecial
                businessGuideFilingOne.setChargeSpecial(checkTrueAndFalse(businessGuideFilingOne.getChargeSpecial()));


                //取得私有数据
                BeanUtil.fillBeanWithMap(newMap, businessGuideFilingOnePrivate, true);
                //放入事项id
                businessGuideFilingOnePrivate.setDepartmentalMattersId(departmentalMattersAndEventRes.getId());

                //特殊处理字段
//               是否有网上服务系统 onlineServiceSystem
                businessGuideFilingOnePrivate.setOnlineServiceSystem(checkTrueAndFalse(businessGuideFilingOnePrivate.getOnlineServiceSystem()));
                //放入版本
                businessGuideFilingOnePrivate.setVersion(version);
                //封装传给前端的数据
                businessGuideFilingOneReq.setBusinessGuideFilingOne(businessGuideFilingOne);
                businessGuideFilingOneReq.setBusinessGuideFilingOnePrivate(businessGuideFilingOnePrivate);
                //将给前端的数据放入泛型类
                importExcelResSet(businessGuideFilingOneReq, importExcelRes);
                break;
            //4.其他服务类
            case "4":
                BusinessGuideOtherServicesOne businessGuideOtherServicesOne = new BusinessGuideOtherServicesOne();
                BusinessGuideOtherServicesOnePrivate businessGuideOtherServicesOnePrivate = new BusinessGuideOtherServicesOnePrivate();
                BusinessGuideOtherServicesOneReq businessGuideOtherServicesOneReq = new BusinessGuideOtherServicesOneReq();
                BusinessGuideOtherServicesOneReq selectBusinessGuideOtherServicesOneReq = businessGuideOtherServicesOneDao.findByEventId(departmentalMattersAndEventRes.getId());
                //判断事项存在
                if (selectBusinessGuideOtherServicesOneReq == null) {
                    throw new CustomException(new ReturnCode.Builder().failed().msg("该事项不存在办事指南。").build());
                }
                //判断事项存在
                if (selectBusinessGuideOtherServicesOneReq.getBusinessGuideOtherServicesOne() == null) {
                    throw new CustomException(new ReturnCode.Builder().failed().msg("该事项不存在办事指南数据。").build());
                }
                //反射取得字段的ColumnName
                newMap = ImportUtil.getNameEverseColumn(BusinessGuideOtherServicesOne.class, map);
                //反射取得私有字段的ColumnName
                newMap.putAll(ImportUtil.getNameEverseColumn(BusinessGuideOtherServicesOnePrivate.class, map));
                //取得公有数据对象
                BeanUtil.fillBeanWithMap(newMap, businessGuideOtherServicesOne, true);
                //设置图片默认值
                businessGuideOtherServicesOne.setResultSample("");
                businessGuideOtherServicesOne.setAddendumToProcessingTimeOrPlace("");
                //放入事项id
                businessGuideOtherServicesOne.setEventId(departmentalMattersAndEventRes.getId());
                //放入目录名称
                businessGuideOtherServicesOne.setItemCatalogue(departmentalMattersAndEventRes.getDirectoryName());
                //事项名称
                businessGuideOtherServicesOne.setItemName(departmentalMattersAndEventRes.getEventName());
                //放入事项大类型
                businessGuideOtherServicesOne.setMajorTypesOfEvents(majorTypesOfEvents);
                //放入事项小类型
                businessGuideOtherServicesOne.setItemSmallType(departmentalMattersAndEventRes.getItemSmallType());
                //放入服务对象
                businessGuideOtherServicesOne.setServiceObject(departmentalMattersAndEventRes.getServiceObject());
                //放入基本编码
                businessGuideOtherServicesOne.setBasicCoding(departmentalMattersAndEventRes.getDirectoryEncoding());
                //放入行使层级
                businessGuideOtherServicesOne.setExerciseHierarchy(departmentalMattersAndEventRes.getExerciseHierarchy());
                //放入实施(服务)编码
                businessGuideOtherServicesOne.setCodingImplementation(departmentalMattersAndEventRes.getCodingImplementation());

                //放入页面禁止使用的字段
                //办理时间或地点类型 typeOfProcessingTimeOrPlace
                businessGuideOtherServicesOne.setTypeOfProcessingTimeOrPlace(selectBusinessGuideOtherServicesOneReq.getBusinessGuideOtherServicesOne().getTypeOfProcessingTimeOrPlace());

                //特殊处理字段
//                是否收费           charge

                businessGuideOtherServicesOne.setCharge(checkTrueAndFalse(businessGuideOtherServicesOne.getCharge()));

//                是否中介服务     intermediaryService
                businessGuideOtherServicesOne.setIntermediaryService(checkTrueAndFalse(businessGuideOtherServicesOne.getIntermediaryService()));
//                是否有服务限制  serviceRestrictions
                businessGuideOtherServicesOne.setServiceRestrictions(checkTrueAndFalse(businessGuideOtherServicesOne.getServiceRestrictions()));

//                是否有申请材料  applicationMaterials
                businessGuideOtherServicesOne.setApplicationMaterials(checkTrueAndFalse(businessGuideOtherServicesOne.getApplicationMaterials()));

//                是否有结果样本  havaResultSample
                businessGuideOtherServicesOne.setHavaResultSample(checkTrueAndFalse(businessGuideOtherServicesOne.getHavaResultSample()));

                //取得私有数据
                BeanUtil.fillBeanWithMap(newMap, businessGuideOtherServicesOnePrivate, true);
                //放入事项id
                businessGuideOtherServicesOnePrivate.setDepartmentalMattersId(departmentalMattersAndEventRes.getId());
                businessGuideOtherServicesOnePrivate.setAddendumToProcessingTimeOrPlace("");
                //放入版本
                businessGuideOtherServicesOnePrivate.setVersion(version);
                //封装传给前端的数据
                businessGuideOtherServicesOneReq.setBusinessGuideOtherServicesOne(businessGuideOtherServicesOne);
                businessGuideOtherServicesOneReq.setBusinessGuideOtherServicesOnePrivate(businessGuideOtherServicesOnePrivate);
                //将给前端的数据放入泛型类
                importExcelResSet(businessGuideOtherServicesOneReq, importExcelRes);
                break;
            default:
                throw new CustomException(new ReturnCode.Builder().failed().msg("导入的事项类型不确定。").build());
        }

    }

    private String checkTrueAndFalse(String value) {

        String returnValue = "";
        if(value == null){
            value = "";
        }
        switch (value) {
            case "是":
                returnValue = "是";
                break;
            case "否":
                returnValue = "否";
                break;
            case "有":
                returnValue = "是";
                break;
            case "无":
                returnValue = "否";
                break;
            default:

                break;

        }

        return returnValue;
    }


    /**
     * 保存各sheet图片ObjectData对象
     */
    private void importExcelObjectData(Sheet sheet, Integer id, ImportExcelRes<Object> importExcelRes, OSSClient ossClient) throws Exception {
        //图片保存
        Map<Integer, List> objectMap = new LinkedHashMap<>();
        List<Map> list = new ArrayList<>();
        EmbeddedExtractor embeddedExtractor = new EmbeddedExtractor();


        List<EmbeddedData> embeddedDataList = null;
        try {
            embeddedDataList = embeddedExtractor.extractAll(sheet);
        } catch (Exception e) {
            e.printStackTrace();
            objectMap.put(id, list);
            importExcelRes.getObjectDataMap().putAll(objectMap);
        }

        if (embeddedDataList == null) {
            return;
        }
        for (EmbeddedData ed : embeddedDataList) {

            Map<String, String> map = new HashMap<>();
            String fileName = ed.getFilename();
            byte[] bytes = fileName.getBytes(Charset.forName("iso8859-1"));
            fileName = new String(bytes, "GBK");
            System.out.println(fileName + " (" + ed.getContentType() + ") - " + ed.getEmbeddedData().length + " bytes");
            byte[] dataBuffer = ed.getEmbeddedData();
            if (dataBuffer == null) {
                continue;
            }
            String suffix = fileName.substring(fileName.lastIndexOf("."));
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String dateString = dateFormat.format(new Date());
            String timeMillis = String.valueOf(System.currentTimeMillis());
            String filePath = dateString + "/" + timeMillis + suffix;
            InputStream inputStream = new ByteArrayInputStream(dataBuffer);
            //保存图片到oss
            OssClientUtil.uploadFileByInputStream(ossClient, inputStream, filePath);
            map.put("name", fileName);
            map.put("url", filePath);
            list.add(map);

        }
        objectMap.put(id, list);
        importExcelRes.getObjectDataMap().putAll(objectMap);
    }


    private static <T> void importExcelResSet(T bean, ImportExcelRes<T> importExcelRes) {
        //将给前端的数据放入泛型类
        importExcelRes.setBusinessGuide(bean);
    }


    private String replaceAllRegex(String oldString, String regex) {
        String newString = oldString.replaceAll("\\s+", ",").replaceAll(regex, ",");
        return newString;
    }

}
