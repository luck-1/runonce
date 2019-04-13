package com.runonce.service.eventversionone;
import com.runonce.ReturnCode;
import com.runonce.httpbean.assets.reqbean.MatterProposalQuestionReq;
import com.runonce.util.WebTokenUtil;
import org.springframework.stereotype.Component;
import com.runonce.service.Service;
import com.runonce.model.eventversionone.MatterProposalQuestion;



/**
* 问题表
*
* Created by xuxueli on '2019-01-24 12:18:08'.
*/
@Component
public interface MatterProposalQuestionService extends Service<MatterProposalQuestion> {

    ReturnCode saveInfo(MatterProposalQuestion matterProposalQuestion);

    Integer updateState(MatterProposalQuestionReq matterProposalQuestionReq);
}
