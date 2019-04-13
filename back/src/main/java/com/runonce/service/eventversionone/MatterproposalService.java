package com.runonce.service.eventversionone;
import com.github.pagehelper.PageInfo;
import com.runonce.ReturnCode;
import com.runonce.httpbean.assets.reqbean.MatterproposalReq;
import com.runonce.httpbean.assets.reqbean.SelectSheetStateReq;
import com.runonce.httpbean.assets.resbean.MatterproposalRes;
import org.springframework.stereotype.Component;
import com.runonce.service.Service;
import  com.runonce.model.eventversionone.Matterproposal;

import java.util.List;


/**
* 事项建议
*
* Created by xuxueli on '2019-01-04 14:23:27'.
*/
@Component
public interface MatterproposalService extends Service<Matterproposal> {


    List<Matterproposal> findByProperty(Matterproposal matterproposal);

    List<Matterproposal> findbypropertyLeader(Matterproposal matterproposal);

//
//    ReturnCode add(Matterproposal  matterproposal,String userId);
//
//    ReturnCode addLeaderComment(Matterproposal  matterproposal,String userId);

    /**
     * 通过审批
     * @param matterProposalId
     * @param userId
     * @return
     */
    ReturnCode savePass(String matterProposalId, String userId);


    /**
     * 查询审批列表详情
     * @param departmentalMattersId
     * @return
     */
    List<Matterproposal> selectListInfo(String departmentalMattersId);

    PageInfo<MatterproposalRes> findAllByCondition(MatterproposalReq matterproposalReq);

    Matterproposal selectSheetState(SelectSheetStateReq SelectSheetStateReq);
}
