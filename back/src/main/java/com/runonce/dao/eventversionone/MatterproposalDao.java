package com.runonce.dao.eventversionone;

import com.runonce.httpbean.assets.reqbean.MatterproposalReq;
import com.runonce.httpbean.assets.reqbean.SelectSheetStateReq;
import org.apache.ibatis.annotations.Mapper;

import com.runonce.dao.MyMapper;
import com.runonce.model.eventversionone.Matterproposal;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 事项建议
 * <p>
 * Created by xuxueli on '2019-01-04 14:23:27'.
 */
@Mapper
public interface MatterproposalDao extends MyMapper<Matterproposal> {



    /**
     * 取一个id下最大时间
     * @param departmentalMattersId
     * @param isApproval
     * @return
     */
    List<Matterproposal> selectByIdAndMaxCreateTime(@Param("departmentalMattersId") String departmentalMattersId ,@Param("isApproval") Integer isApproval);

    /**
     * 按事项id和是否为审批查询
     * @param departmentalMattersId
     * @param isApproval
     * @return
     */
    List<Matterproposal> selectByIdAndIsApproval(@Param("departmentalMattersId") String departmentalMattersId ,@Param("isApproval") Integer isApproval);


    /**
     * 查询审批列表详情
     * @param departmentalMattersId
     * @return
     */
    List<Matterproposal> selectListInfo(@Param("departmentalMattersId") String departmentalMattersId);


    /**
     * 查询状态count
     * @param departmentalMattersId
     * @param isApproval
     * @param state
     * @return
     */
    Integer selectCountByIdAndIsApprovalAndState(@Param("departmentalMattersId") String departmentalMattersId,@Param("isApproval")  Integer isApproval,@Param("state") Integer state);


    /**
     * 通过事项id和sheet编号查找
     * @param departmentalMattersId
     * @param sheetNum
     * @return
     */
    List<Matterproposal>  selectBySheetNumAndDmId(@Param("departmentalMattersId") String departmentalMattersId,@Param("sheetNum") Integer sheetNum,@Param("isApproval") Integer isApproval);

    List<Matterproposal> findAllByCondition(MatterproposalReq matterproposalReq);

    Matterproposal selectSheetState(SelectSheetStateReq SelectSheetStateReq);

    Integer resetState(String dmId);

}
