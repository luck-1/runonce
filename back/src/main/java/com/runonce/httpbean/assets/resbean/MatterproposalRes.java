package com.runonce.httpbean.assets.resbean;


import com.runonce.model.eventversionone.MatterProposalAnswer;
import com.runonce.model.eventversionone.MatterProposalQuestion;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class MatterproposalRes {

    private String eventName;//事项名称
    private Integer sheetNum;//sheet页编号
    private Integer state;//sheet页状态
    private String propertyName;//属性名称
    private String questionId;//评论Id
    private String questionContent;//评论内容
    private  Integer questionState;//评论状态
    private String questionerUser;//评论用
    private Date questionTime;//评论时间
    private List<MatterProposalAnswer> matterProposalAnswer;//回复列表


}
