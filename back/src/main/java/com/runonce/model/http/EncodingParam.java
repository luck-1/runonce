package com.runonce.model.http;

import lombok.Data;

@Data
public class EncodingParam {
    private String deptId;
    private String catalogueItemSubtype;
    private Boolean isMin;
    private String directoryEncoding;
}
