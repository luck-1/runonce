package com.runonce.httpbean.assets.reqbean;

import lombok.Data;

import java.util.Date;

@Data
public class MatterproposalReq {

    private String dmId;//事项ID
    private String eventName;//事项名称
    private Integer sheetNum;//sheet页编号
    private Integer state;//sheet页状态
    private String propertyName;//属性名称
    private Integer questionState;//评论状态
    private String questionerUserName;//评论用户
    private Integer isApproval;
    private String questionId;//问题ID

    private Date startTime;//评论时间区间
    private Date endTime;

    private Integer page;//那一夜
    private Integer size;//那十页
}
