package com.runonce.service.impl.export;

import com.aliyun.oss.OSSClient;
import com.runonce.dao.base.CatalogueEventTypeDao;
import com.runonce.dao.base.ServiceObjectDao;
import com.runonce.dao.eventversionone.*;
import com.runonce.httpbean.assets.reqbean.BusinessGuideAdministrativeLicensingOneReq;
import com.runonce.httpbean.assets.reqbean.BusinessGuideAuditAndForwardingCategoryOneReq;
import com.runonce.httpbean.assets.reqbean.BusinessGuideFilingOneReq;
import com.runonce.httpbean.assets.reqbean.BusinessGuideOtherServicesOneReq;
import com.runonce.model.eventversionone.*;
import com.runonce.model.http.ExportParm;
import com.runonce.service.eventversionone.ProcessDescriptionPicService;
import com.runonce.service.export.OtherExportService;
import com.runonce.util.EXUtil;
import com.runonce.util.ExportUtil;
import com.runonce.util.OssClientUtil;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.util.*;

@Component
public class OtherExport implements OtherExportService {

    @Resource
    private BusinessGuideAdministrativeLicensingOneDao businessGuideAdministrativeLicensingOneDao ;

    @Resource
    private BusinessGuideAuditAndForwardingCategoryOneDao businessGuideAuditAndForwardingCategoryOneDao;

    @Resource
    private BusinessGuideFilingOneDao businessGuideFilingOneDao;

    @Resource
    private BusinessGuideOtherServicesOneDao businessGuideOtherServicesOneDao;

    @Resource
    private PrequalificationDao prequalificationDao;

    @Resource
    private ProcessDescriptionPicDao processDescriptionPicDao;

    @Resource
    private ProcessDescriptionPicService processDescriptionPicService;

    /**
     *  空填充字段
     */
    private static final String NULL_FILL = "无";

