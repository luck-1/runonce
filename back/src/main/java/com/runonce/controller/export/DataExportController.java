package com.runonce.controller.export;

import com.aliyun.oss.OSSClient;
import com.runonce.ReturnCode;
import com.runonce.model.common.SystemBean;
import com.runonce.service.export.DataExportService;
import com.runonce.util.OssClientUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * @author swq
 * @date 2018/12/20 0020
 * @description
 */
@RestController
@RequestMapping("dataExport")
public class DataExportController {


    @Autowired
    private DataExportService dataExportService;

    /**
     * 下载
     *
     * @param filename
     * @return
     */
    @GetMapping("/download/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> download(@PathVariable String filename) {

        return dataExportService.download(SystemBean.DOWNLOAD_LOCAL, filename);
    }

    /**
     * 上传
     *
     * @param file
     * @return
     */
    @PostMapping(value = "/upload")
    public ReturnCode uploadDeviceImage(@RequestBody MultipartFile file, HttpServletRequest request) {

        if (file == null) {
            return new ReturnCode.Builder().failed().msg("文件为空").build();
        }
        // 获取文件名
        String fileName = file.getOriginalFilename();

        dataExportService.upload(SystemBean.DOWNLOAD_LOCAL ,fileName,request);


        return new ReturnCode.Builder().success().msg("上传成功").object(fileName).build();
    }


    /**
     * 打印
     * @return
     */
    @GetMapping(value = "/showExcel/{eventId}")
    public ReturnCode showExcel(@PathVariable String eventId ,HttpServletRequest request) {
        OSSClient ossClient = OssClientUtil.getOssClient();
        try {
            return dataExportService.showExcel(eventId,ossClient,request);
        } catch (Exception e) {
            ossClient.shutdown();
            e.printStackTrace();
            return new ReturnCode.Builder().failed().msg("导出失败").build();
        }
    }








}
