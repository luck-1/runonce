package com.runonce.service.system;


import com.runonce.model.system.RolePermission;
import com.runonce.service.MyBaseJpaService;

import java.util.List;

/**
 * 角色权限接口
 * @author Exrick
 */
public interface RolePermissionService extends MyBaseJpaService<RolePermission,String> {

    /**
     * 通过permissionId获取
     * @param permissionId
     * @return
     */
    List<RolePermission> findByPermissionId(String permissionId);

    /**
     * 通过roleId获取
     * @param roleId
     * @return
     */
    List<RolePermission> findByRoleId(String roleId);

    /**
     * 通过roleId删除
     * @param roleId
     */
    void deleteByRoleId(String roleId);

    /**
     * 批量按role删除
     * @param idList
     */
    void deleteByRoleIdIn(List<String> idList);
}