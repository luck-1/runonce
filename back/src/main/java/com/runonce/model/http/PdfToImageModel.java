package com.runonce.model.http;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table(name = "pdf_to_image_model")
public class PdfToImageModel {

    @Id
    @ApiModelProperty(value = "id")
    private String id;

    @Column(name = "materialId")
    @ApiModelProperty(value = "材料")
    private String materialId;

    @Column(name = "imageName")
    @ApiModelProperty(value = "图片名")
    private String imageName;

    @Column(name = "sourceName")
    @ApiModelProperty(value = "源图片名")
    private String sourceName;
}
