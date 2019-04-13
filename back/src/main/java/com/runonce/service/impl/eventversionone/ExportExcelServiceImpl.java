package com.runonce.service.impl.eventversionone;

import com.aliyun.oss.OSSClient;
import com.runonce.ReturnCode;
import com.runonce.dao.eventversionone.ApplicationTypeFieldDao;
import com.runonce.dao.eventversionone.ExportExcelDao;
import com.runonce.model.eventversionone.ApplicationTypeField;
import com.runonce.model.http.MaterialGroupAddParam;
import com.runonce.service.eventversionone.ExportExcelService;
import com.runonce.service.eventversionone.MaterialGroupingService;
import com.runonce.util.Stringexcel;
import com.runonce.util.SysUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.Column;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.stream.Collectors;


@Service
public class ExportExcelServiceImpl implements ExportExcelService {


    @Resource
    private ExportExcelDao exportExcelDao;

    @Resource
    private ApplicationTypeFieldDao applicationTypeFieldDao;

    @Autowired
    private MaterialGroupingService materialGroupingService;

    @Override
    public List<List<ApplicationTypeField>> applicationTypeField(String eventId) {

        ApplicationTypeField bb = new ApplicationTypeField();

        bb.setEventId(eventId);
        bb.setParentId("-1");

        //查询申请类型父节点
        ApplicationTypeField ss = exportExcelDao.selectApplicationTypeFileByEventIdAndParentId(bb);

        if (ss == null) {
            return null;
        }
        //申请类型叶子节点个数
        Integer count = exportExcelDao.selectApplicationTypeFileLeafNodeCountByEventId(bb);


        List<ApplicationTypeField> alist = new ArrayList<>();

        alist.add(ss);

        List<ApplicationTypeField> allApplicationTypeField = new ArrayList<>();
        Integer a = 0;

        getChildren(alist, allApplicationTypeField, a, count);

        if (allApplicationTypeField == null || allApplicationTypeField.size() < 1) {

            return null;
        }
        //最大行
        int max = 0;

        for (ApplicationTypeField applicationType :
                allApplicationTypeField) {

            if (max < applicationType.getLevel()) {

                max = applicationType.getLevel();

            }
        }
        System.out.println(max);

        //最后一行
        List<List<ApplicationTypeField>> ApplicationTypeField3 = new ArrayList<>();

        List<ApplicationTypeField> ApplicationTypeField1 = new ArrayList<>();
        for (ApplicationTypeField ApplicationTypeField :
                allApplicationTypeField) {

            if (max == ApplicationTypeField.getLevel()) {

                ApplicationTypeField1.add(ApplicationTypeField);

            }
        }

        ApplicationTypeField3.add(ApplicationTypeField1);


        //其他行
        for (int k = 0; k < max - 1; k++) {
            List<ApplicationTypeField> ApplicationTypeField = ApplicationTypeField3.get(k);
            List<ApplicationTypeField> ApplicationTypeField2 = new ArrayList<>();
            for (int o = 0; o < ApplicationTypeField.size(); o++) {
                ApplicationTypeField s = new ApplicationTypeField();
                s.setId(ApplicationTypeField.get(o).getParentId());
                ApplicationTypeField ApplicationTypeFieldf = null;

                for (int w = 0; w < allApplicationTypeField.size(); w++) {

                    if (allApplicationTypeField.get(w).getId().equals(ApplicationTypeField.get(o).getParentId())) {

                        ApplicationTypeFieldf = allApplicationTypeField.get(w);
                        break;
                    }

                }

                ApplicationTypeField2.add(ApplicationTypeFieldf);
            }
            ApplicationTypeField3.add(ApplicationTypeField2);

        }

        //打印输出
        for (List<ApplicationTypeField> ApplicationTypeFields : ApplicationTypeField3
        ) {

            for (int i = 0; i < ApplicationTypeFields.size(); i++) {
                System.out.print(ApplicationTypeFields.get(i).getFieldName() + "|");
            }
            System.out.println();
        }


        return ApplicationTypeField3;
    }