    /**
     * 办事指南
     * @param workbook
     * @param eventId
     * @param ossClient
     * @throws Exception
     */
    @Override
    public void businessGuideExport(XSSFWorkbook workbook, String eventId, String type, OSSClient ossClient) throws Exception {

        String column1,column2;

        List<String> objectNames;


        Map<String, Object> map0 = getData(workbook,eventId,type);
        if( ! map0.containsKey("sheetName")){
            return;
        }
        Object obj = map0.get("sheetName");
        if(obj == null || "".equals(obj)){
            return;
        }

        XSSFSheet sheet1 = workbook.getSheet(obj.toString());
        if(sheet1 == null ){
            return;
        }
        XSSFCell cell;
        XSSFRow row;
        XSSFCellStyle style1,style2;

        XSSFFont font1 = EXUtil.getFont(workbook);
        font1.setBold(true);//加粗
        //第一列单元格格式
        style1 = EXUtil.getStyle(workbook,BorderStyle.THIN,font1);

        //第二列单元格格式
        XSSFFont font2 = EXUtil.getFont(workbook);
        style2 = EXUtil.getStyle(workbook,BorderStyle.THIN,font2);
        //水平左对齐
        style2.setAlignment(HorizontalAlignment.LEFT);
        //垂直上对齐
        style2.setVerticalAlignment(VerticalAlignment.TOP);

        //行
        for(int i = 0;i<map0.size()+20;i++){
            row = sheet1.getRow(i+2);
            if (row == null ){
                return;
            }
            cell = row.getCell(1);
            cell.setCellStyle(style1);
            //第一列值
            column1 = cell.getStringCellValue().trim();
            if(map0.containsKey(column1)){
                cell = row.getCell(2);
                cell.setCellStyle(style2);
                obj = map0.get(column1);
                if(obj == null || "".equals(obj)){
                    cell.setCellValue(NULL_FILL);
                    continue;
                }
                column2 = obj.toString();
                if (column2 == null || "".equals(column2)){
                    cell.setCellValue(NULL_FILL);
                    continue;
                }
                if("办理时间或地点附件".equals(column1)){
                    //设置值行高
                    EXUtil.setRowHeight(row,EXUtil.DEFAULT_ROW_HEIGHT_PIX * 3);
                    cell.setCellValue("");
                    //获得图片名
                    objectNames = OssClientUtil.getFileName(column2);
                    //图片为空
                    if(objectNames == null || objectNames.size() ==0){
                        continue;
                    }
                    String showName;
                    for(int i1=0;i1<objectNames.size();i1++){
                        //得到文件数组
                        byte [] bytes = OssClientUtil.getObjectForByte(ossClient,objectNames.get(i1));
                        //文件不存在
                        if(bytes == null || bytes.length ==0){continue;}
                        //嵌入文件
                        showName = (i1+1) + objectNames.get(i1).substring(objectNames.get(i1).lastIndexOf("."));
                        EXUtil.embedObjectToCell(sheet1,bytes,showName,2,i+2,i1,0);
                    }
                }else if("结果样本".equals(column1)){
                    int showWidght = 200;
                    cell.setCellValue("");
                    //获得图片名
                    objectNames = OssClientUtil.getFileName(column2);
                    //图片为空
                    if(objectNames.size() == 0 || objectNames == null){
                        cell.setCellValue(NULL_FILL);
                        continue;
                    }
                    //插入图片字节数组
                    List<byte[]> list1 = new ArrayList<>();
                    //插入每张图片高度
                    List<Integer> list2 = new ArrayList<>();
                    //嵌入每个对象名字
                    List<String> embedNames = new ArrayList<>();
                    //插入每张图片名字
                    List<String> imageNames = new ArrayList<>();
                    String fileName ;
                    int height = 0,height1 = 0,embedHeight = 0;
                    for(int i1=0;i1<objectNames.size();i1++){
                        //是否为图片
                        if(EXUtil.isPicture(objectNames.get(i1))){

                            byte[] bytes = OssClientUtil.getObjectForByte(ossClient,objectNames.get(i1));
                            if(bytes.length == 0 || bytes == null){continue;}

                            height1 = EXUtil.getHight(bytes,showWidght);
                            height += height1;

                            list1.add(bytes);
                            list2.add(height1);
                            imageNames.add(objectNames.get(i1));

                        }else{  //嵌入文件格式，单元格内嵌入文件
                            embedNames.add(objectNames.get(i1));
                        }
                    }
                    //嵌入文件的高度
                    if(embedNames.size() == 0 || embedNames == null){
                        embedHeight = 0;
                    }else{
                        embedHeight = EXUtil.DEFAULT_ROW_HEIGHT_PIX * 3 + 10;
                    }
                    //高度像素 = 图片高度 + 嵌入文件高度 + 两个图片间空格高度
                    EXUtil.setRowHeight(row,height+embedHeight+imageNames.size()*5);
                    height = 0;
                    //逐个嵌入附件
                    for(int i1=0;i1<embedNames.size();i1++){
                        byte[] bytes = OssClientUtil.getObjectForByte(ossClient,embedNames.get(i1));
                        if(bytes == null || bytes.length == 0){
                            continue;
                        }
                        fileName = (i1+1)+"."+embedNames.get(i1).substring(embedNames.get(i1).lastIndexOf(".")+1).toLowerCase();
                        EXUtil.embedObjectToCell(sheet1,bytes,fileName,2,i+2,i1,0);
                    }
                    //逐个插入图片
                    for(int i1=0;i1<imageNames.size();i1++){
                        height1 = list2.get(i1);
                        XSSFClientAnchor anchor = new XSSFClientAnchor(EXUtil.setEmbedDx(1),0,0,0,2,i+2,3,i+3);
                        //Dy偏移像素 = (嵌入文件高度 + 两个图片间空格高度) + 上一个图片高度
                        EXUtil.setDy(anchor,(embedHeight + (i1+1)*5) + height);
                        EXUtil.addPictureToCell(sheet1,anchor,list1.get(i1),0,showWidght);
                        height += height1;
                    }
                }else{
                    //普通字段
                    cell.setCellValue(column2);
                }
            }
        }
    }

