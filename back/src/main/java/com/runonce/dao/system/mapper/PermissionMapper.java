package com.runonce.dao.system.mapper;

import com.runonce.dao.MyMapper;
import com.runonce.model.system.Permission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.BaseMapper;

import java.util.List;

/**
 * @author Exrickx
 */
@Mapper
public interface PermissionMapper extends MyMapper<Permission> {


}
