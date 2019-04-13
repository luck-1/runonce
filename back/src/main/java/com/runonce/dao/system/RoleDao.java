package com.runonce.dao.system;


import com.runonce.dao.MyBaseJpaDao;
import com.runonce.model.system.Role;

import java.util.List;

/**
 * 角色数据处理层
 * @author Exrickx
 */
public interface RoleDao extends MyBaseJpaDao<Role,String> {

    /**
     * 获取默认角色
     * @param defaultRole
     * @return
     */
    List<Role> findByDefaultRole(Boolean defaultRole);


    /**
     * 批量删除
     * @param idList
     */
    void deleteByIdIn(List<String> idList);



    List<Role> findByDataType(int dataType);
}
