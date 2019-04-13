package com.runonce.service.system.impl;


import com.runonce.dao.system.RolePermissionDao;
import com.runonce.model.system.RolePermission;
import com.runonce.service.system.RolePermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 角色权限接口实现
 * @author Exrick
 */
@Service
@Transactional
public class RolePermissionServiceImpl implements RolePermissionService {

    @Autowired
    private RolePermissionDao rolePermissionDao;

    @Override
    public RolePermissionDao getRepository() {
        return rolePermissionDao;
    }

    @Override
    public List<RolePermission> findByPermissionId(String permissionId) {

        return rolePermissionDao.findByPermissionId(permissionId);
    }

    @Override
    public List<RolePermission> findByRoleId(String roleId) {

        return rolePermissionDao.findByRoleId(roleId);
    }

    @Override
    public void deleteByRoleId(String roleId) {

        rolePermissionDao.deleteByRoleId(roleId);
    }

    /**
     * 批量按role删除
     * @param idList
     */
    @Override
    public void deleteByRoleIdIn(List<String> idList) {
        rolePermissionDao.deleteByRoleIdIn(idList);
    }
}