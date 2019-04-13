package com.runonce.dao.system.mapper;

import com.runonce.dao.MyBaseJpaDao;
import com.runonce.dao.MyMapper;
import com.runonce.model.system.RolePermission;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 角色权限数据处理层
 * @author Exrick
 */
@Mapper
public interface RolePermissionMapper extends MyMapper<RolePermission> {

}