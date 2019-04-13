package com.runonce.model.http;

import lombok.Data;
import java.util.List;
@Data
public class MaterialGroupVersionParam {

    List<MaterialGroupAddParam> list;

    private Integer version;
}