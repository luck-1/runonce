package com.runonce.dao.eventversionone;

import com.github.pagehelper.Page;
import com.runonce.httpbean.assets.Paging;
import com.runonce.httpbean.assets.reqbean.ImportClassesReq;
import com.runonce.httpbean.assets.resbean.DepartmentalMattersAndEventRes;
import com.runonce.model.http.*;
import org.apache.ibatis.annotations.Mapper;
import com.runonce.dao.MyMapper;
import com.runonce.model.eventversionone.DepartmentalMatters;
import org.apache.ibatis.annotations.Param;


import java.util.List;
import java.util.Map;


/**
 * 部门事项
 * <p>
 * Created by xuxueli on '2018-12-12 16:38:30'.
 */
@Mapper
public interface DepartmentalMattersDao extends MyMapper<DepartmentalMatters> {
//    /**
//     * 查詢一级事项
//     * @param departmentIds
//     * @param param
//     * @param paging
//     * @return
//     */
//    Page<DepartmentalMattersAndDepartmentRes> selectByParam(@Param("departmentIds") List<String> departmentIds, @Param("param") SelectDepartmentalMattersListParam param,@Param("paging") Paging paging);

    /**
     * 查询该部门该父级事项下的子事项
     *
     * @param deptId  部门id
     * @param eventId 父事项id
     * @return
     */
    List<DepartmentalMattersRes> selectSecondLevelEvent(@Param("deptId") String deptId, @Param("eventId") String eventId, @Param("param") SelectDepartmentalMattersListParam param);

    List<DepartmentalMattersRes> selectByDeptId(@Param("deptId") String deptId);

    DealCount findDealCount();

    EventLevel findEventLevel();

    Integer findAllEventCountByArea(@Param("deptId") String deptId);

    Integer findFinishEventCountByArea(@Param("deptId") String deptId);

    List<DepartmentalMatters> findByGroupId(@Param("groupId") String groupId);

    String findIdByEventId(@Param("eventId") String eventId);

    void updateStateById(@Param("dId") String dId, @Param("state") String state);

    void updateBusinessGuideState(@Param("eventId") String eventId, @Param("businessGuideState") int businessGuideState);

    void updateMaterialGroupingState(@Param("eventId") String eventId, @Param("materialGroupingState") int materialGroupingState);

    void updateApplicationTypeState(@Param("eventId") String eventId, @Param("applicationTypeState") int applicationTypeState);

    void updatePrequalificationState(@Param("eventId") String eventId, @Param("prequalificationState") int prequalificationState);

    void updateProcessDescriptionState(@Param("eventId") String eventId, @Param("processDescriptionState") int processDescriptionState);

    void updateProcessMapState(@Param("eventId") String eventId, @Param("processMapState") int processMapState);

    void updateFormThatStateState(@Param("eventId") String eventId, @Param("formThatState") int formThatState);

    DepartmentalMatters findState(String eventId);

    List<DepartmentalMatters> findAllSubmited();

    Map findEventNameAndType(@Param("eventId") String eventId);

    Page<DepartmentalMattersRes> selectAllByParam(@Param("param") SelectDepartmentalMattersListParam param);

    List<DepartmentalMatters> findOtherWithTheDepartment(@Param("eventId") String eventId);

    DepartmentalMatters findParent(@Param("eventId") String eventId);

    EventsTableParam findEventsTableParamByDmid(@Param("dmId") String dmId);

    String selectSonPidByParId(@Param("eventId") String eventPid);
    /**
     * 查询条数
     *
     * @param departmentIds
     * @return
     */
    long selectTreeByCount(@Param("departmentIds") List<String> departmentIds);

    /**
     * 查询列表树结构数据
     *
     * @param departmentIds
     * @param page
     * @return
     */
    List<DepartmentalMattersRes> selectTreeByList(@Param("departmentIds") List<String> departmentIds, @Param("page") Paging page);

    /**
     * 查询列表事项数据
     *
     * @param departmentIds
     * @param param
     * @return
     */

    List<DepartmentalMattersRes> selectEventIdListByCondition(@Param("departmentIds") List<String> departmentIds, @Param("param") SelectDepartmentalMattersListParam param);


    /**
     * 条件查询count
     *
     * @param departmentIds
     * @param param
     * @return
     */
    long selectTreeByConditionCount(@Param("departmentIds") List<String> departmentIds, @Param("param") SelectDepartmentalMattersListParam param);

    /**
     * 条件查询返回树
     *
     * @param departmentIds
     * @param param
     * @param page
     * @return
     */


    List<DepartmentalMattersRes> selectByConditionList(@Param("departmentIds") List<String> departmentIds, @Param("param") SelectDepartmentalMattersListParam param, @Param("page") Paging page);

    /**
     * @param eventIds
     * @param deptId
     * @return
     */
    String selectGroupIdByEventIds(@Param("eventIds") List<String> eventIds, @Param("deptId") String deptId);


    /**
     * 查询父id
     *
     * @param departmentalMattersId
     * @return
     */
    String selectPidById(@Param("departmentalMattersId") String departmentalMattersId);

    /**
     * 查询父的所有子状态
     *
     * @param departmentalMattersId
     * @return
     */
    List<Map> selectSonState(@Param("departmentalMattersId") String departmentalMattersId);


    /**
     * 更新父状态
     *
     * @param dId
     * @param dState
     */
    void updateFatherState(@Param("dId") String dId, @Param("dState") Integer dState);


    void deleteByEventId(@Param("eventId") String eventId);

    /**
     * 关联id查询
     * @param departmentalMatterId
     * @return
     */
    String selectMappingIdById(String departmentalMatterId);


    /**
     * 事项提交后梳理员可修改的状态
     * @param departmentalMattersId
     * @param isApproval
     * @return
     */
    List<Map<String,Integer>> selectRevisableByIdAndIsApproval(@Param("departmentalMattersId") String departmentalMattersId,@Param("isApproval") Integer isApproval);


    /**
     * 查询事项导出判断处理
     * @param departmentalMattersId
     * @return
     */
    DepartmentalMattersAndEventRes selectDepartmentalMattersAndEvent(@Param("departmentalMattersId")String departmentalMattersId);


    Integer  SelectDmRegion(String dmId);


    /**
     * 按情形引导子项id查询
     * @param situationChildId 情形引导子项Id
     * @return
     */
    List<DepartmentalMattersRes> selectEventIdListBySituationChildId(@Param("situationChildId")String situationChildId);

    /**
     * 推送所有目录及完成事项
     * @return List<ImportClassesReq>
     */
    List<ImportClassesReq> sendAllClasses();
}
