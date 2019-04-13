package com.runonce.service.importdata;

import com.runonce.httpbean.assets.resbean.ImportExcelRes;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * @author swq
 * @date 2019/2/14 0014
 * @description
 */
@Component
public interface DataImportService {


    /**
     * 导入excel
     * @param file
     * @param type
     * @param version
     * @param departmentalMattersId
     * @return
     * @throws Exception
     *
     */
    ImportExcelRes importExcel(MultipartFile file, Integer type, String version, String departmentalMattersId) throws Exception;

}
