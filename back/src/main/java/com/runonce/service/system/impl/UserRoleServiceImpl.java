package com.runonce.service.system.impl;


import com.runonce.dao.system.UserRoleDao;
import com.runonce.dao.system.mapper.UserRoleMapper;
import com.runonce.model.system.UserRole;
import com.runonce.service.system.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户角色接口实现
 * @author Exrickx
 */
@Service
@Transactional
public class UserRoleServiceImpl implements UserRoleService {

    @Autowired
    private UserRoleDao userRoleDao;

    @Resource
    private UserRoleMapper userRoleMapper;

    @Override
    public UserRoleDao getRepository() {
        return userRoleDao;
    }

    @Override
    public List<UserRole> findByRoleId(String roleId) {
        return userRoleDao.findByRoleId(roleId);
    }

    /**
     * 删除用户角色
     * @param userId
     */
    @Override
    public void deleteByUserId(String userId) {
        userRoleDao.deleteByUserId(userId);
    }

    @Override
    public void deleteByUserIdIn(List<String> userIdList) {
        userRoleDao.deleteByUserIdIn( userIdList);
    }


    /**
     * 按用户id获取用户角色关联的部门
     * @param userId
     * @return
     */
    @Override
    public List<String> findDepIdsByUserId(String userId) {

        return userRoleMapper.findDepIdsByUserId(userId);
    }
}
