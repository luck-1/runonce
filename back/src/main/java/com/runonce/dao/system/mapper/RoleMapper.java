package com.runonce.dao.system.mapper;


import com.runonce.dao.MyBaseJpaDao;
import com.runonce.dao.MyMapper;
import com.runonce.model.system.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色数据处理层
 * @author Exrickx
 */
@Mapper
public interface RoleMapper extends MyMapper<Role> {

    /**
     * 按人员id查询角色
     * @param userId
     * @return
     */
    List<Role> selectRoleListByUserId(@Param("userId") String userId);
}
