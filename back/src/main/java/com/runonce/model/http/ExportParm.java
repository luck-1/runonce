package com.runonce.model.http;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class ExportParm {
    private String processFlowChart;
    private String resultSample;
    private String addendumToProcessingTimeOrPlace;
    private String sheetName;
    private String serviceObjects;
    private List<String> removeSheetName ;
    private Map<String,Object> map;
}
