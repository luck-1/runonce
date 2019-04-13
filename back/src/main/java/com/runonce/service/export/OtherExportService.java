package com.runonce.service.export;

import com.aliyun.oss.OSSClient;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public interface OtherExportService {

    void businessGuideExport(XSSFWorkbook workbook, String eventId,String type, OSSClient ossClient) throws Exception;

    void prequalificationExport(XSSFWorkbook workbook,String sheetName ,String eventId,OSSClient ossClient) throws Exception;

    void flowChartExport(XSSFWorkbook workbook,String sheetName ,String eventId,OSSClient ossClient)throws Exception;

}
