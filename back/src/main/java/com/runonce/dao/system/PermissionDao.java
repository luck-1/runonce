package com.runonce.dao.system;
import com.runonce.dao.MyBaseJpaDao;
import com.runonce.model.system.Permission;

import java.util.List;

/**
 * 权限数据处理层
 * @author Exrick
 */
public interface PermissionDao extends MyBaseJpaDao<Permission,String> {

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
     * 批量删除
     * @param idList
     */
    void deleteByIdIn(List<String> idList);

    /**
     * 判断是否重名
     * @param title
     * @param id
     * @return
     */
    Permission findByTitleAndIdNot(String title,String id);
}