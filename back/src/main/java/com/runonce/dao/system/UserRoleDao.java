package com.runonce.dao.system;

import com.runonce.dao.MyBaseJpaDao;
import com.runonce.model.system.UserRole;

import java.util.List;

/**
 * 用户角色数据处理层
 * @author Exrickx
 */
public interface UserRoleDao extends MyBaseJpaDao<UserRole,String> {

    /**
     * 通过roleId查找
     * @param roleId
     * @return
     */
    List<UserRole> findByRoleId(String roleId);

    /**
     * 删除用户角色
     * @param userId
     */
    void deleteByUserId(String userId);
    /**
     * 删除用户角色
     * @param userIdList
     */
    void deleteByUserIdIn(List<String> userIdList);

}