    public void getChildren(List<ApplicationTypeField> ss, List<ApplicationTypeField> allApplicationTypeField, Integer a, Integer count) {
        a++;
        List<ApplicationTypeField> list = new ArrayList<>();
        if (ss != null && ss.size() > 0) {
            for (ApplicationTypeField sss : ss) {

                List<ApplicationTypeField> children = applicationTypeFieldDao.selectByParentId(sss.getId());

                if (children == null || children.size() < 1) {
                    ApplicationTypeField kk = new ApplicationTypeField();
                    BeanUtils.copyProperties(sss, kk);
                    List<ApplicationTypeField> ds = new ArrayList<>();

                    kk.setParentId(sss.getId());
                    kk.setLevel(a);
                    kk.setId(SysUtil.getUUID());
                    ds.add(kk);
                    sss.setApplicationTypeFields(ds);
                    list.add(kk);
                } else {

                    for (ApplicationTypeField applicationTypeField : children) {
                        applicationTypeField.setLevel(a);
                    }
                    sss.setApplicationTypeFields(children);
                    list.addAll(children);
                }
            }
        } else {
            return;
        }
        if (list == null || list.size() < 1 || (list != null && list.size() == count)) {
            allApplicationTypeField.addAll(list);
            return;
        } else {
            allApplicationTypeField.addAll(list);
            getChildren(list, allApplicationTypeField, a, count);
        }


    }


