package com.runonce.service.impl.eventversionone;

import javax.annotation.Resource;

import com.runonce.ReturnCode;
import com.runonce.dao.eventversionone.MatterProposalQuestionDao;
import com.runonce.dao.eventversionone.MatterproposalDao;
import com.runonce.model.common.SystemBean;
import com.runonce.model.eventversionone.MatterProposalQuestion;
import com.runonce.model.eventversionone.Matterproposal;
import com.runonce.util.SysUtil;
import org.springframework.stereotype.Service;
import com.runonce.service.eventversionone.MatterProposalAnswerService;
import com.runonce.dao.eventversionone.MatterProposalAnswerDao;
import com.runonce.service.AbstractService;
import com.runonce.model.eventversionone.MatterProposalAnswer;

/**
 * 回复表
 * <p>
 * Created by xuxueli on '2019-01-24 12:15:13'.
 */
@Service
public class MatterProposalAnswerServiceImpl extends AbstractService<MatterProposalAnswer> implements MatterProposalAnswerService {

    @Resource
    private MatterProposalAnswerDao matterProposalAnswerDao;

    @Resource
    private MatterproposalDao matterProposalDao;

    @Resource
    private MatterProposalQuestionDao matterProposalQuestionDao;

    @Override
    public ReturnCode saveInfo(MatterProposalAnswer matterProposalAnswer) {

        //查询问题表，获取问题ID（评论）
        MatterProposalQuestion selectmatterProposalQuestion = matterProposalQuestionDao.selectByPrimaryKey(matterProposalAnswer.getQuestionId());

        //验证事项建议表状态是否通过
        Matterproposal selectmatterproposal = matterProposalDao.selectByPrimaryKey(selectmatterProposalQuestion.getMatterProposalId());
        if (!SystemBean.MATTER_PROPOSAL_AGREE.equals(selectmatterproposal.getState())) {
            /**
             * 0-待梳理员回复
             * 1-梳理员拒绝
             * 2-沟通中
             * 3-已确认
             * 除了已确认，其他都可以进行回复
             */
            if (!SystemBean.QUESTION_AGREE.equals(selectmatterProposalQuestion.getQuestionState())) {
                //如果id不为空，则修改；如果为空，则新增
                if (matterProposalAnswer.getId() != null) {
                    matterProposalAnswerDao.updateByPrimaryKey(matterProposalAnswer);
                } else {
                    matterProposalAnswer.setId(SysUtil.getUUID());
                    matterProposalAnswerDao.insertSelective(matterProposalAnswer);
                }
                return new ReturnCode.Builder().msg("添加成功").success().object(matterProposalAnswer).build();

            }
        }

        return new ReturnCode.Builder().failed().msg("当前事项条目评论已被确认，无法新增回复！").build();
    }
}
