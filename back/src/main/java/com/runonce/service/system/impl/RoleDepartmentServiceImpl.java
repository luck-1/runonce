package com.runonce.service.system.impl;


import com.runonce.dao.system.RoleDepartmentDao;
import com.runonce.model.system.RoleDepartment;
import com.runonce.service.system.RoleDepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * 角色部门接口实现
 * @author Exrick
 */
@Service
@Transactional
public class RoleDepartmentServiceImpl implements RoleDepartmentService {

    @Autowired
    private RoleDepartmentDao roleDepartmentDao;

    @Override
    public RoleDepartmentDao getRepository() {
        return roleDepartmentDao;
    }

    @Override
    public List<RoleDepartment> findByRoleId(String roleId) {

        return roleDepartmentDao.findByRoleId(roleId);
    }

    @Override
    public void deleteByRoleId(String roleId) {

        roleDepartmentDao.deleteByRoleId(roleId);
    }

    @Override
    public void deleteByDepartmentId(String departmentId) {

        roleDepartmentDao.deleteByDepartmentId(departmentId);
    }

    /**
     * 按角色id批量删除
     *
     * @param idList
     */
    @Override
    public void deleteByRoleIdIn(List<String> idList) {
        roleDepartmentDao.deleteByRoleIdIn(idList);
    }
}