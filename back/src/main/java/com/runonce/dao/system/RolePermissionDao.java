package com.runonce.dao.system;

import com.runonce.dao.MyBaseJpaDao;
import com.runonce.model.system.RolePermission;

import java.util.List;

/**
 * 角色权限数据处理层
 * @author Exrick
 */
public interface RolePermissionDao extends MyBaseJpaDao<RolePermission,String> {

    /**
     * 通过permissionId获取
     * @param permissionId
     * @return
     */
    List<RolePermission> findByPermissionId(String permissionId);

    /**
     * 通过roleId获取
     * @param roleId
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