    @Override
    public void makeMaterialGroup(XSSFWorkbook wb, String sheetName, String eventName, String eventId) throws IOException {
        Sheet sheet = wb.getSheet(sheetName);
        //设置事项名称
        int rowIndex = 1;

        Row row = sheet.getRow(rowIndex);
        Cell cell = row.getCell(0);
        CellStyle cellStyle = cell.getCellStyle();
        String strEventName = cell.getStringCellValue();

        //查询事项名称
        strEventName = strEventName.replace("（此处填写办理项名称）", eventName);
        cell.setCellValue(strEventName);
        cell.setCellStyle(cellStyle);
        //标题
        //准备数据
        List<List<String>> applicationTypeFieldList=new ArrayList<>();

        List<List<ApplicationTypeField>> applicationTypeFieldList2 = applicationTypeField(eventId);
        if (applicationTypeFieldList2 != null && applicationTypeFieldList2.size() > 0) {


            for (int i = applicationTypeFieldList2.size() - 1; i >= 0; i--) {
                List<String> applicationTypeField4 = new ArrayList<>();
                for (int k = 0; k < applicationTypeFieldList2.get(i).size(); k++) {

                    applicationTypeField4.add(applicationTypeFieldList2.get(i).get(k).getFieldName());
                }
                applicationTypeFieldList.add(applicationTypeField4);

            }

        }
        //高度
        int height = 1;
        if (applicationTypeFieldList.size() > 0) {

            height = applicationTypeFieldList.size();
        }
        int width = 0;
        if (applicationTypeFieldList.size() > 0) {

            width = applicationTypeFieldList.get(0).size();
        }


        //标题第一行处理
        List<String> title = new ArrayList<>();
        title.add("序号");
        title.add("类别");
        title.add("编号");
        title.add("输入(I)/输出(O)");
        title.add("材料名称");
        List<String> title1 = new ArrayList<>();
        title1.addAll(title);

        if (applicationTypeFieldList.size() > 0) {
            for (int i = 0; i < applicationTypeFieldList.get(0).size(); i++) {
                title1.add("申请类型");
            }
        }

        title1.add("窗口提交材料要求");
        title1.add("窗口提交材料要求");
        title1.add("窗口提交材料要求");
        title1.add("审批输出物");
        title1.add("审批输出物");
        List<String> titleLast = new ArrayList<>();
        titleLast.add("数量");
        titleLast.add("是否属于可容缺的材料");
        titleLast.add("预受理");
        titleLast.add("备注说明");
        //titleLast.add("样表\n" +
            //    "（电子版本/图片）");
        title1.addAll(titleLast);

        List<String> titleMiddle = new ArrayList<>();
        titleMiddle.add("原件\n" +
                "提供");
        titleMiddle.add("复印件\n" +
                "提供");
        titleMiddle.add("电子件\n" +
                "提供");
        titleMiddle.add("原件");
        titleMiddle.add("电子件");


        List<List<String>> titleALL = new ArrayList<>();
        titleALL.add(title1);

        if (applicationTypeFieldList.size() > 0) {
            for (int k = 0; k < applicationTypeFieldList.size(); k++) {

                List<String> thisTitle = new ArrayList<>();
                thisTitle.addAll(title);
                for (int l = 0; l < applicationTypeFieldList.get(k).size(); l++) {
                    thisTitle.add(applicationTypeFieldList.get(k).get(l));
                }
                thisTitle.addAll(titleMiddle);
                thisTitle.addAll(titleLast);
                titleALL.add(thisTitle);

            }
        } else {
            List<String> thisTitle = new ArrayList<>();
            thisTitle.addAll(title);
            thisTitle.addAll(titleMiddle);
            thisTitle.addAll(titleLast);
            titleALL.add(thisTitle);
        }

        CellStyle cellStyle2 = wb.createCellStyle();
        cellStyle2.setBorderBottom(BorderStyle.THIN); // 底部边框
        cellStyle2.setBottomBorderColor(IndexedColors.BLACK.getIndex()); // 底部边框颜色
        cellStyle2.setBorderLeft(BorderStyle.THIN);  // 左边边框
        cellStyle2.setLeftBorderColor(IndexedColors.BLACK.getIndex()); // 左边边框颜色
        cellStyle2.setBorderRight(BorderStyle.THIN); // 右边边框
        cellStyle2.setRightBorderColor(IndexedColors.BLACK.getIndex());  // 右边边框颜色
        cellStyle2.setBorderTop(BorderStyle.THIN); // 上边边框
        cellStyle2.setTopBorderColor(IndexedColors.BLACK.getIndex());  // 上边边框颜色
        //设置字体
        XSSFFont font1 = wb.createFont();
        //颜色
        font1.setColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        //字体
        font1.setFontName("微软雅黑");
        //设置字体大小
        font1.setFontHeightInPoints((short) 9);
        //加粗
        font1.setBold(true);
        cellStyle2.setFont(font1);
        //水平居中对齐
        cellStyle2.setAlignment(HorizontalAlignment.CENTER);
        //垂直居中对齐
        cellStyle2.setVerticalAlignment(VerticalAlignment.CENTER);
        //自动换行
        cellStyle2.setWrapText(true);


        //开始写入Excel

        for (int i = 0; i < titleALL.size(); i++) {
            rowIndex++;
            Row thisRow = sheet.createRow(rowIndex);
            for (int k = 0; k < titleALL.get(i).size(); k++) {
                //thisRow.createCell(k).setCellStyle(cellStyle2);
                Cell cellnew = thisRow.createCell(k);
                cellnew.setCellStyle(cellStyle2);
                cellnew.setCellValue(titleALL.get(i).get(k));


            }

        }


        List<MaterialGroupAddParam> materialGroupAddParam = materialGroupingService.selectByEventId(eventId).getList();

        if (materialGroupAddParam == null && materialGroupAddParam.size() < 1) {

            return;
        }
        List<List<String>> allMaterial = new ArrayList<>();
        List<String> material;

        for (int i = 0; i < materialGroupAddParam.size(); i++) {
            material = new ArrayList<>();
            material.add(materialGroupAddParam.get(i).getOrderNum() + "");
            material.add(materialGroupAddParam.get(i).getType() + "");
            material.add(materialGroupAddParam.get(i).getNumber() + "");
            material.add(materialGroupAddParam.get(i).getIo() + "");
            material.add(materialGroupAddParam.get(i).getName() + "");
            if (applicationTypeFieldList.size() > 0) {
                for (int k = 0; k < applicationTypeFieldList.get(0).size(); k++) {
                    material.add(isRigeht(materialGroupAddParam.get(i).getId(), applicationTypeFieldList.get(applicationTypeFieldList.size() - 1).get(k)) ? "√" : "");
                }
            }

            material.add(materialGroupAddParam.get(i).getOriginalScriptProvide() ? "√" : "");
            material.add(materialGroupAddParam.get(i).getCopyProvide() ? "√" : "");
            material.add(materialGroupAddParam.get(i).getElectronicProvide() ? "√" : "");


            material.add(materialGroupAddParam.get(i).getOriginalScriptOutput() ? "√" : "");
            material.add(materialGroupAddParam.get(i).getElectronicOutput() ? "√" : "");

            material.add(materialGroupAddParam.get(i).getCount()+"");
            material.add(materialGroupAddParam.get(i).getIsItIndispensable()!=null&&materialGroupAddParam.get(i).getIsItIndispensable()?"√":""+"");
            material.add(materialGroupAddParam.get(i).getPreAcceptance() + "");
            material.add(materialGroupAddParam.get(i).getRemarks() + "");
          //  material.add(materialGroupAddParam.get(i).getSampleTable() + "");
            allMaterial.add(material);

        }


        CellStyle cellStyle3 = wb.createCellStyle();
        cellStyle3.setBorderBottom(BorderStyle.THIN); // 底部边框
        cellStyle3.setBottomBorderColor(IndexedColors.BLACK.getIndex()); // 底部边框颜色
        cellStyle3.setBorderLeft(BorderStyle.THIN);  // 左边边框
        cellStyle3.setLeftBorderColor(IndexedColors.BLACK.getIndex()); // 左边边框颜色
        cellStyle3.setBorderRight(BorderStyle.THIN); // 右边边框
        cellStyle3.setRightBorderColor(IndexedColors.BLACK.getIndex());  // 右边边框颜色
        cellStyle3.setBorderTop(BorderStyle.THIN); // 上边边框
        cellStyle3.setTopBorderColor(IndexedColors.BLACK.getIndex());  // 上边边框颜色
        //设置字体
        XSSFFont font2 = wb.createFont();
        //颜色
        font2.setColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
        //字体
        font2.setFontName("微软雅黑");
        //设置字体大小
        font2.setFontHeightInPoints((short) 9);
        //加粗
        font2.setBold(false);
        cellStyle3.setFont(font2);
        //水平居中对齐
        cellStyle3.setAlignment(HorizontalAlignment.CENTER);
        //垂直居中对齐
        cellStyle3.setVerticalAlignment(VerticalAlignment.CENTER);
        //自动换行
        cellStyle3.setWrapText(true);





/*
输出
 */

        for (int i = 0; i < allMaterial.size(); i++) {
            rowIndex++;
            Row thisRow = sheet.createRow(rowIndex);
            for (int k = 0; k < allMaterial.get(i).size(); k++) {

                Cell cellnew = thisRow.createCell(k);

                cellnew.setCellStyle(cellStyle3);
                cellnew.setCellValue(allMaterial.get(i).get(k));


            }

        }


//        FileOutputStream outputStream = new FileOutputStream("E://22222.xlsx");
//        wb.write(outputStream);
//        outputStream.flush();
//        outputStream.close();

        if(applicationTypeFieldList.size()>0){
            main(wb, sheetName, 2, 2 + height + 1, 0, allMaterial.get(0).size());
        }
        if(allMaterial.size()>0) {
            main(wb, sheetName, 2 + height, 2 + height + materialGroupAddParam.size(), 1, 2);
        }
    }


