package com.runonce.service.eventversionone;

import com.github.pagehelper.PageInfo;
import com.runonce.ReturnCode;
import com.runonce.httpbean.assets.reqbean.CopyMattersReq;
import com.runonce.model.http.DealCount;
import com.runonce.model.http.DepartmentalMattersRes;
import com.runonce.model.http.EventLevel;
import com.runonce.model.http.SelectDepartmentalMattersListParam;
import com.runonce.model.system.Role;
import org.springframework.stereotype.Component;
import com.runonce.service.Service;
import com.runonce.model.eventversionone.DepartmentalMatters;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;


/**
 * 部门事项
 * <p>
 * Created by xuxueli on '2018-12-12 16:38:30'.
 */
@Component
public interface DepartmentalMattersService extends Service<DepartmentalMatters> {

    Map selectDepartmentalMattersListByParam(SelectDepartmentalMattersListParam param, HttpServletRequest request);

    List<DepartmentalMattersRes> selectByDeptId(String deptid);

    DealCount findDealCount();

    EventLevel findEventLevel();

    List<Map<String,Object>> findEveryAreaCount();
    /**
     * 获取映射id
     *
     * @param DepartmentalMatterId
     * @return
     */
    String getMappingId(String DepartmentalMatterId);


    /**
     * 更新状态
     *
     * @param id
     * @param state
     */
    void updateState(String id, String state);


    /**
     * 更新审核状态
     *
     * @param departmentalMattersId
     * @param isApproval
     */
    void updateExamineAndApproveState(String departmentalMattersId, Integer isApproval);


    /**
     * 更新父级审核状态
     * @param departmentalMattersPid
     */
    void updateFatherExamineAndApproveState(String departmentalMattersPid);

    /**
     * 更新父级审核状态条件更新
     * @param departmentalMattersPid
     * @param examineAndApproveState
     */
    void updateFatherExamineAndApproveState(String departmentalMattersPid,Integer examineAndApproveState);
    /**
     * 查询状态
     *
     * @param eventId
     * @return
     */
    DepartmentalMatters findState(String eventId);

    /**
     *  更新父级事项状态
     * @param departmentalMattersPid
     */
    void updateFatherState(String departmentalMattersPid);
    /**
     * 父级事项状态条件更新
     * @param departmentalMattersPid
     * @param state
     */
    void updateFatherState(String departmentalMattersPid,Integer state);

    /**
     * 提交
     * @param departmentalMatters
     * @return
     */
    boolean isCommit(DepartmentalMatters departmentalMatters);

    String findFatherId(String id);
    /**
     * 事项复制
     *
     * @param copyMattersReq
     * @return
     */
    boolean copyMatters(CopyMattersReq copyMattersReq, HttpServletRequest request) throws Exception;

    Map selectAllByParam(SelectDepartmentalMattersListParam param);


    /**
     * 发起审批
     * @param departmentalMattersId
     * @param userId
     */
    void launchedExamineAndApprove(String departmentalMattersId,String userId);


    /**
     * 事项提交后梳理员可修改的状态
     * @param departmentalMattersId
     * @param role
     * @return
     */
    Map getRevisable(String departmentalMattersId,Role role);

    /**
     * 获取事项名称
     * @param dmId
     * @return
     */
    String getEventName(String dmId);

    void changeDept(List<String> ids,String newDeptId,int type);

    /**
     * 推送所有事项
     * @return ReturnCode
     */
    ReturnCode sendAllClasses();


    /**
     * 推送单个事项
     * @param departmentalMattersId
     * @return ReturnCode
     */
    ReturnCode sendOneClasses(String departmentalMattersId);
}
