package com.runonce.httpbean.assets.reqbean;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * @author swq
 * @date 2019/4/8 0008
 * @description
 */
@Data
public class ImportClassesReq  {

    @ApiModelProperty(value = "分组id")
    private String fClassId;

    @ApiModelProperty(value = "导入id")
    private String fImportId;

    @ApiModelProperty(value = "父id")
    private String fParentId;

    @ApiModelProperty(value = "祖id")
    private String fAncestorId;

    @ApiModelProperty(value = "分类名称")
    private String fClassName;

    @ApiModelProperty(value = "分组主题")
    private String fSubjectId;

    @ApiModelProperty(value = "主体")
    private String fMainBody;

    @ApiModelProperty(value = "图标")
    private String fClassIcon;

    @ApiModelProperty(value = "分类描述")
    private String fClassDescribe;

    @ApiModelProperty(value = "是否最小级别")
    private Boolean fIsMin;

    @ApiModelProperty(value = "是否删除")
    private Boolean fDeleted;

    @ApiModelProperty(value = "部门（分组）id")
    private String fGroupId;

}
