package com.runonce.service.system;

import com.runonce.model.system.Permission;
import com.runonce.service.MyBaseJpaService;
import com.runonce.service.Service;

import java.util.List;

/**
 * 权限接口
 * @author Exrick
 */
public interface PermissionService extends MyBaseJpaService<Permission,String> {

    /**
     * 通过层级查找
     * 默认升序
     * @param level
     * @return
     */
    List<Permission> findByLevelOrderBySortOrder(Integer level);

    /**
     * 通过parendId查找
     * @param parentId
     * @return
     */
    List<Permission> findByParentIdOrderBySortOrder(String parentId);

    /**
     * 通过类型和状态获取
     * @param type
     * @param status
     * @return
     */
    List<Permission> findByTypeAndStatusOrderBySortOrder(Integer type, Integer status);

    /**
     * 通过名称获取
     * @param title
     * @return
     */
    List<Permission> findByTitle(String title);

    /**
     * 模糊搜索
     * @param title
     * @return
     */
    List<Permission> findByTitleLikeOrderBySortOrder(String title);


    /**
     * 批量
     * @param idList
     */
    void deleteByIdIn(List<String> idList);


    /**
     *
     * @param title
     * @param id
     * @return
     */
    Permission findByTitleAndNotId(String title, String id);
}