package com.runonce.service.eventversionone;

import com.runonce.model.eventversionone.ApplicationTypeField;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Future;

@Service
public interface ExportExcelService {

    List<List<ApplicationTypeField>> applicationTypeField(String eventId);

    void makeMaterialGroup(XSSFWorkbook wb, String sheetName, String eventName, String eventId) throws IOException;


}