    /**
     * 资格预审
     * @param workbook
     * @param sheetName
     * @param eventId
     * @param ossClient
     * @throws Exception
     */
    @Override
    public void prequalificationExport(XSSFWorkbook workbook,String sheetName ,String eventId,OSSClient ossClient) throws Exception {

        Object obj;

        List<Prequalification> list = prequalificationDao.findByEventId(eventId);
        if (list.isEmpty()) {return;}
        Prequalification prequalification;

        XSSFSheet sheet1 = workbook.getSheet(sheetName);
        XSSFCell cell;
        XSSFRow row;
        XSSFCellStyle style,style0;
        String text,header,value;
        short hight = 1000;

        //无数据样式
        style0 = workbook.createCellStyle();
        style0.setBorderTop(BorderStyle.THIN);

        //单元格格式
        XSSFFont font = EXUtil.getFont(workbook);
        style = EXUtil.getStyle(workbook,BorderStyle.THIN,font);

        //表header
        List<String> headers = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            row = sheet1.getRow(2);
            cell = row.getCell(i);
            text = cell.getStringCellValue();
            headers.add(text);
        }
        //对应写入数据
        for(int j=0;j<list.size();j++){
            //字段和字段对应值
            prequalification = list.get(j);
            Map<String, Object> map0 = ExportUtil.getApiKeyAndValue(prequalification);
            row = sheet1.createRow(j + 3);
            row.setHeight(hight);
            //列
            for(int i=0;i<headers.size();i++){
                //表头值
                header = headers.get(i).replaceAll("\\s*", "");
                //map存在对应header
                if (map0.containsKey(header)) {
                    cell =row.createCell(i);
                    cell.setCellStyle(style);
                    //现有审核操作（截图或说明）
                    if(i == 4){
                        //判断第三列是否为平台审核
                        cell = row.getCell(i-1);
                        text = cell.getStringCellValue().trim();
                        if("平台查询".equals(text)){
                            text = prequalification.getAuditOperationPic();
                            //获得图片名
                            List<String> objectNames = OssClientUtil.getFileName(text);
                            //图片为空
                            if(objectNames == null || objectNames.size() ==0){
                                continue;
                            }

                            for(int i1=0;i1<objectNames.size();i1++){
                                //得到文件数组
                                byte [] bytes = OssClientUtil.getObjectForByte(ossClient,objectNames.get(i1));
                                //文件不存在
                                if(bytes == null || bytes.length ==0){continue;}
                                //嵌入文件
                                String showName = (i1+1)+objectNames.get(i1).substring(objectNames.get(i1).lastIndexOf("."));
                                EXUtil.embedObjectToCell(sheet1,bytes,showName,i,j+3,i1,0);
                            }
                            continue;
                        }else{
                            cell = row.getCell(i);
                        }
                    }
                    //获得header对应值
                    obj = map0.get(header);
                    if(obj == null || "".equals(obj)){
                        cell.setCellValue(NULL_FILL);
                        cell.setCellStyle(style);
                        continue;
                    }
                    value = obj.toString();
                    //向单元格写值
                    cell.setCellValue(value);
                    //设置单元格格式
                    cell.setCellStyle(style);
                }else{
                    cell = row.createCell(i);
                    cell.setCellValue(NULL_FILL);
                    cell.setCellStyle(style);
                }
            }
        }
    }

    /**
     * 办理流程图
     * @param workbook
     * @param sheetName
     * @param eventId
     * @param ossClient
     * @throws Exception
     */
    @Override
    public void flowChartExport(XSSFWorkbook workbook,String sheetName ,String eventId,OSSClient ossClient)throws Exception{
        //行宽
        final int maxWidth = 9;
        final int maxHeight = 70;

        List<ProcessDescriptionPic> processDescriptionPicList = processDescriptionPicService.findByEventId(eventId).getProcessDescriptionPicList();
//
        for (int i = 0;i < processDescriptionPicList.size();i++) {

            ProcessDescriptionPic processDescriptionPic = processDescriptionPicList.get(i);

            if(processDescriptionPic == null ){
                return;
            }
            String image = processDescriptionPic.getPicbase();
            if(image == null || "".equals(image)){
                return;
            }
            //OSS获取文件
            byte[] bytes = OssClientUtil.getObjectForByte(ossClient,image);
            if(bytes == null || bytes.length == 0){
                return;
            }
            XSSFSheet sheet = workbook.getSheet(sheetName);

            List<String> conditionList = processDescriptionPic.getConditionList();

            String title = (conditionList == null || conditionList.size() == 0) ? "" : String.join(",",conditionList);

            int startCell = (i % 2 == 0) ? 0 : (maxWidth + 1);
            int startRow = i * (maxHeight + 3) + 2;

            EXUtil.sheetAddPic(sheet,bytes,title,maxWidth,maxHeight,startCell,startRow);
        }
    }

    public Map<String,Object> getData(XSSFWorkbook workbook, String eventId, String type){

        String sheetName = "";
        List<String> removeSheetName;
        Map<String,Object> map = new HashMap<>();

        switch(type){
            case "1":
                sheetName = "办事指南(行政许可和备案类)";
                removeSheetName = Arrays.asList(new String[]{"办事指南(审核转报类)","办事指南(其他服务类)"});

                BusinessGuideAdministrativeLicensingOneReq businessGuideAdministrativeLicensingOneReq =
                        businessGuideAdministrativeLicensingOneDao.showInfo(eventId);

                if(businessGuideAdministrativeLicensingOneReq == null){return null;}

                BusinessGuideAdministrativeLicensingOne businessGuideAdministrativeLicensingOne =
                        businessGuideAdministrativeLicensingOneReq.getBusinessGuideAdministrativeLicensingOne();
                BusinessGuideAdministrativeLicensingOnePrivate businessGuideAdministrativeLicensingOnePrivate =
                        businessGuideAdministrativeLicensingOneReq.getBusinessGuideAdministrativeLicensingOnePrivate();

                if(businessGuideAdministrativeLicensingOne == null){return null;}
                if(businessGuideAdministrativeLicensingOnePrivate == null){return null;}
                map = ExportUtil.getApiAllKV(businessGuideAdministrativeLicensingOne,businessGuideAdministrativeLicensingOnePrivate);
                break;
            case "2":
                sheetName = "办事指南(审核转报类)";
                removeSheetName = Arrays.asList(new String[]{"办事指南(行政许可和备案类)","办事指南(其他服务类)"});

                BusinessGuideAuditAndForwardingCategoryOneReq businessGuideAuditAndForwardingCategoryOneReq =
                        businessGuideAuditAndForwardingCategoryOneDao.showInfo(eventId);

                if(businessGuideAuditAndForwardingCategoryOneReq == null){return null;}

                BusinessGuideAuditAndForwardingCategoryOne businessGuideAuditAndForwardingCategoryOne =
                        businessGuideAuditAndForwardingCategoryOneReq.getBusinessGuideAuditAndForwardingCategoryOne();
                BusinessGuideAuditAndForwardingCategoryOnePrivate businessGuideAuditAndForwardingCategoryOnePrivate  =
                        businessGuideAuditAndForwardingCategoryOneReq.getBusinessGuideAuditAndForwardingCategoryOnePrivate();

                if(businessGuideAuditAndForwardingCategoryOne == null){return null;}
                if(businessGuideAuditAndForwardingCategoryOnePrivate == null){return null;}

                map = ExportUtil.getApiAllKV(businessGuideAuditAndForwardingCategoryOne,businessGuideAuditAndForwardingCategoryOnePrivate);
                break;
            case "3":
                sheetName = "办事指南(行政许可和备案类)";
                removeSheetName = Arrays.asList(new String[]{"办事指南(审核转报类)","办事指南(其他服务类)"});

                BusinessGuideFilingOneReq businessGuideFilingOneReq =
                        businessGuideFilingOneDao.showInfo(eventId);

                if(businessGuideFilingOneReq == null){return null;}

                BusinessGuideFilingOne businessGuideFilingOne =
                        businessGuideFilingOneReq.getBusinessGuideFilingOne();
                BusinessGuideFilingOnePrivate businessGuideFilingOnePrivate =
                        businessGuideFilingOneReq.getBusinessGuideFilingOnePrivate();

                if(businessGuideFilingOne == null){return null;}
                if(businessGuideFilingOnePrivate == null){return null;}

                map = ExportUtil.getApiAllKV(businessGuideFilingOne,businessGuideFilingOnePrivate);
                break;
            case "4":
                sheetName = "办事指南(其他服务类)";
                removeSheetName = Arrays.asList(new String[]{"办事指南(行政许可和备案类)","办事指南(审核转报类)"});

                BusinessGuideOtherServicesOneReq businessGuideOtherServicesOneReq =
                        businessGuideOtherServicesOneDao.showInfo(eventId);

                if(businessGuideOtherServicesOneReq == null){return null;}

                BusinessGuideOtherServicesOne businessGuideOtherServicesOne =
                        businessGuideOtherServicesOneReq.getBusinessGuideOtherServicesOne();
                BusinessGuideOtherServicesOnePrivate businessGuideOtherServicesOnePrivate  =
                        businessGuideOtherServicesOneReq.getBusinessGuideOtherServicesOnePrivate();

                if(businessGuideOtherServicesOne == null){return null;}
                if(businessGuideOtherServicesOnePrivate == null){return null;}

                map = ExportUtil.getApiAllKV(businessGuideOtherServicesOne,businessGuideOtherServicesOnePrivate);
                break;
            default:
                removeSheetName = Arrays.asList(new String[]{"办事指南(行政许可和备案类)","办事指南(审核转报类)","办事指南(其他服务类)"});
        }
        EXUtil.removeSheet(workbook,removeSheetName);
        map.put("sheetName",sheetName);
        return map;
    }
}