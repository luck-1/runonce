package com.runonce.service.impl.eventversionone;

import javax.annotation.Resource;

import com.runonce.ReturnCode;
import com.runonce.dao.eventversionone.MatterproposalDao;
import com.runonce.httpbean.assets.reqbean.MatterProposalQuestionReq;
import com.runonce.model.common.SystemBean;
import com.runonce.model.eventversionone.DepartmentalMatters;
import com.runonce.model.eventversionone.Matterproposal;
import com.runonce.service.eventversionone.DepartmentalMattersService;
import com.runonce.service.eventversionone.TasktodoService;
import com.runonce.service.system.DepartmentService;
import com.runonce.util.SysUtil;
import org.springframework.stereotype.Service;
import com.runonce.service.eventversionone.MatterProposalQuestionService;
import com.runonce.dao.eventversionone.MatterProposalQuestionDao;
import com.runonce.service.AbstractService;
import com.runonce.model.eventversionone.MatterProposalQuestion;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 问题表
 * <p>
 * Created by xuxueli on '2019-01-24 12:18:08'.
 */
@Service
public class MatterProposalQuestionServiceImpl extends AbstractService<MatterProposalQuestion> implements MatterProposalQuestionService {

    @Resource
    private MatterProposalQuestionDao matterProposalQuestionDao;
    @Resource
    private MatterproposalDao matterProposalDao;
    @Resource
    private TasktodoService tasktodoService;
    @Resource
    private DepartmentalMattersService departmentalMattersService;
    @Resource
    private DepartmentService departmentService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ReturnCode saveInfo(MatterProposalQuestion matterProposalQuestion) {


        List<Matterproposal> matterproposals = matterProposalDao.selectBySheetNumAndDmId(matterProposalQuestion.getDepartmentalMattersId(), matterProposalQuestion.getSheetNum(), matterProposalQuestion.getIsApproval());

        Matterproposal selectmatterproposal = null;
        if (matterproposals != null && matterproposals.size() > 0) {
            selectmatterproposal = matterproposals.get(0);
            matterProposalQuestion.setMatterProposalId(selectmatterproposal.getId());
        } else {
            return new ReturnCode.Builder().failed().msg("当前事项数据有问题，请联系管理员").build();
        }

        //验证事项建议表状态是否通过，若未通过则新增question（评论）
        if (!SystemBean.MATTER_PROPOSAL_AGREE.equals(selectmatterproposal.getState())) {
            //如果id不为空，则修改；如果为空，则新增
            if (matterProposalQuestion.getId() != null) {
                matterProposalQuestionDao.updateByPrimaryKeySelective(matterProposalQuestion);
            } else {
                matterProposalQuestion.setId(SysUtil.getUUID());
                matterProposalQuestion.setQuestionDeleteState(false);
                matterProposalQuestion.setQuestionState(SystemBean.QUESTION_WAIT);//待梳理员回复
                matterProposalQuestionDao.insertSelective(matterProposalQuestion);
            }

            //修改审核状态为拒绝
            Matterproposal matterProposal = new Matterproposal();
            matterProposal.setId(matterProposalQuestion.getMatterProposalId());
            matterProposal.setState(SystemBean.MATTER_PROPOSAL_REFUSE);//拒绝
            matterProposalDao.updateByPrimaryKeySelective(matterProposal);

            //发送todo
            //推送消息给梳理员
            MatterProposalQuestion matterProposalQuestionReturn = matterProposalQuestionDao.selectByPrimaryKey(matterProposalQuestion.getId());
            Matterproposal matterproposal= matterProposalDao.selectByPrimaryKey(matterProposalQuestionReturn.getMatterProposalId());
            String dmId=matterproposal.getDepartmentalMattersId();
            String eventName = departmentalMattersService.getEventName(dmId);
            //查询梳理员
            DepartmentalMatters departmentalMatters = departmentalMattersService.findById(dmId);
            List<String> userIdList = new ArrayList<>();
            userIdList.add(departmentalMatters.getOperator());
            String content = "";
            if (matterProposalQuestion.getIsApproval() == 1) {
                content = "事项，部门管理员添加了新评论，请及时修改";

            } else {
                content = "事项，论证员添加了新评论，请及时修改";
            }
            tasktodoService.sendTodo(dmId, eventName, 1, userIdList, content);
            return new ReturnCode.Builder().msg("提交成功").success().object(matterProposalQuestion).build();
        }
        return new ReturnCode.Builder().failed().msg("当前事项条目已通过审核，无法新增评论！").build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer updateState(MatterProposalQuestionReq matterProposalQuestionReq) {

        String quesId = matterProposalQuestionReq.getId();
        MatterProposalQuestion matterProposalQuestion = matterProposalQuestionDao.selectByPrimaryKey(quesId);

        Matterproposal matterproposal= matterProposalDao.selectByPrimaryKey(matterProposalQuestion.getMatterProposalId());
        String dmId = matterproposal.getDepartmentalMattersId();
        String eventName = departmentalMattersService.getEventName(dmId);
        DepartmentalMatters departmentalMatters = departmentalMattersService.findById(dmId);

        List<String> userIdList = new ArrayList<>();
        String content="";
        Integer type=1;
        //(0:待梳理员回复 1:梳理员拒绝 2:沟通中 3:同意  4:梳理员待确认)
        switch (matterProposalQuestionReq.getQuestionState()) {
            case 1:
                if (matterproposal.getIsApproval() == 1) {
                    type=1;
                    userIdList=departmentService.findDeptGly(departmentalMatters.getDeptId());
                } else {
                    type=3;
                    userIdList=departmentService.selectReviewGly();
                }
                content = "事项，梳理员拒绝了评论，请注意查看";
                break;
            case 2:
                if (matterproposal.getIsApproval() == 1) {
                    type=1;
                    userIdList=departmentService.findDeptGly(departmentalMatters.getDeptId());
                } else {
                    type=3;
                    userIdList=departmentService.selectReviewGly();
                }
                content = "事项，梳理员接受了评论，请注意查看";
                break;
            case 3:
                //查询梳理员
                if (matterproposal.getIsApproval() == 1) {
                    content = "事项，部门管理员通过了修改，请及时查收";

                } else {
                    content = "事项，论证员通过了修改，请及时查收";
                }
                userIdList.add(departmentalMatters.getOperator());
                break;
            case 4:
                if (matterproposal.getIsApproval() == 1) {
                    type=1;
                    userIdList=departmentService.findDeptGly(departmentalMatters.getDeptId());
                } else {
                    type=3;
                    userIdList=departmentService.selectReviewGly();
                }
                content = "事项，梳理员更改评论为待确认状态，请注意查看";
                break;
        }
        tasktodoService.sendTodo(dmId, eventName, type, userIdList, content);
        return matterProposalQuestionDao.updateState(matterProposalQuestionReq);
    }

}
