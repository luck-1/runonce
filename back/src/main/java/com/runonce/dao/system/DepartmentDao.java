package com.runonce.dao.system;

import com.runonce.dao.MyBaseJpaDao;
import com.runonce.model.system.Department;
import org.springframework.data.domain.Sort;

import java.util.List;

/**
 * 部门数据处理层
 * @author Exrick
 */
public interface DepartmentDao extends MyBaseJpaDao<Department,String> {

    /**
     * 通过父id获取 升序
     * @param parentId
     * @return
     */
    List<Department> findByParentIdAndIsLocationOrderBySortOrder(String parentId,Boolean IsLocation);

    List<Department> findByDelFlagOrderBySortOrder(int delFlag);
    List<Department> findByIsLocationOrderBySortOrder(Boolean IsLocation);

    List<Department>  findByParentIdOrderBySortOrder(String parentId);

    /**
     * 通过父id获取 升序 数据权限
     * @param parentId
     * @param departmentIds
     * @return
     */
    List<Department> findByParentIdAndIdInOrderBySortOrder(String parentId, List<String> departmentIds);

    /**
     * 通过父id和状态获取 升序
     * @param parentId
     * @param status
     * @return
     */
    List<Department> findByParentIdAndStatusOrderBySortOrder(String parentId, Integer status);

    /**
     * 部门名模糊搜索 升序
     * @param title
     * @return
     */
    List<Department> findByTitleLikeOrderBySortOrder(String title);

    /**
     * 部门名模糊搜索 升序 数据权限
     * @param title
     * @param departmentIds
     * @return
     */
    List<Department> findByTitleLikeAndIdInOrderBySortOrder(String title, List<String> departmentIds);

    /**
     * 批量删除
     * @param idList
     */
    void  deleteByIdIn(List<String> idList);

//
//    /**
//     * 部门名模糊搜索 升序
//     * @param title
//     * @return
//     */
//    List<Department>(String title);

    /**
     * 查询
     * @param idList
     * @return
     */
    List<Department> findByIdInOrderBySortOrder(List<String> idList);


    /**
     * 排出删除按idList查询
     * @param idList
     * @return
     */
    List<Department> findByIdInAndDelFlagOrderBySortOrder(List<String> idList,int delFlag);

    /**
     * 排删除全查
     */
    List<Department> findByDelFlagAndIsLocationOrderBySortOrder(int delFlag,Boolean IsLocation);


    /**
     * 排删除按条件查询
     * @param title
     * @param depIds
     * @return
     */
    List<Department> findByTitleLikeAndIdInAndDelFlagOrderBySortOrder(String title, List<String> depIds,int delFlag);


    /**
     * 排除删除
     */

    int countByTitleAndParentIdAndDelFlag(String title,String parentId,int delFlag);


    /**
     * 排除删除
     */

    int countByTitleAndParentIdAndDelFlagAndIdNot(String title,String parentId,int delFlag,String id);


    /**
     *  查询是否有子部门
     * @param parentIdList
     * @param delFlag
     * @return
     */
    int countByParentIdInAndDelFlag(List<String> parentIdList, int delFlag);

    Department findByIdAndDelFlag(String id,int delFlag);

    List<Department> findDepartmentByAreaCode(String areaCode);

}