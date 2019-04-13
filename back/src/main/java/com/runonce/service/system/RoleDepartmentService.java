package com.runonce.service.system;


import com.runonce.model.system.RoleDepartment;
import com.runonce.service.MyBaseJpaService;


import java.util.List;

/**
 * 角色部门接口
 * @author Exrick
 */
public interface RoleDepartmentService extends MyBaseJpaService<RoleDepartment,String> {

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
     * 按角色id批量删除
     * @param idList
     */
    void deleteByRoleIdIn(List<String> idList);
}