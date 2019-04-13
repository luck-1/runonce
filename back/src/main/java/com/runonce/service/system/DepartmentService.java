package com.runonce.service.system;

import com.runonce.ReturnCode;
import com.runonce.model.system.Department;
import com.runonce.service.AbstractService;
import com.runonce.service.MyBaseJpaService;
import com.runonce.service.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * 部门接口
 * @author swq
 */
public interface DepartmentService extends MyBaseJpaService<Department,String> {



    /**
     * 通过父id获取 升序
     * @param parentId
     * @param openDataFilter 是否开启数据权限
     * @return
     */
    List<Department> findByParentIdOrderBySortOrder(String parentId, Boolean openDataFilter,String userId ,Boolean IsLocation);

    /**
     * 通过父id和状态获取
     * @param parentId
     * @param status
     * @return
     */
    List<Department> findByParentIdAndStatusOrderBySortOrder(String parentId, Integer status);

    /**
     * 查询全部
     * @return
     */
    List<Department> findAll( Boolean openDataFilter,String userId,String IsLocation);


    /**
     * 批量删除
     * @param idList
     */
    void deleteByIdIn(List<String> idList);

    int countByTitleAndParentIdAndDelFlag(String title,String parentId);


    int countByTitleAndParentIdAndDelFlagAndIdNot(String title,String parentId,String id);

    /**
     * 查询是否有子部门
     * @param parentIdList
     * @return
     */
    int countByParentIdInAndDelFlag(List<String> parentIdList);


    /**
     * 初次获取部门下拉菜单
     * @param userId
     * @param openDataFilter
     * @return
     */
    List<Map> getDropList(String userId, boolean openDataFilter);

    /**
     * 获取下一级部门下拉菜单
     * @param parentId
     * @param openDataFilter
     * @return
     */
    List<Map> getDropSonList(String parentId, boolean openDataFilter);

    /**
     * 查询部门管理员
     * @param deptId
     * @return
     */
     List<String> findDeptGly( String deptId);

    /**
     * 查询超级管理员
     * @return
     */
    List<String> selectGly();

    /**
     * 查找论证员
     */
    List<String>  selectReviewGly();


    List<String> selectAllLocation();

    /**
     * 部门/地点下是否有事项
     * @param deptId
     * @return
     */
    void checkHasChildren(String deptId);


    /**
     * 发送地点下所有部门
     * @param areaCode
     * @return ReturnCode
     */
    ReturnCode sendPlace(String areaCode);
}