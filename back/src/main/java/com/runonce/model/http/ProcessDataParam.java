package com.runonce.model.http;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @Author: zhaolei
 * @Descriptions: 流程图保存参数
 * @Date: create at 2018/12/27 0027 下午 12:15
 */
@Data
public class ProcessDataParam {

    /**
     * 文件名
     */
    @NotBlank(message = "文件名不能为空")
    private String fileName;
    /**
     * xml数据字符串
     */
    @NotBlank(message = "文件xml不能为空")
    private String xmlData;
    /**
     * xml数据编码字符串
     */
    @NotBlank(message = "文件xml编码不能为空")
    private String codeedXmlString;

    private String format;

    private int width;

    private int height;
}
