package com.runonce.service.system.impl;


import com.runonce.dao.system.RoleDao;
import com.runonce.dao.system.mapper.RoleMapper;
import com.runonce.model.system.Role;
import com.runonce.service.system.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 角色接口实现
 *
 * @author Exrickx
 */
@Service
@Transactional
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleDao roleDao;
    @Resource
    private RoleMapper roleMapper;

    @Override
    public RoleDao getRepository() {
        return roleDao;
    }

    @Override
    public List<Role> findByDefaultRole(Boolean defaultRole) {
        return roleDao.findByDefaultRole(defaultRole);
    }

    @Override
    public void deleteByIdIn(List<String> idList) {
        roleDao.deleteByIdIn(idList);
    }

    /**
     * @param dataType
     * @return
     */
    @Override
    public List<Role> findByDataType(int dataType) {
        List<Role> roleList = roleDao.findByDataType(dataType);
        return roleList;
    }

    /**
     * 通过获取用户角色
     *
     * @param userId
     * @return
     */
    @Override
    public List<Role> findByUserRole(String userId) {
        List<Role> roleList = roleMapper.selectRoleListByUserId(userId);
        return roleList;
    }
}
