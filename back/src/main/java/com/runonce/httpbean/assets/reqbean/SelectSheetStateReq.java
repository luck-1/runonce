package com.runonce.httpbean.assets.reqbean;

import lombok.Data;

@Data
public class SelectSheetStateReq {


    private String  dmId;//事项id
    private Integer  SheetNumber;//sheet页
    private Integer isApproval;//是否部门审批


}
