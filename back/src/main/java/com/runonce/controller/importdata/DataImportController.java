package com.runonce.controller.importdata;

import com.runonce.ReturnCode;
import com.runonce.httpbean.assets.reqbean.ImportExcelReq;
import com.runonce.httpbean.assets.resbean.ImportExcelRes;
import com.runonce.service.importdata.DataImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;

/**
 * @author swq
 * @date 2019/2/14 0014
 * @description
 */
@RestController
@RequestMapping("dataImport")
public class DataImportController {
    @Autowired
    private DataImportService dataImportService;

    /**
     * 导入excel
     *
     * @return
     */
    @PostMapping(value = "/importExcel")
    public ReturnCode importExcel(@RequestPart MultipartFile file,
                                  @RequestParam Integer type,
                                  @RequestParam String version,
                                  @RequestParam String departmentalMattersId,
                                  HttpServletRequest request) throws Exception {


        if (file == null) {
            return new ReturnCode.Builder().failed().msg("文件为空").build();
        }
        // 获取文件名
        String fileName = file.getOriginalFilename();



        ImportExcelRes  importExcelRes =  dataImportService.importExcel(file, type,version,departmentalMattersId);
        return new ReturnCode.Builder().success().msg("导入成功,导入文件" + fileName).object(importExcelRes).build();
    }
}
