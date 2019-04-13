package com.runonce.service.impl.export;

import com.alibaba.fastjson.JSON;
import com.aliyun.oss.OSSClient;
import com.runonce.ReturnCode;
import com.runonce.dao.eventversionone.DepartmentalMattersDao;
import com.runonce.dao.eventversionone.FormDescriptionDao;
import com.runonce.httpbean.assets.reqbean.MinderDataReq;
import com.runonce.httpbean.assets.resbean.FromDescriptionInfoRes;
import com.runonce.model.common.SystemBean;
import com.runonce.model.eventpublic.MinderData;
import com.runonce.model.eventversionone.SystemDocking;
import com.runonce.model.eventversiontwo.QuantitativeRendering;
import com.runonce.model.http.MaterialGroupAddParam;
import com.runonce.service.eventpublic.MinderDataService;
import com.runonce.service.eventversionone.ExportExcelService;
import com.runonce.service.eventversionone.MaterialGroupingService;
import com.runonce.service.eventversionone.SystemDockingService;
import com.runonce.service.eventversiontwo.QuantitativeRenderingService;
import com.runonce.service.export.DataExportService;
import com.runonce.service.export.OtherExportService;
import com.runonce.util.*;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;

import org.apache.poi.ss.util.ImageUtils;
import org.apache.poi.util.Units;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;


/**
 * @author swq
 * @date 2018/12/20 0020
 */
@Slf4j
@Service
public class DataExportServiceImpl implements DataExportService {


    @javax.annotation.Resource
    private FormDescriptionDao formDescriptionDao;

    @javax.annotation.Resource
    private DepartmentalMattersDao departmentalMattersDao;
    private final MaterialGroupingService materialGroupingService;

    private final QuantitativeRenderingService quantitativeRenderingService;

    private final MinderDataService minderDataService;

    private final SystemDockingService systemDockingService;

    private final OtherExportService otherExportService;

    private final ExportExcelService exportExcelService;


    @Autowired
    public DataExportServiceImpl(MaterialGroupingService materialGroupingService, QuantitativeRenderingService quantitativeRenderingService, MinderDataService minderDataService, SystemDockingService systemDockingService, OtherExportService otherExportService, ExportExcelService exportExcelService) {
        this.materialGroupingService = materialGroupingService;
        this.quantitativeRenderingService = quantitativeRenderingService;
        this.minderDataService = minderDataService;
        this.systemDockingService = systemDockingService;
        this.otherExportService = otherExportService;
        this.exportExcelService = exportExcelService;
    }

    /**
     * 模板文件位置
     *
     * @param local
     * @return
     */
    @Override
    public String getMould(String local) {
        try {
            File path = new File(ResourceUtils.getURL("classpath:").getPath());
            if (!path.exists()) {
                path = new File("");
            }
            String parent = path.getAbsolutePath();
            File upload = new File(parent, local);
            return upload.getAbsolutePath();
        } catch (IOException e) {
            return "../" + local;
        }
    }


    /**
     * 下载
     *
     * @param fileName 文件名
     * @return 文件源
     */
    @Override
    public ResponseEntity<Resource> download(String path, String fileName) {

        String filePath = getMould(path);
        Resource file = loadAsResource(fileName, filePath);
        try {
            fileName = new String((fileName).getBytes(), "iso-8859-1" + SystemBean.EXPORT_FORMAT_XLSX);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + fileName)
                //.header("content-type", "application/vnd.ms-excel;charset=utf-8")
                .header("content-type", "application/octet-stream;charset=utf-8")
                .body(file);
    }


    /**
     * 上传
     *
     * @param pathName 地址
     * @param fileName 文件名
     * @param request  请求
     * @return FileOutputStream 文件
     */
    @Override
    public FileOutputStream upload(String pathName, String fileName, HttpServletRequest request) {
        File cat = new File(request.getSession().getServletContext().getRealPath("/"));
        String path = cat.getParent() + pathName;
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }


