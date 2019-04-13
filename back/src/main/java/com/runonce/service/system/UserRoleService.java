package com.runonce.service.system;

import com.runonce.model.system.UserRole;
import com.runonce.service.MyBaseJpaService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户角色接口
 * @author Exrickx
 */
@Service
public interface UserRoleService   extends MyBaseJpaService<UserRole,String> {

    /**
     * 通过roleId查找
     * @param roleId
     * @return
     */
    List<UserRole> findByRoleId(String roleId);


    /**
     * 删除用户角色
     * @param
     */
    void deleteByUserId(String userId);

    /**
     * 批量删除用户角色
     * @param userIdList
     */
    void deleteByUserIdIn(List<String> userIdList);

    /**
     * 按用户id获取用户角色关联的部门
     * @param userId
     * @return
     */
    List<String> findDepIdsByUserId(String userId);
}
