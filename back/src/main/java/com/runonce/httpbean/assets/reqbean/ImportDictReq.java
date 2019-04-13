package com.runonce.httpbean.assets.reqbean;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author swq
 * @date 2019/4/4 0004
 * @description
 */
@Data
public class ImportDictReq {

    @ApiModelProperty(value = "字典类型")
    private String fDictType;

    @ApiModelProperty(value = "字典名称")
    private String fDictName;

    @ApiModelProperty(value = "字典说明")
    private String fDictDescription;

    @ApiModelProperty(value = "字典数据")
    private List<DictData> dictData =new ArrayList<>();
    /**
     * 字典数据
     */
    @Data
    public static class DictData {
        @ApiModelProperty(value = "字典数据名称")
        private String fDataName;

        @ApiModelProperty(value = "字典数据值")
        private String fDataValue;

        @ApiModelProperty(value = "字典说明")
        private String fDataDescription;

        @ApiModelProperty(value = "字典数据排序")
        private Integer fSortOrder;

    }

}
