package com.runonce.service.system;




import com.runonce.model.system.Role;
import com.runonce.service.MyBaseJpaService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 角色接口
 * @author Exrickx
 */
@Service
public interface RoleService extends MyBaseJpaService<Role,String> {

    /**
     * 获取默认角色
     * @param defaultRole
     * @return
     */
    List<Role> findByDefaultRole(Boolean defaultRole);

    void deleteByIdIn(List<String> idList);

    List<Role> findByDataType(int dataType);


    /**
     * 通过获取用户角色
     * @param userId
     * @return
     */
    List<Role> findByUserRole(String userId);
}
