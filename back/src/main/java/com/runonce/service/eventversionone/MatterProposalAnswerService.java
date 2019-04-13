package com.runonce.service.eventversionone;
import com.runonce.ReturnCode;
import org.springframework.stereotype.Component;
import com.runonce.service.Service;
import com.runonce.model.eventversionone.MatterProposalAnswer;



/**
* 回复表
*
* Created by xuxueli on '2019-01-24 12:15:13'.
*/
@Component
public interface MatterProposalAnswerService extends Service<MatterProposalAnswer> {

    ReturnCode saveInfo(MatterProposalAnswer matterProposalAnswer);

}