        String strPath = path + fileName;
        File outFile = new File(strPath);
        // 检测是否存在目录
        if (!outFile.getParentFile().exists()) {
            outFile.getParentFile().mkdirs();
        }
        // 输出到文件
        FileOutputStream fo = null;
        try {
            fo = new FileOutputStream(outFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return fo;
    }

    /**
     * 导出Excel
     *
     * @return ReturnCode
     */
    @Override
    public ReturnCode showExcel(String eventId, OSSClient ossClient, HttpServletRequest request) throws Exception {

        String id = eventId;

        String filePath = getMould(SystemBean.EXPORT_LOCAL);
        File outFile = new File(filePath, "untangleMould_2.0.xlsx");

        Map map = departmentalMattersDao.findEventNameAndType(eventId);

        if (map == null || map.size() == 0) {
            return new ReturnCode.Builder().failed().msg("该事项不存在").build();
        }

        if (map.containsKey("mappingId")) {
            eventId = map.get("mappingId").toString();
        }

        if (StringUtils.isEmpty(eventId)) {
            return new ReturnCode.Builder().failed().msg("该映射id不存在").build();
        }

        String eventName = null;
        String eventType = null;
        String eventTypeName = null;
        if (map.containsKey("eventName")) {

            eventName = map.get("eventName").toString();
            if (StringUtils.isEmpty(eventName)) {
                eventName = "未填写事项名";
            }
        }

        if (map.containsKey("eventTypeName")) {

            eventTypeName = map.get("eventTypeName").toString();

            if (StringUtils.isEmpty(eventTypeName)) {
                eventTypeName = "未填写事项类型";
            }
        }

        if (map.containsKey("eventType")) {
            eventType = map.get("eventType").toString();
            if (StringUtils.isEmpty(eventName)) {
                eventType = "0";
            }
        }

        //文件流
        FileInputStream fs = new FileInputStream(outFile);
        //加载模板
        XSSFWorkbook workbook = new XSSFWorkbook(fs);
        String sheetName;
        //封面
        sheetName = "封面";
        XSSFSheet sheet = workbook.getSheet(sheetName);
        sheet.getRow(1).getCell(0).setCellValue("《 " + eventName + " 》");

//        //目录 （sheet名称）
//        sheetName = "目录";
//        sheet = workbook.getSheet(sheetName);
//        sheet.getRow(4).getCell(4).setCellValue(eventTypeName);
//
//        //办事指南
//        otherExportService.businessGuideExport(workbook, id, eventType, ossClient);
//
//        //资格预审（sheet名称）
//        sheetName = "资格预审";
//        otherExportService.prequalificationExport(workbook, sheetName, eventId, ossClient);

        sheetName = "办理流程图";
        otherExportService.flowChartExport(workbook, sheetName, eventId, ossClient);
//
//        //办事情形（sheet名称）
//        sheetName = "办事情形";
//        MinderDataReq minderDataReq = minderDataService.selectByEventId(eventId, 2);
//
//        if (minderDataReq != null) {
//            MinderData minderData = minderDataReq.getMinderData();
//            if (minderData != null) {
//                sheet6(workbook, sheetName, minderData, ossClient);
//            }
//        }
//        //材料分组（sheet名称）
//        sheetName = "材料分组";
//        exportExcelService.makeMaterialGroup(workbook, sheetName, eventName, eventId);
//
//
//        //表单说明（sheet名称）
//        sheetName = "表单说明";
//        List<FromDescriptionInfoRes> fromDescriptionInfoResList = formDescriptionDao.selectFormDescriptionInfo(eventId, "申请表");
//
//        if (fromDescriptionInfoResList != null && fromDescriptionInfoResList.size() > 0) {
//            sheet8(workbook, sheetName, fromDescriptionInfoResList, ossClient);
//        }
//
//        //图例样表&审查细则（sheet名称）
//        sheetName = "图例样表&审查细则";
//        List<MaterialGroupAddParam> materialGroupAddParamList = materialGroupingService.selectByEventIdAndIo(eventId, "I");
//        if (materialGroupAddParamList != null && materialGroupAddParamList.size() > 0) {
//            sheet10(workbook, sheetName, materialGroupAddParamList, ossClient);
//        }
//
//        //量化呈现图（sheet名称）
//        sheetName = "量化呈现图";
//        QuantitativeRendering quantitativeRendering = quantitativeRenderingService.findBy("eventId", eventId);
//        if (quantitativeRendering != null) {
//            sheet11(workbook, sheetName, quantitativeRendering, ossClient);
//        }
//
//        //系统对接方案（sheet名称）
//        sheetName = "系统对接方案";
//        ReturnCode code = systemDockingService.selectByEventId(eventId);
//        SystemDocking systemDocking = (SystemDocking) code.getObj();
//        if (systemDocking != null) {
//            sheet12(workbook, sheetName, systemDocking, ossClient);
//        }


        //存放的文件名称
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(new Date());

        if (eventName == null) {
            eventName = "没有事项名";

        }
        String fileName = eventName + dateString + ".xlsx";
        // 输出到文件
        FileOutputStream fo = upload(SystemBean.DOWNLOAD_LOCAL, fileName, request);


        ossClient.shutdown();
        workbook.write(fo);
        workbook.unLock();
        workbook.close();

        fs.close();
        return new ReturnCode.Builder().success().msg("导出成功").object(SystemBean.DOWNLOAD_LOCAL + fileName).build();

    }


    /**
     * 图例样表&审查细则
     *
     * @param workbook                  工作簿
     * @param sheetName                 sheet名
     * @param materialGroupAddParamList 图例样表&审查细则数据
     */
    private void sheet10(XSSFWorkbook workbook, String sheetName, List<MaterialGroupAddParam> materialGroupAddParamList, OSSClient ossClient) throws Exception {

        //读取sheet固定表名的index
        int index = workbook.getSheetIndex(sheetName);
        //读取index的sheet
        XSSFSheet sheet = workbook.getSheetAt(index);

        //头部行
        int headRowNum = 2;
        //数据行
        int dateRowNum = headRowNum + 1;

        int infoRowNum = dateRowNum + materialGroupAddParamList.size() + 3;
        XSSFRow columnNameRow = sheet.getRow(headRowNum);

        // 准备下拉列表数据
        String[] booleanStrings = new String[]{"是", "否"};
        String[] strings = new String[]{"系统自动审查", "窗口人员人工审查", "审批人员人工审查"};
        //取得标题起始列
        int startCell = 1;

        //取得数据
        for (int i = 0; i < materialGroupAddParamList.size(); i++) {
            //开始列
            int infoStartCell = 0;
            int infoEndCell = 0;
            Map<String, Object> map = ExportUtil.getApiKeyAndValue(materialGroupAddParamList.get(i));
            map.put("序号", i + 1);
            //数据行处理
            XSSFRow row;

            //判断是否没有该行
            if (sheet.getRow(dateRowNum + i) == null) {
                row = sheet.createRow(dateRowNum + i);

            } else {
                row = sheet.getRow(dateRowNum + i);
            }
            //自动行高
            for (int j = 0; j < map.keySet().size(); j++) {

                //单元格格子样式
                CellStyle cellStyle = workbook.createCellStyle();
                cellStyle.setBorderTop(BorderStyle.THIN);
                cellStyle.setBorderLeft(BorderStyle.THIN);
                cellStyle.setBorderRight(BorderStyle.THIN);
                cellStyle.setBorderBottom(BorderStyle.THIN);

                //水平靠左对齐
                cellStyle.setAlignment(HorizontalAlignment.CENTER);
                //垂直居中对齐
                cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
                //自动换行
                cellStyle.setWrapText(true);

                //设置字体
                XSSFFont font = workbook.createFont();
                //颜色
                font.setColor(XSSFFont.DEFAULT_FONT_COLOR);
                //字体
                font.setFontName("微软雅黑");
//              //设置斜体
//              font.setItalic(true);
                //设置字体大小
                font.setFontHeightInPoints((short) 9);

                cellStyle.setFont(font);

                //最后一行时候样式
                if (i == materialGroupAddParamList.size() - 1) {
                    cellStyle.setBorderBottom(BorderStyle.MEDIUM);
                }
                //标题列
                XSSFCell cell = columnNameRow.getCell(j + startCell);
                XSSFCell cellDate;

                if (cell == null) {
                    continue;
                }
                String cellValue = cell.getStringCellValue().replaceAll("\r|\n*", "").trim();
                //判断是否没有该列
                if (row.getCell((short) j + startCell) == null) {
                    cellDate = row.createCell((short) j + startCell);
                } else {
                    cellDate = row.getCell((short) j + startCell);
                }
                if (!StringUtils.isEmpty(cellValue)) {
                    if (map.containsKey(cellValue)) {

                        Object objectValue = map.get(cellValue) == null ? "" : map.get(cellValue);
                        if ("序号".equals((cellValue))) {
                            cellStyle.setBorderLeft(BorderStyle.MEDIUM);

                        }

                        if ("受理人员审查方式".equals(cellValue) || "审批人员审查方式".equals(cellValue)) {
                            // 设置第一列的1-10行为下拉列表
                            CellRangeAddressList cellRangeAddressList = new CellRangeAddressList(dateRowNum, dateRowNum + i, cellDate.getColumnIndex(), cellDate.getColumnIndex());
                            DataValidationHelper helper = sheet.getDataValidationHelper();
                            DataValidationConstraint constraint = helper.createExplicitListConstraint(strings);
                            DataValidation dataValidation = helper.createValidation(constraint, cellRangeAddressList);
                            sheet.addValidationData(dataValidation);
                        }

                        if ("审批人员审查方式".equals(cellValue)) {
                            cellStyle.setBorderRight(BorderStyle.MEDIUM);
                            infoEndCell = cellDate.getColumnIndex();
                        }

                        if ("受理人员审查".equals(cellValue) || "审批人员审查".equals(cellValue)) {
                            Boolean bool = Boolean.parseBoolean(objectValue.toString());
                            if (bool) {
                                objectValue = "√";
                            } else {
                                objectValue = "";
                            }


                            if ("受理人员审查".equals(cellValue)) {
                                infoStartCell = cellDate.getColumnIndex();
                            }
                        }

                        if ("可通过数据共享进行减免（是/否）".equals(cellValue)) {

                            // 设置第一列的1-10行为下拉列表
                            CellRangeAddressList cellRangeAddressList = new CellRangeAddressList(dateRowNum, dateRowNum + i, cellDate.getColumnIndex(), cellDate.getColumnIndex());
                            DataValidationHelper helper = sheet.getDataValidationHelper();
                            DataValidationConstraint constraint = helper.createExplicitListConstraint(booleanStrings);
                            DataValidation dataValidation = helper.createValidation(constraint, cellRangeAddressList);
                            sheet.addValidationData(dataValidation);

                            System.out.println(objectValue.toString());
                            Boolean bool = Boolean.parseBoolean(objectValue.toString());

                            if (bool) {
                                objectValue = "是";
                            } else {
                                objectValue = "否";
                            }
                        }

                        cellDate.setCellStyle(cellStyle);
                        cellDate.setCellValue(objectValue.toString());
                    }
                }
            }

//            float endHeight = 0;
//
//            for (int j = infoRowNum; j < 2 + infoRowNum; j++) {
//                Row infoRow;
//                if (sheet.getRow(infoRowNum + j) == null) {
//                    infoRow = sheet.createRow(infoRowNum + j);
//
//                } else {
//                    infoRow = sheet.getRow(infoRowNum + j);
//                }
//                endHeight = endHeight + (infoRow.getHeightInPoints() / Units.POINT_DPI) * Units.PIXEL_DPI;
//            }

            //详情图片
            //oos对象存储 缩放图片
            if (StringUtils.isEmpty(materialGroupAddParamList.get(i).getSampleTable())) {
                continue;
            }
            Map<String, List<String>> maps = JSON.parseObject(materialGroupAddParamList.get(i).getSampleTable(), Map.class);

            List<String> objectNameList = maps.get("imgs");


            float endWidth = 0;
            for (int j = 1; j <= 1; j++) {

                endWidth += sheet.getColumnWidthInPixels(j);

            }

          int rowNum =  createHorizontalImg(workbook, sheet, objectNameList, ossClient, infoRowNum, 1, endWidth, null, 0);

            //详细
            //受理人员审查要点
            for (int j = 0; j < materialGroupAddParamList.get(i).getAcceptance().size() + 2; j++) {
                Row infoRow;
                if (sheet.getRow(infoRowNum + j) == null) {
                    infoRow = sheet.createRow(infoRowNum + j);

                } else {
                    infoRow = sheet.getRow(infoRowNum + j);
                }

                //前两行标题的处理
                for (int k = infoStartCell; k <= infoEndCell; k++) {


                    //单元格格子样式
                    CellStyle cellStyle = workbook.createCellStyle();
                    //水平靠左对齐
                    cellStyle.setAlignment(HorizontalAlignment.CENTER);
                    //垂直居中对齐
                    cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
                    //自动换行
                    cellStyle.setWrapText(true);

                    //设置字体
                    XSSFFont font = workbook.createFont();
                    //颜色
                    font.setColor(XSSFFont.DEFAULT_FONT_COLOR);
                    //字体
                    font.setFontName("微软雅黑");
//                  //设置斜体
//                  font.setItalic(true);
                    //设置字体大小
                    font.setFontHeightInPoints((short) 9);
                    cellStyle.setFont(font);
                    cellStyle.setBorderTop(BorderStyle.THIN);
                    cellStyle.setBorderLeft(BorderStyle.THIN);
                    cellStyle.setBorderRight(BorderStyle.THIN);
                    cellStyle.setBorderBottom(BorderStyle.THIN);
                    Cell infoCell;
                    if (infoRow.getCell(k) == null) {
                        infoCell = infoRow.createCell(k);
                    } else {
                        infoCell = infoRow.getCell(k);
                    }

                    if (j == 0) {
                        infoCell.setCellValue(materialGroupAddParamList.get(i).getName());
                        cellStyle.setBorderTop(BorderStyle.MEDIUM);
                        cellStyle.setBorderLeft(BorderStyle.MEDIUM);
                        cellStyle.setBorderRight(BorderStyle.MEDIUM);
                        font.setFontHeightInPoints((short) 11);
                        font.setBold(true);
                        cellStyle.setFont(font);
                        //合并单元格
                        if (k == infoStartCell) {
                            CellRangeAddress cra = new CellRangeAddress(infoRowNum + j, infoRowNum + j, k, infoEndCell);
                            sheet.addMergedRegion(cra);
                        }
                        infoCell.setCellStyle(cellStyle);
                        continue;
                    }
                    if (j == 1) {
                        if (k == infoStartCell) {
                            cellStyle.setBorderLeft(BorderStyle.MEDIUM);
                            infoCell.setCellValue("序号");
                        }
                        if (k == infoStartCell + 1) {
                            infoCell.setCellValue("受理人员审查要点");
                            CellRangeAddress cra = new CellRangeAddress(infoRowNum + j, infoRowNum + j, k, infoEndCell);
                            //合并单元格
                            sheet.addMergedRegion(cra);
                        }

                        if (k == infoEndCell) {
                            cellStyle.setBorderRight(BorderStyle.MEDIUM);
                        }
                        infoCell.setCellStyle(cellStyle);
                        continue;

                    }
                    //数据行
                    if (k == infoStartCell) {
                        infoCell.setCellValue(j - 1);
                        cellStyle.setBorderLeft(BorderStyle.MEDIUM);
                    }
                    if (k == infoStartCell + 1) {
                        String content = materialGroupAddParamList.get(i).getAcceptance().get(j - 2).getContent();
                        infoCell.setCellValue(content == null ? "" : content);
                        CellRangeAddress cra = new CellRangeAddress(infoRowNum + j, infoRowNum + j, k, infoEndCell);
                        //合并单元格
                        sheet.addMergedRegion(cra);
                    }
                    if (k == infoEndCell) {
                        cellStyle.setBorderRight(BorderStyle.MEDIUM);
                    }
                    //最后一行处理
                    if (j == materialGroupAddParamList.get(i).getAcceptance().size() + 1) {
                        cellStyle.setBorderBottom(BorderStyle.MEDIUM);
                    }
                    infoCell.setCellStyle(cellStyle);
                }
            }

            infoRowNum = infoRowNum + materialGroupAddParamList.get(i).getAcceptance().size() + 2;


            //审核人员审批要点
            for (int j = 0; j < materialGroupAddParamList.get(i).getApproval().size() + 1; j++) {


                Row infoRow;
                if (sheet.getRow(infoRowNum + j) == null) {
                    infoRow = sheet.createRow(infoRowNum + j);

                } else {
                    infoRow = sheet.getRow(infoRowNum + j);
                }


                //前两行标题的处理

                for (int k = infoStartCell; k <= infoEndCell; k++) {

                    //单元格格子样式
                    CellStyle cellStyle = workbook.createCellStyle();
                    //水平靠左对齐
                    cellStyle.setAlignment(HorizontalAlignment.CENTER);
                    //垂直居中对齐
                    cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
                    //自动换行
                    cellStyle.setWrapText(true);

                    //设置字体
                    XSSFFont font = workbook.createFont();
                    //颜色
                    font.setColor(XSSFFont.DEFAULT_FONT_COLOR);
                    //字体
                    font.setFontName("微软雅黑");
                    //设置字体大小
                    font.setFontHeightInPoints((short) 9);
                    cellStyle.setFont(font);
                    cellStyle.setBorderTop(BorderStyle.THIN);
                    cellStyle.setBorderLeft(BorderStyle.THIN);
                    cellStyle.setBorderRight(BorderStyle.THIN);
                    cellStyle.setBorderBottom(BorderStyle.THIN);
                    Cell infoCell;
                    if (infoRow.getCell(k) == null) {
                        infoCell = infoRow.createCell(k);
                    } else {
                        infoCell = infoRow.getCell(k);
                    }

                    if (j == 0) {
                        if (k == infoStartCell) {
                            cellStyle.setBorderLeft(BorderStyle.MEDIUM);
                            infoCell.setCellValue("序号");

                        }
                        if (k == infoStartCell + 1) {
                            infoCell.setCellValue("审核人员审批要点");
                            //合并单元格
                            CellRangeAddress cra = new CellRangeAddress(infoRowNum + j, infoRowNum + j, k, infoEndCell);
                            sheet.addMergedRegion(cra);
                        }
                        if (k == infoEndCell) {
                            cellStyle.setBorderRight(BorderStyle.MEDIUM);
                        }

                        if (0 == materialGroupAddParamList.get(i).getAcceptance().size()) {
                            cellStyle.setBorderBottom(BorderStyle.MEDIUM);
                        }
                        infoCell.setCellStyle(cellStyle);
                        continue;
                    }


                    if (k == infoStartCell) {
                        infoCell.setCellValue(j);
                        cellStyle.setBorderLeft(BorderStyle.MEDIUM);
                    }
                    if (k == infoStartCell + 1) {
                        String content = materialGroupAddParamList.get(i).getApproval().get(j - 1).getContent();
                        infoCell.setCellValue(content == null ? "" : content);
                        //合并单元格
                        sheet.addMergedRegion(new CellRangeAddress(infoRowNum + j, infoRowNum + j, k, infoEndCell));
                    }

                    if (k == infoEndCell) {
                        cellStyle.setBorderRight(BorderStyle.MEDIUM);
                    }


                    //最后一行处理
                    if (j == materialGroupAddParamList.get(i).getApproval().size()) {
                        cellStyle.setBorderBottom(BorderStyle.MEDIUM);
                    }
                    infoCell.setCellStyle(cellStyle);


                    //后面数据行处理

                }
            }
            infoRowNum = infoRowNum + materialGroupAddParamList.get(i).getApproval().size() + 1 + 30 +rowNum;

        }

    }

    /**
     * 表单说明
     */
    private void sheet8(XSSFWorkbook workbook, String sheetName, List<FromDescriptionInfoRes> fromDescriptionInfoResList, OSSClient ossClient) throws Exception {

        //读取sheet固定表名的index
        int index = workbook.getSheetIndex(sheetName);
        //读取index的sheet
        XSSFSheet sheet = workbook.getSheetAt(index);

        if (fromDescriptionInfoResList == null || fromDescriptionInfoResList.isEmpty()) {
            return;
        }

        float endWidth = 0;
        for (int j = 1; j <= 4; j++) {
            endWidth += sheet.getColumnWidthInPixels(j);
        }

        //头部行
        int headRowNum = 3;
        //数据行
        int dateRowNum = headRowNum + 1;
        XSSFRow columnNameRow = sheet.getRow(headRowNum);
        //取得标题起始列
        int startCell = 9;
        //取得数据
        for (FromDescriptionInfoRes fromDescriptionInfoRes : fromDescriptionInfoResList) {

            //标题行处理
            Row endRow;
            if (sheet.getRow(headRowNum) == null) {
                endRow = sheet.createRow(headRowNum);

            } else {

                endRow = sheet.getRow(headRowNum);
            }

            for (Iterator cellIt = columnNameRow.cellIterator(); cellIt.hasNext(); ) {
                Cell tmpCell = (Cell) cellIt.next();

                Cell newCell;
                if (endRow.getCell(tmpCell.getColumnIndex()) == null) {
                    newCell = endRow.createCell(tmpCell.getColumnIndex());

                } else {

                    newCell = endRow.getCell(tmpCell.getColumnIndex());
                }
                PoiUtil.copyCell(tmpCell, newCell, true, false);
            }


            float endHeight = (sheet.getRow(headRowNum).getHeightInPoints() / Units.POINT_DPI) * Units.PIXEL_DPI;
            for (int i = 0; i < fromDescriptionInfoRes.getFormDescriptionList().size(); i++) {


                //数据行处理
                XSSFRow row;
                Map map = ExportUtil.getApiKeyAndValue(fromDescriptionInfoRes.getFormDescriptionList().get(i));

                //判断是否没有该行
                if (sheet.getRow(dateRowNum + i) == null) {
                    row = sheet.createRow(dateRowNum + i);

                } else {
                    row = sheet.getRow(dateRowNum + i);
                }


                //自动行高
                for (int j = 0; j < map.keySet().size(); j++) {

                    //单元格格子样式
                    CellStyle cellStyle = workbook.createCellStyle();
                    cellStyle.setBorderTop(BorderStyle.THIN);
                    cellStyle.setBorderLeft(BorderStyle.THIN);
                    cellStyle.setBorderRight(BorderStyle.THIN);
                    cellStyle.setBorderBottom(BorderStyle.THIN);

                    //水平靠左对齐
                    cellStyle.setAlignment(HorizontalAlignment.LEFT);
                    //垂直居中对齐
                    cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
                    //自动换行
                    cellStyle.setWrapText(true);

                    //设置字体
                    XSSFFont font = workbook.createFont();
                    //颜色
                    font.setColor(XSSFFont.DEFAULT_FONT_COLOR);

                    //字体
                    font.setFontName("微软雅黑");
                    //设置字体大小
                    font.setFontHeightInPoints((short) 9);

                    cellStyle.setFont(font);

                    //最后一行时候样式
                    if (i == fromDescriptionInfoRes.getFormDescriptionList().size() - 1) {
                        cellStyle.setBorderBottom(BorderStyle.MEDIUM);
                    }
                    //标题列
                    XSSFCell cell = columnNameRow.getCell(j + startCell);
                    XSSFCell cellDate;

                    if (cell == null) {
                        continue;
                    }
                    String cellValue = cell.getStringCellValue().trim();
                    //判断是否没有该列
                    if (row.getCell((short) j + startCell) == null) {
                        cellDate = row.createCell((short) j + startCell);
                    } else {
                        cellDate = row.getCell((short) j + startCell);
                    }


                    if (!StringUtils.isEmpty(cellValue)) {
                        if (map.containsKey(cellValue)) {

                            Object objectValue = map.get(cellValue) == null ? "" : map.get(cellValue);
                            if ("表单名称".equals((cellValue))) {
                                cellStyle.setBorderLeft(BorderStyle.MEDIUM);
                                //水平靠左对齐
                                cellStyle.setAlignment(HorizontalAlignment.CENTER);
                            }

                            if ("备注".equals(cellValue)) {
                                cellStyle.setBorderRight(BorderStyle.MEDIUM);
                            }

                            if ("是否复数存在".equals(cellValue) || "是否必填".equals(cellValue)) {
                                System.out.println(objectValue.toString());
                                Boolean bool = Boolean.parseBoolean(objectValue.toString());

                                if (bool) {
                                    objectValue = "是";
                                } else {
                                    objectValue = "否";
                                }
                            }


                            cellDate.setCellStyle(cellStyle);
                            cellDate.setCellValue(objectValue.toString());
                        }
                    }
                }
                endHeight = endHeight + (row.getHeightInPoints() / Units.POINT_DPI) * Units.PIXEL_DPI;
            }


            Map<String, List<String>> map = JSON.parseObject(fromDescriptionInfoRes.getFromDescriptionSampleTable(), Map.class);
            List<String> objectNameList = map.get("imgs");
            //oos对象存储 缩放图片
            createHorizontalImg(workbook, sheet, objectNameList, ossClient, headRowNum, 1, endWidth, endHeight, 0);

            //合并单元格
            if (fromDescriptionInfoRes.getFormDescriptionList().size() - 1 > 0) {
                sheet.addMergedRegion(new CellRangeAddress(dateRowNum, dateRowNum + fromDescriptionInfoRes.getFormDescriptionList().size() - 1, startCell, startCell));
            }


            //下个标题位置
            headRowNum = headRowNum + fromDescriptionInfoRes.getFormDescriptionList().size() + 10;
            dateRowNum = dateRowNum + fromDescriptionInfoRes.getFormDescriptionList().size() + 10;
        }
    }


    /**
     * 系统对接方案
     *
     * @param workbook      工作簿
     * @param sheetName     sheet名称
     * @param systemDocking 数据
     * @throws Exception 异常
     */
    private void sheet12(Workbook workbook, String sheetName, SystemDocking systemDocking, OSSClient ossClient) throws Exception {

        //读取sheet固定表名的index
        int index = workbook.getSheetIndex(sheetName);
        //读取index的sheet
        Sheet sheet = workbook.getSheetAt(index);
        float endWidth = 0;
        //所需要的图片宽度 只需要一边高自动计算
        for (int j = 0; j < 9; j++) {
            endWidth = endWidth + sheet.getColumnWidthInPixels(j);
        }
        List<String> objectNameList = new ArrayList<>();
        if (!StringUtils.isEmpty(systemDocking.getPicPath())) {
            objectNameList.add(systemDocking.getPicPath());
        }

        //oos对象存储
        createVerticalImg(workbook, sheet, objectNameList, ossClient, 3, 0, endWidth, null, 0);
    }

    /**
     * 量化呈现图
     *
     * @param workbook              工作簿
     * @param sheetName             sheet名
     * @param quantitativeRendering 量化呈现图数据
     */
    private void sheet11(Workbook workbook, String sheetName, QuantitativeRendering quantitativeRendering, OSSClient ossClient) throws Exception {

        //读取sheet固定表名的index
        int index = workbook.getSheetIndex(sheetName);
        //读取index的sheet
        Sheet sheet = workbook.getSheetAt(index);


        //减材料
        Row row1 = sheet.getRow(1);
        Cell cell11 = row1.getCell(1);
        if (!StringUtils.isEmpty(quantitativeRendering.getReductionMaterialDetail())) {
            cell11.setCellValue(quantitativeRendering.getReductionMaterialDetail());
        }
        Cell cell112 = row1.getCell(12);
        if (!StringUtils.isEmpty(quantitativeRendering.getReductionMaterialProblem())) {
            cell112.setCellValue(quantitativeRendering.getReductionMaterialProblem());
        }
        Row row3 = sheet.getRow(3);
        Cell cell312 = row3.getCell(12);
        cell312.setCellValue("优化前需提交材料" + quantitativeRendering.getBeforeMateriaReduction() + "个。" + "优化后提交材料减至" + quantitativeRendering.getAfterReducingMaterial() + "个。");


        //减环节
        Row row4 = sheet.getRow(4);
        Cell cell41 = row4.getCell(1);
        if (!StringUtils.isEmpty(quantitativeRendering.getReductionLinkDetail())) {
            cell41.setCellValue(quantitativeRendering.getReductionLinkDetail());
        }
        Cell cell412 = row4.getCell(12);
        if (!StringUtils.isEmpty(quantitativeRendering.getReductionLinkProblem())) {
            cell412.setCellValue(quantitativeRendering.getReductionLinkProblem());
        }
        Row row7 = sheet.getRow(7);
        Cell cell712 = row7.getCell(12);
        cell712.setCellValue("优化前环节数" + quantitativeRendering.getBeforeLinkReduction() + "个。" + "优化后环节数减至" + quantitativeRendering.getAfterLinkReduction() + "个。");

        //减时效
        Row row8 = sheet.getRow(8);
        Cell cell81 = row8.getCell(1);
        if (!StringUtils.isEmpty(quantitativeRendering.getReductionTimeDetail())) {
            cell81.setCellValue(quantitativeRendering.getReductionTimeDetail());
        }
        Cell cell812 = row8.getCell(12);
        if (!StringUtils.isEmpty(quantitativeRendering.getReductionTimeProblem())) {
            cell812.setCellValue(quantitativeRendering.getReductionTimeProblem());
        }
        Row row11 = sheet.getRow(11);
        Cell cell1112 = row11.getCell(12);
        cell1112.setCellValue("窗口受理时间平均" + quantitativeRendering.getBeforeTimeReduction() + quantitativeRendering.getTimeUnit() + "。" + "优化后减至" + quantitativeRendering.getAfterTimeReduction() + quantitativeRendering.getTimeUnit() + "。");

        //总体效果
        Row row12 = sheet.getRow(12);
        Cell cell121 = row12.getCell(1);
        if (!StringUtils.isEmpty(quantitativeRendering.getOverallEffectDetail())) {
            cell121.setCellValue(quantitativeRendering.getOverallEffectDetail());
        }
        Cell cell1212 = row12.getCell(12);
        if (!StringUtils.isEmpty(quantitativeRendering.getOverallProblem())) {
            cell1212.setCellValue(quantitativeRendering.getOverallProblem());
        }

        if (!StringUtils.isEmpty(quantitativeRendering.geteChartsPicPath())) {
            //获取图片流eChartsPicPath
            ByteArrayOutputStream byteArrayOutputStream = OssClientUtil.getObjectData(quantitativeRendering.geteChartsPicPath());

            if (byteArrayOutputStream == null) {
                return;
            }
            int pictureIdx = workbook.addPicture(byteArrayOutputStream.toByteArray(), Workbook.PICTURE_TYPE_JPEG);
            //关闭流
            byteArrayOutputStream.close();
            CreationHelper helper = sheet.getWorkbook().getCreationHelper();
            ClientAnchor anchor = helper.createClientAnchor();
            // 图片插入坐标
            anchor.setCol1(18);
            anchor.setRow1(1);
            anchor.setCol2(22);
            anchor.setRow2(18);
            anchor.setAnchorType(ClientAnchor.AnchorType.MOVE_DONT_RESIZE);
            Drawing patriarch = sheet.createDrawingPatriarch();
            patriarch.createPicture(anchor, pictureIdx);
        }

        float endWidth = 0;
        //所需要的图片宽度 只需要一边高自动计算
        for (int j = 0; j < 22; j++) {
            endWidth = endWidth + sheet.getColumnWidthInPixels(j);
        }
        List<String> objectNameList = new ArrayList<>();
        if (!StringUtils.isEmpty(quantitativeRendering.getRenderPicture())) {

            objectNameList.add(quantitativeRendering.getRenderPicture());
        }
        if (!StringUtils.isEmpty(quantitativeRendering.getDockingModePicture())) {
            objectNameList.add(quantitativeRendering.getDockingModePicture());
        }
        //oos对象存储
        createVerticalImg(workbook, sheet, objectNameList, ossClient, 19, 0, endWidth, null, 0);

    }

    /**
     * 办事情形
     *
     * @param workbook   工作簿
     * @param sheetName  sheet名
     * @param minderData 办事情形数据
     */
    private void sheet6(Workbook workbook, String sheetName, MinderData minderData, OSSClient ossClient) throws Exception {
        //读取sheet固定表名的index
        int index = workbook.getSheetIndex(sheetName);
        //读取index的sheet
        Sheet sheet = workbook.getSheetAt(index);

        float endWidth = 0;

        //所需要的图片宽度 只需要一边高自动计算
        for (int j = 0; j < 4; j++) {
            endWidth = endWidth + sheet.getColumnWidthInPixels(j);
        }
        List<String> objectNameList = new ArrayList<>();

        if (!StringUtils.isEmpty(minderData.getPicPath())) {
            objectNameList.add(minderData.getPicPath());
        }
        //oos对象存储
        createVerticalImg(workbook, sheet, objectNameList, ossClient, 2, 0, endWidth, null, 1);

    }

    /**
     * 图片做成(竖向)
     *
     * @param workbook       工作簿
     * @param finalSheet     sheet页
     * @param objectNameList 图片
     * @param startRowNum    开始行
     * @param startCellNum   开始列
     * @param endWidth       最终宽度 传null按最终宽度缩放
     * @param endHeight      最终高度 传null按最终高度缩放
     * @param rowValueCount  数据总计行数
     * @throws Exception 异常
     */
    @Override
    public void createVerticalImg(Workbook workbook, Sheet finalSheet, List<String> objectNameList, OSSClient ossClient, int startRowNum, int startCellNum, Float endWidth, Float endHeight, int rowValueCount) throws Exception {


        if (objectNameList == null) {

            return;
        }
        int bottomStartRowCountNum = startRowNum;

        Drawing patriarch = finalSheet.createDrawingPatriarch();
        Row row;
        if (finalSheet.getRow(startRowNum) == null) {

            row = finalSheet.createRow(startRowNum);
        } else {
            row = finalSheet.getRow(startRowNum);
        }

        float startRowHeight = (row.getHeightInPoints() / Units.POINT_DPI) * Units.PIXEL_DPI;

        for (String anObjectNameList : objectNameList) {
            //oss连接
            //获取图片流

            if (OssClientUtil.getObjectData(ossClient, anObjectNameList) == null) {
                return;
            }
            ByteArrayOutputStream byteArrayOutputStream = OssClientUtil.getObjectData(ossClient, anObjectNameList);
            if (byteArrayOutputStream == null) {
                continue;
            }

            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
            //关闭流
            byteArrayInputStream.close();
            BufferedImage bufferedImage = ImageIO.read(byteArrayInputStream);
            //图片宽
            float standardWidth = bufferedImage.getWidth();
            //图片高
            float standardHeight = bufferedImage.getHeight();
            double scale = 0;
            if (endWidth != null) {
                scale = div(endWidth + "", standardWidth + "");
            }
            if (endHeight != null) {
                scale = div(endHeight + "", standardHeight + "");
            }
            int width = (int) (scale * bufferedImage.getWidth());
            int height = (int) (scale * bufferedImage.getHeight());

            //放大缩小
            byteArrayOutputStream = ExportUtil.zoomByScale(width, height, bufferedImage);

            int pictureIdx = workbook.addPicture(byteArrayOutputStream.toByteArray(), Workbook.PICTURE_TYPE_JPEG);
            //关闭流
            byteArrayOutputStream.close();
            CreationHelper helper = finalSheet.getWorkbook().getCreationHelper();
            ClientAnchor anchor = helper.createClientAnchor();
            // 图片插入坐标
            anchor.setCol1(startCellNum);
            anchor.setRow1(bottomStartRowCountNum);
            anchor.setAnchorType(ClientAnchor.AnchorType.MOVE_DONT_RESIZE);
            Picture pict = patriarch.createPicture(anchor, pictureIdx);
            pict.resize();
            bottomStartRowCountNum = bottomStartRowCountNum + divToInt(height + "", startRowHeight + "");
        }
        //底部值处理
        bottomValue(finalSheet, startRowNum, rowValueCount, bottomStartRowCountNum);

    }


    /**
     * 图片做成(横向)
     *
     * @param workbook        工作簿
     * @param finalSheet      sheet页
     * @param objectsNameList 图片
     * @param startRowNum     开始行
     * @param startCellNum    开始列
     * @param maxWidth        最大宽度
     * @param maxHeight       最大高度
     * @throws Exception 异常
     */
    private int createHorizontalImg(Workbook workbook, Sheet finalSheet, List<String> objectsNameList, OSSClient ossClient, int startRowNum, int startCellNum, Float maxWidth, Float maxHeight, int rowValueCount) throws Exception {



        int bottomStartRowCountNum = startRowNum;
        if (objectsNameList == null || objectsNameList.size() <= 0) {
            return bottomStartRowCountNum;
        }
        for (String name : objectsNameList) {

            int imageHeightColumnNum = 0;
            byte[] bytes = OssClientUtil.getObjectForByte(ossClient, name);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
            BufferedImage bufferedImage = ImageIO.read(byteArrayInputStream);
            if (EXUtil.isPicture(name)) {
                Drawing patriarch = finalSheet.getDrawingPatriarch();
                if (patriarch == null) {
                    patriarch = finalSheet.createDrawingPatriarch();
                }

                //关闭流

                byteArrayInputStream.close();

                float imgWidth = bufferedImage.getWidth();
                //图片高
                float imgHeight = bufferedImage.getHeight();

                double scale;
                int width = 0;
                int height = 0;
                if (maxWidth != null) {
                    scale = div(maxWidth + "", imgWidth + "");
                    width = (int) (scale * imgWidth);
                    height = (int) (scale * imgHeight);
                }
                if (maxHeight != null) {
                    if (height > maxHeight) {
                        scale = div(maxHeight + "", imgHeight + "");
                        width = (int) (scale * imgWidth);
                        height = (int) (scale * imgHeight);

                    }
                }

                CreationHelper helper = finalSheet.getWorkbook().getCreationHelper();
                ClientAnchor anchor = helper.createClientAnchor();

                // 图片插入坐标
                anchor.setCol1(startCellNum);
                anchor.setRow1(bottomStartRowCountNum);

//
                // 取得起始行的列宽
                float cellWidth = finalSheet.getColumnWidthInPixels(bottomStartRowCountNum);
                // 取得起始列高
                float cellHeight = (float) ImageUtils.getRowHeightInPixels(finalSheet, bottomStartRowCountNum);

                imageHeightColumnNum= divToInt(height+"",cellHeight+"");
                // 计算需要的长宽比例的系数
                double a = width / cellWidth;
                double b = height / cellHeight;
                int pictureIdx = workbook.addPicture(bytes, Workbook.PICTURE_TYPE_JPEG);
                System.out.println("image :" + pictureIdx);
                anchor.setAnchorType(ClientAnchor.AnchorType.MOVE_DONT_RESIZE);
                Picture pict = patriarch.createPicture(anchor, pictureIdx);
                pict.resize(a, b);
            }
            EXUtil.embedObjectToCell((XSSFSheet) finalSheet, bytes, name, startCellNum + 2, bottomStartRowCountNum, 0, 0);
            bottomStartRowCountNum += imageHeightColumnNum;
            byteArrayInputStream.close();
        }

        return bottomStartRowCountNum;

    }


    /**
     * 底部值处理
     *
     * @param finalSheet        sheet
     * @param startValueRowNum  数据起始行
     * @param rowValueCount     数据共几行 0 不需要改变
     * @param bottomStartRowNum 数据插入到的起始行
     */
    private void bottomValue(Sheet finalSheet, int startValueRowNum, int rowValueCount, int bottomStartRowNum) {

        //底部值处理
        for (int i = startValueRowNum; i < rowValueCount + startValueRowNum; i++) {
            Row row;

            if (finalSheet.getRow(i) == null) {
                row = finalSheet.createRow(i);
            } else {
                row = finalSheet.getRow(i);
            }
            Row endRow = finalSheet.createRow(bottomStartRowNum);

            for (Iterator cellIt = row.cellIterator(); cellIt.hasNext(); ) {
                Cell tmpCell = (Cell) cellIt.next();

                Cell newCell;
                if (endRow.getCell(tmpCell.getColumnIndex()) == null) {
                    newCell = endRow.createCell(tmpCell.getColumnIndex());

                } else {

                    newCell = endRow.getCell(tmpCell.getColumnIndex());
                }
                PoiUtil.copyCell(tmpCell, newCell, true, true);

            }
        }
    }


    /**
     * 获取文件信息
     *
     * @param filename 文件名
     * @param path     地址
     * @return Resource 源
     */
    private Resource loadAsResource(String filename, String path) {
        Path file = load(filename, path);
        Resource resource;
        try {
            resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                return null;

            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }


    private Path load(String filename, String path) {
        // 获取文件存储地址
        Path rootLocation = Paths.get(path);
        return rootLocation.resolve(filename);
    }

    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指
     * BigDecimal.ROUND_UP进位
     *
     * @param v1 被除数
     * @param v2 除数
     * @return 两个参数的商
     */
    private static double div(String v1, String v2) {

        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.divide(b2, 5, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    private static int divToInt(String v1, String v2) {

        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return (int) b1.divide(b2, 0, BigDecimal.ROUND_UP).doubleValue();
    }

    /**
     * 提供精确的加法运算。
     *
     * @param v1 被加数
     * @param v2 加数
     * @return double 两个参数的和
     */
    public static double add(String v1, String v2) {
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(v2);
        return b1.add(b2).doubleValue();
    }
}
