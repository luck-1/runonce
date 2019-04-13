package com.runonce.httpbean.assets.reqbean;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author swq
 * @date 2019/2/14 0014
 * @description
 */
@Data
public class ImportExcelReq {

    private MultipartFile file;
    private String type;

}
