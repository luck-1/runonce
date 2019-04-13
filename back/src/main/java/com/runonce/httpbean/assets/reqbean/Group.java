package com.runonce.httpbean.assets.reqbean;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
public class Group {
    @ApiModelProperty(value = "分组id")
    private String fGroupId;

    @ApiModelProperty(value = "导入id")
    private String fImportId;

    @ApiModelProperty(value = "父id")
    private String fParentId;

    @ApiModelProperty(value = "地点id")
    private String fPlaceId;

    @ApiModelProperty(value = "分组类型")
    private String fGroupType;

    @ApiModelProperty(value = "分组名称")
    private String fGroupName;

    @ApiModelProperty(value = "区域ID")
    private String fRegionId;

    @ApiModelProperty(value = "分组描述")
    private String fGroupDescribe;

    @ApiModelProperty(value = "分组归属")
    private String fBelongTo;

    @ApiModelProperty(value = "分组图标")
    private String fIcon;

    @ApiModelProperty(value = "展示方式")
    private String fDisplayMode;

    @ApiModelProperty(value = "分组地点ID")
    private String fLocationId;

    @ApiModelProperty(value = "最后修改人")
    private String fModifier;

    @ApiModelProperty(value = "最后修改时间")
    private Date fModifyTime;

    @ApiModelProperty(value = "是否删除")
    private Boolean fDeleted;

    @ApiModelProperty(value = "是否最小")
    private Boolean fIsMin;

    @ApiModelProperty(value = "18位社会信用代码")
    private String fSocialCreditCode;

    @ApiModelProperty(value = "对应中央部门")
    private String fCentralDepartments;
}
