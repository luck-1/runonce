package com.runonce.dao.eventversionone;

import org.apache.ibatis.annotations.Mapper;

import com.runonce.dao.MyMapper;
import com.runonce.model.eventversionone.MatterProposalAnswer;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
* 回复表
*
* Created by xuxueli on '2019-01-24 12:15:13'.
*/
@Mapper
public interface MatterProposalAnswerDao extends MyMapper<MatterProposalAnswer> {

    List<MatterProposalAnswer> selectMatterProposalAnswer(@Param("questionId") String questionId);

}
