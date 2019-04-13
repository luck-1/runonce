package com.runonce.dao.eventversionone;

import com.runonce.httpbean.assets.reqbean.MatterProposalQuestionReq;
import org.apache.ibatis.annotations.Mapper;

import com.runonce.dao.MyMapper;
import com.runonce.model.eventversionone.MatterProposalQuestion;


/**
* 问题表
*
* Created by xuxueli on '2019-01-24 12:18:08'.
*/
@Mapper
public interface MatterProposalQuestionDao extends MyMapper<MatterProposalQuestion> {


    Integer updateState(MatterProposalQuestionReq matterProposalQuestionReq);
}
