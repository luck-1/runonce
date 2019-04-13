package com.runonce.dao.system;


import com.runonce.dao.MyBaseJpaDao;
import com.runonce.model.system.RoleDepartment;

import java.util.List;

/**
 * 角色部门数据处理层
 * @author Exrick
 */
public interface RoleDepartmentDao extends MyBaseJpaDao<RoleDepartment,String> {

    /**
     * 通过roleId获取
     * @param roleId
     * @return
     */
    List<RoleDepartment> findByRoleId(String roleId);

    /**
     * 通过角色id删除
     * @param roleId
     */
    void deleteByRoleId(String roleId);

    /**
     * 通过角色id删除
     * @param departmentId
     */
    void deleteByDepartmentId(String departmentId);


    /**
     * 通过角色id批量删除
     * @param idList
     */
    void deleteByRoleIdIn(List<String> idList);
}