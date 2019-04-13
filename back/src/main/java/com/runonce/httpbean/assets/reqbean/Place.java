package com.runonce.httpbean.assets.reqbean;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.persistence.Column;
import javax.persistence.Id;

@Data
public class Place {
    @ApiModelProperty(value = "地点id")
    private String fPlaceId;

    @ApiModelProperty(value = "导入的数据id")
    private String fImportId;

    @ApiModelProperty(value = "父id")
    private String fParentId;

    @ApiModelProperty(value = "排序")
    private Integer fSortOrder;

    @ApiModelProperty(value = "地点状态")
    private Integer fPlaceStatus;

    @ApiModelProperty(value = "地点名称")
    private String fPlaceName;

    @ApiModelProperty(value = "区分（省、市、县）")
    private String fRegion;

}
