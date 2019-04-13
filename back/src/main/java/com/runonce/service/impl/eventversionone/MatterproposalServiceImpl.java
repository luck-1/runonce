package com.runonce.service.impl.eventversionone;

import javax.annotation.Resource;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.runonce.ReturnCode;
import com.runonce.httpbean.assets.reqbean.MatterproposalReq;
import com.runonce.httpbean.assets.reqbean.SelectSheetStateReq;
import com.runonce.httpbean.assets.resbean.MatterproposalRes;
import com.runonce.model.common.SystemBean;
import com.runonce.model.eventversionone.DepartmentalMatters;
import com.runonce.service.eventversionone.DepartmentalMattersService;
import com.runonce.service.eventversionone.MatterProposalQuestionService;
import com.runonce.service.eventversionone.TasktodoService;
import com.runonce.service.system.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.runonce.service.eventversionone.MatterproposalService;
import com.runonce.dao.eventversionone.MatterproposalDao;
import com.runonce.service.AbstractService;
import com.runonce.model.eventversionone.Matterproposal;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author swq
 * 事项建议
 * <p>
 * Created by xuxueli on '2019-01-04 14:23:27'.
 */
@Service
public class MatterproposalServiceImpl extends AbstractService<Matterproposal> implements MatterproposalService {

    @Resource
    private MatterproposalDao matterproposalDao;

    @Autowired
    private DepartmentalMattersService departmentalMattersService;

    @Autowired
    private MatterProposalQuestionService matterProposalQuestionService;

    @Resource
    private TasktodoService tasktodoService;

    @Resource
    private DepartmentService departmentService;

    @Override
    public List<Matterproposal> findByProperty(Matterproposal matterproposal) {


        return matterproposalDao.selectByIdAndIsApproval(matterproposal.getDepartmentalMattersId(), SystemBean.EXAMINE_APPROVAL);
    }

    @Override
    public List<Matterproposal> findbypropertyLeader(Matterproposal matterproposal) {

        return matterproposalDao.selectByIdAndIsApproval(matterproposal.getDepartmentalMattersId(), SystemBean.DEPT_APPROVAL);
    }


//    /**
//     * 审批审核
//     *
//     * @param matterproposal
//     * @param userId
//     * @return
//     */
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public ReturnCode add(Matterproposal matterproposal, String userId) {
//
//        matterproposal.setUserId(userId);
//        //审批意见
//        matterproposal.setIsApproval(2);
//        matterproposal.setId(SysUtil.getUUID());
//        matterproposalDao.insertSelective(matterproposal);
//        matterProposalQuestionService.add(matterproposal.getMatterProposalQuestion());
//        departmentalMattersService.updateExamineAndApproveState(matterproposal.getDepartmentalMattersId(), matterproposal.getIsApproval());
//        return new ReturnCode.Builder().success().msg("添加成功").build();
//    }

//    /**
//     * 部门审核
//     *
//     * @param matterproposal
//     * @param userId
//     * @return
//     */
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public ReturnCode addLeaderComment(Matterproposal matterproposal, String userId) {
//        matterproposal.setUserId(userId);
//        //部门审批
//        matterproposal.setIsApproval(1);
//        matterproposal.setId(SysUtil.getUUID());
//        matterproposalDao.insertSelective(matterproposal);
//        //
//        matterProposalQuestionService.add(matterproposal.getMatterProposalQuestion());
//        departmentalMattersService.updateExamineAndApproveState(matterproposal.getDepartmentalMattersId(), matterproposal.getIsApproval());
//        return new ReturnCode.Builder().success().msg("添加成功").build();
//    }

    /**
     * 通过审批
     *
     * @param matterProposalId
     * @param userId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ReturnCode savePass(String matterProposalId, String userId) {
        Matterproposal selectMatterProposal = matterproposalDao.selectByPrimaryKey(matterProposalId);

        if (selectMatterProposal == null) {
            return new ReturnCode.Builder().failed().msg("未找到审核sheet页id").build();
        }
        selectMatterProposal.setState(SystemBean.MATTER_PROPOSAL_AGREE);
        selectMatterProposal.setUserId(userId);
        matterproposalDao.updateByPrimaryKeySelective(selectMatterProposal);

        //通知梳理员sheet页审核状态
        String  dmId=selectMatterProposal.getDepartmentalMattersId();
        String  eventName=departmentalMattersService.getEventName(dmId);
        DepartmentalMatters departmentalMatters=departmentalMattersService.findById(dmId);
        List<String> userIdList=new ArrayList<>();
        userIdList.add(departmentalMatters.getOperator());
        String content="";
        if(selectMatterProposal.getIsApproval()==1){
            content="事项，部门管理员审核sheet页通过，请注意查看";
        }else {
            content="事项，论证员审核sheet页通过，请注意查看";
        }
        tasktodoService.sendTodo(dmId,eventName,1,userIdList,content);

        //更新审核状态
        departmentalMattersService.updateExamineAndApproveState(selectMatterProposal.getDepartmentalMattersId(), selectMatterProposal.getIsApproval());
        return new ReturnCode.Builder().success().msg("提交成功").build();
    }

    /**
     * 查询审批列表详情
     * @param departmentalMattersId
     * @return
     */
    @Override
    public List<Matterproposal> selectListInfo(String departmentalMattersId) {

        List<Matterproposal> matterProposalList = matterproposalDao.selectListInfo(departmentalMattersId);
        return matterProposalList;
    }



    @Override
    public PageInfo<MatterproposalRes> findAllByCondition(MatterproposalReq matterproposalReq) {

        PageHelper.startPage(matterproposalReq.getPage(),matterproposalReq.getSize());
        List<Matterproposal> matterProposalList = matterproposalDao.findAllByCondition(matterproposalReq);
        PageInfo pageInfo = new PageInfo(matterProposalList);
        return pageInfo;

    }


    @Override
    public Matterproposal selectSheetState(SelectSheetStateReq SelectSheetStateReq) {
        Matterproposal matterProposal = matterproposalDao.selectSheetState(SelectSheetStateReq);
        return matterProposal;
    }

}