    private boolean isRigeht(String materialId, String TypeName) {


        int a = applicationTypeFieldDao.selectIsExsit(materialId, TypeName);

        if (a == 0) {

            return false;
        } else {

            return true;

        }

    }


    public void main(XSSFWorkbook wb, String SheetName, int startY, int endY, int startX, int endX) throws IOException {

//        XSSFWorkbook wb = null;
//        File file = new File("E://22222.xlsx");
//        wb = new XSSFWorkbook(new FileInputStream(file));
        Sheet sheet = wb.getSheet(SheetName);


        List<List<String>> dataList = new ArrayList<>();
        //读取数据
        List<String> data;
        for (int i = startY; i < endY; i++) {
            data = new ArrayList<>();
            Row row = sheet.getRow(i);
            for (int k = startX; k < endX; k++) {
                data.add(row.getCell(k).getStringCellValue());
            }
            dataList.add(data);
        }

        for (int i = 0; i < dataList.size(); i++) {
            for (int k = 0; k < dataList.get(i).size(); k++) {
                System.out.print("|" + dataList.get(i).get(k));
            }
            System.out.println();
        }


        //上一个单元格的值
        //当前单元格的值
        Stringexcel thisCellValue;
        Stringexcel lastCellValue = new Stringexcel();

        List<Stringexcel> Stringexcel = new ArrayList<>();
        int beginX = startX;
        int beginY = startY;

        if (endX - startX > 2) {
            for (int i = 0; i < dataList.size(); i++) {

                for (int k = 0; k < dataList.get(i).size(); k++) {

                    if (k == 0) {

                        lastCellValue.setY_start(beginY + i);
                        lastCellValue.setY_end(beginY + i);
                        lastCellValue.setX_start(beginX + k);
                        lastCellValue.setX_end(beginX + k);
                        lastCellValue.setStrData(dataList.get(i).get(k));


                    }

                    thisCellValue = new Stringexcel();

                    thisCellValue.setY_start(beginY + i);
                    thisCellValue.setY_end(beginY + i);
                    thisCellValue.setX_start(beginX + k);
                    thisCellValue.setX_end(beginX + k);
                    thisCellValue.setStrData(dataList.get(i).get(k));

                    if (!thisCellValue.getStrData().equals(lastCellValue.getStrData()) || k == dataList.get(i).size() - 1) {

                        int b;
                        if (k == dataList.get(i).size() - 1 && dataList.get(i).get(k).equals(dataList.get(i).get(k - 1))) {
                            b = thisCellValue.getX_end();
                        } else {
                            b = thisCellValue.getX_end() - 1;
                        }
                        lastCellValue.setX_end(b);

                        if (lastCellValue.getX_start() != lastCellValue.getX_end()) {
                            Stringexcel.add(lastCellValue);
                        }

                        lastCellValue = new Stringexcel();
                        lastCellValue.setY_start(beginY + i);
                        lastCellValue.setY_end(beginY + i);
                        lastCellValue.setX_start(beginX + k);
                        lastCellValue.setX_end(beginX + k);
                        lastCellValue.setStrData(dataList.get(i).get(k));

                    }

                }
            }
        }
        //纵向合并
        for (int k = 0; k < dataList.get(0).size(); k++) {
            for (int i = 0; i < dataList.size(); i++) {


                if (i == 0) {

                    lastCellValue.setY_start(beginY + i);
                    lastCellValue.setY_end(beginY + i);
                    lastCellValue.setX_start(beginX + k);
                    lastCellValue.setX_end(beginX + k);
                    lastCellValue.setStrData(dataList.get(i).get(k));


                }

                thisCellValue = new Stringexcel();

                thisCellValue.setY_start(beginY + i);
                thisCellValue.setY_end(beginY + i);
                thisCellValue.setX_start(beginX + k);
                thisCellValue.setX_end(beginX + k);

                thisCellValue.setStrData(dataList.get(i).get(k));

                if (!thisCellValue.getStrData().equals(lastCellValue.getStrData()) || i == dataList.size() - 1) {

                    int b;


                    if (dataList.size() <= 1){

                        continue;
                    }
                    if (i == dataList.size() - 1 && dataList.get(i).get(k).equals(dataList.get(i - 1).get(k))) {
                        b = thisCellValue.getY_end();
                    } else {
                        b = thisCellValue.getY_end() - 1;
                    }
                    lastCellValue.setY_end(b);

                    if (lastCellValue.getY_start() != lastCellValue.getY_end()) {
                        Stringexcel.add(lastCellValue);
                    }

                    lastCellValue = new Stringexcel();
                    lastCellValue.setY_start(beginY + i);
                    lastCellValue.setY_end(beginY + i);
                    lastCellValue.setX_start(beginX + k);
                    lastCellValue.setX_end(beginX + k);
                    lastCellValue.setStrData(dataList.get(i).get(k));

                }

            }
        }

        for (int k = 0; k < Stringexcel.size(); k++) {
            try {
                sheet.addMergedRegion(new CellRangeAddress(Stringexcel.get(k).getY_start(), Stringexcel.get(k).getY_end(), Stringexcel.get(k).getX_start(), Stringexcel.get(k).getX_end()));
            }catch(Exception e){
                e.printStackTrace();
                continue;
            }
        }

//        FileOutputStream outputStream = new FileOutputStream("E://22222.xlsx");
//        wb.write(outputStream);
//        outputStream.flush();
//        outputStream.close();

    }
}



