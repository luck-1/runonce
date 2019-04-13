package com.runonce.dao.system.mapper;

import com.runonce.dao.MyMapper;
import com.runonce.httpbean.assets.reqbean.Group;
import com.runonce.httpbean.assets.reqbean.Place;
import com.runonce.model.system.Department;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 部门数据处理层
 * @author Exrick
 */
@Mapper
public interface DepartmentMapper extends MyMapper<Department> {

        List<Map> selectByIdOrParentId(@Param("id")  String id,  @Param("parentId") String parentId);

        List<Department> getAllByIsLocation(@Param("isLocation")  Boolean IsLocation);

        List<String> selectDeptGly(@Param("deptId")  String deptId);

        List<String> selectReviewGly();

        List<String> selectGly();

        List<String> selectAllLocation();

        Integer findEventById(@Param("deptId")  String deptId);

        Integer findChildrenById(@Param("deptId")  String deptId);

        List<Group> sendGroup(@Param("areaCode")  String areaCode);

        Place sendPlace(@Param("areaCode")  String areaCode);

}