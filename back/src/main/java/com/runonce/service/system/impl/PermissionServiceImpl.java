package com.runonce.service.system.impl;


import com.runonce.dao.system.PermissionDao;
import com.runonce.model.system.Permission;
import com.runonce.service.system.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 权限接口实现
 *
 * @author Exrick
 */
@Service
@Transactional
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionDao permissionDao;

    @Override
    public PermissionDao getRepository() {
        return permissionDao;
    }

    @Override
    public List<Permission> findByLevelOrderBySortOrder(Integer level) {

        return permissionDao.findByLevelOrderBySortOrder(level);
    }

    @Override
    public List<Permission> findByParentIdOrderBySortOrder(String parentId) {

        return permissionDao.findByParentIdOrderBySortOrder(parentId);
    }

    @Override
    public List<Permission> findByTypeAndStatusOrderBySortOrder(Integer type, Integer status) {

        return permissionDao.findByTypeAndStatusOrderBySortOrder(type, status);
    }

    @Override
    public List<Permission> findByTitle(String title) {

        return permissionDao.findByTitle(title);
    }

    @Override
    public List<Permission> findByTitleLikeOrderBySortOrder(String title) {

        return permissionDao.findByTitleLikeOrderBySortOrder(title);
    }

    /**
     * 批量
     *
     * @param idList
     */
    @Override
    public void deleteByIdIn(List<String> idList) {
        permissionDao.deleteByIdIn(idList);
    }

    /**
     *
     * @param title
     * @param id
     * @return
     */
    @Override
    public Permission findByTitleAndNotId(String title, String id) {

        Permission permission = permissionDao.findByTitleAndIdNot(title, id);

        return permission;
    }
}