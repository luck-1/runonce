package com.runonce.service.eventversionone;


import com.runonce.httpbean.assets.resbean.DepartmentalMattersAndEventRes;
import com.runonce.httpbean.assets.resbean.ImportExcelRes;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import org.springframework.stereotype.Component;



@Component
public interface MaterialGroupingImportService {

     void materialGroupingImport(Workbook workbook, Sheet sheet, String  version, DepartmentalMattersAndEventRes departmentalMattersAndEventRes, ImportExcelRes importExcelRes);

}
