package com.runonce.model.http;

import com.runonce.model.eventversionone.MaterialGrouping;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Transient;
import java.util.List;

@Data
public class FormThatStateParm {

    @ApiModelProperty(value = "材料分组中申请表")
    private List<MaterialGrouping> list;

    @ApiModelProperty(value = "pdf转图片文件")
    private List<PdfToImageModel> pdfList;

    @ApiModelProperty(value = "表单说明状态")
    private Integer state;

    @Transient
    @ApiModelProperty(value = "版本")
    private Integer version;

}
