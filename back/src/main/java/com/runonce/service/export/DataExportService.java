package com.runonce.service.export;

import com.aliyun.oss.OSSClient;
import com.runonce.ReturnCode;
import com.runonce.util.OssClientUtil;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

/**
 * @author swq
 * @date 2018/12/20 0020
 * @description
 */
@Component
public interface DataExportService {


    /**
     * 模板文件位置
     * @param local
     * @return
     */
    String getMould(String local);


    ResponseEntity<Resource> download(String path,String filename);



    FileOutputStream upload(String path, String fileName,HttpServletRequest request);


    /**
     * 导出Excel
     * @param eventId
     * @param ossClient
     * @param request
     * @return
     * @throws Exception
     */
    ReturnCode showExcel(String eventId,OSSClient ossClient,HttpServletRequest request) throws Exception;


    void  createVerticalImg(Workbook workbook, Sheet finalSheet, List<String> objectNameList, OSSClient ossClient, int startRowNum, int startCellNum, Float endWidth, Float endHeight, int rowValueCount)throws Exception;
}
