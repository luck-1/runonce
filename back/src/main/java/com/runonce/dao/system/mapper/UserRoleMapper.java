package com.runonce.dao.system.mapper;


import com.runonce.dao.MyMapper;
import com.runonce.model.system.Role;
import com.runonce.model.system.UserRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Exrickx
 */
@Mapper
public interface UserRoleMapper extends MyMapper<UserRole> {
    /**
     * 通过用户id获取用户角色关联的部门数据
     * @param userId
     * @return
     */
    List<String> findDepIdsByUserId(@Param("userId") String userId);
}
