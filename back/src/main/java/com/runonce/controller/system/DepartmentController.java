package com.runonce.controller.system;


import com.runonce.ReturnCode;
import com.runonce.model.system.Department;
import com.runonce.service.system.DepartmentService;
import com.runonce.service.system.UserService;
import com.runonce.util.WebTokenUtil;
import com.runonce.util.constant.CommonConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * @author Exrick
 */
@RestController
@Api(description = "部门管理接口")
@RequestMapping("/xboot/department")
 @CacheConfig(cacheNames = "department")
@Transactional(rollbackFor = Exception.class)
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/getByParentId/{parentId}", method = RequestMethod.GET)
    @ApiOperation(value = "通过id获取")
    public ReturnCode getByParentId(@PathVariable String parentId,
                                    @ApiParam("是否开始数据权限过滤") @RequestParam(required = false, defaultValue = "true") Boolean openDataFilter, @ApiParam("是否是地点") @RequestParam(required = false, defaultValue = "false") Boolean IsLocation, HttpServletRequest request) {

        String userId = WebTokenUtil.getUserByWebToken(request);
        List<Department> list = userService.getDeparmentIdList(userId, openDataFilter, parentId);
        return new ReturnCode.Builder().success().msg("获取成功").object(list).build();
    }


    @RequestMapping(value = "/getDropList", method = RequestMethod.GET)
    @ApiOperation(value = "初次获取部门下拉菜单")
    public ReturnCode getDropList(@ApiParam("是否开始数据权限过滤") @RequestParam(required = false, defaultValue = "true") Boolean openDataFilter, HttpServletRequest request) {

        List<Map> list ;

        String userId = WebTokenUtil.getUserByWebToken(request);


        if (userId != null) {

            list = departmentService.getDropList(userId, openDataFilter);

        } else {
            return new ReturnCode.Builder().success().msg("当前登陆用户已失效").build();
        }

        return new ReturnCode.Builder().success().msg("获取成功").object(list).build();
    }


    @RequestMapping(value = "/getDropSonList/{parentId}", method = RequestMethod.GET)
    @ApiOperation(value = "获取下一级部门下拉菜单")
    public ReturnCode getDropSonList(@PathVariable String parentId,
                                     @ApiParam("是否开始数据权限过滤") @RequestParam(required = false, defaultValue = "true") Boolean openDataFilter, HttpServletRequest request) {

        List<Map> list;

        String userId = WebTokenUtil.getUserByWebToken(request);


        if (userId != null) {

            list = departmentService.getDropSonList(parentId, openDataFilter);

        } else {
            return new ReturnCode.Builder().success().msg("当前登陆用户已失效").build();
        }

        return new ReturnCode.Builder().success().msg("获取成功").object(list).build();
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation(value = "添加")
    public ReturnCode add(@RequestBody Department department) {
//        if (!CommonConstant.PARENT_ID.equals(department.getParentId())) {
        //判断他的父下是否重名
        int count = departmentService.countByTitleAndParentIdAndDelFlag(department.getTitle(), department.getParentId());
        if (count > 0) {
            return new ReturnCode.Builder().failed().msg("部门名称不能重复。").build();
        }
//        } else {
//
//            int count = departmentService.countByTitleAndParentIdAndDelFlag(department.getTitle(), department.getParentId());
//        }


        departmentService.save(department);

        // 如果不是添加的一级 判断设置上级为父节点标识
        if (!CommonConstant.PARENT_ID.equals(department.getParentId())) {
            Department parent = departmentService.get(department.getParentId());
            if (parent.getIsParent() == null || !parent.getIsParent()) {
                parent.setIsParent(true);
                departmentService.update(parent);
            }
        }
        return new ReturnCode.Builder().success().msg("添加成功").build();
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation(value = "编辑")
    public ReturnCode edit(@RequestBody Department department) {
        int count = departmentService.countByTitleAndParentIdAndDelFlagAndIdNot(department.getTitle(), department.getParentId(), department.getId());

        Department selectDepartment =  departmentService.get(department.getId());
        if (selectDepartment == null) {
            return new ReturnCode.Builder().failed().msg("未找到该记录。").build();
        }
        department.setParentId(department.getParentId());
        if (count > 0) {
            return new ReturnCode.Builder().failed().msg("部门名称不能重复。").build();
        }
        departmentService.update(department);
        // 手动删除所有部门缓存
        //  Set<String> keys = redisTemplate.keys("department:" + "*");
        //  redisTemplate.delete(keys);
        // 删除所有用户缓存
        //  Set<String> keysUser = redisTemplate.keys("user:" + "*");
        //  redisTemplate.delete(keysUser);
        return new ReturnCode.Builder().msg("编辑成功").success().build();
    }

    @RequestMapping(value = "/delByIds/{ids}", method = RequestMethod.DELETE)
    @ApiOperation(value = "批量通过id删除")
    public ReturnCode delByIds(@PathVariable String[] ids) {

//        for (String id : ids) {
//            List<User> list = userService.findByDepartmentId(id);
//            if (list != null && list.size() > 0) {
//                return new ReturnCode.Builder().failed().msg("该部门下存在用户，无法直接删除").build();
//            }
//        }
        List<String> idList = Arrays.asList(ids);

        int count = userService.countByDepartmentIdIn(idList);

        if (count > 0) {
            return new ReturnCode.Builder().failed().msg("所选部门下存在用户，无法直接删除").build();
        }
        int sonDepartmentCount = departmentService.countByParentIdInAndDelFlag(idList);

        if (sonDepartmentCount > 0) {
            return new ReturnCode.Builder().failed().msg("所选部门下存在子部门，无法直接删除").build();
        }

        departmentService.deleteByIdIn(idList);
//
        //  for (String id : ids) {
        //  departmentService.delete(id);
        // 删除关联数据权限
        //  roleDepartmentService.deleteByDepartmentId(id);
        //   }
        // 手动删除所有部门缓存
        //Set<String> keys = redisTemplate.keys("department:" + "*");
        // redisTemplate.delete(keys);
        // 删除数据权限缓存
        // Set<String> keysUserRoleData = redisTemplate.keys("userRole::depIds:" + "*");
        //redisTemplate.delete(keysUserRoleData);
        return new ReturnCode.Builder().success().msg("批量通过id删除数据成功").build();
    }

    @RequestMapping(value = "/getall", method = RequestMethod.GET)
    @ApiOperation(value = "部门按权限全查")
    public ReturnCode getAll(@ApiParam("是否开始数据权限过滤") @RequestParam(required = false, defaultValue = "true") Boolean openDataFilter, @ApiParam("是否查询地点") @RequestParam(required = false, defaultValue = "true") String IsLocation, HttpServletRequest request) {
        List<Department> list = null;
        String userId = WebTokenUtil.getUserByWebToken(request);


        if (userId != null) {
            list = departmentService.findAll(openDataFilter, userId, IsLocation);
//            lambda表达式
//            list.forEach(item -> {
//                if (!CommonConstant.PARENT_ID.equals(item.getParentId())) {
//                    Department parent = departmentService.get(item.getParentId());
//                    item.setParentTitle(parent.getTitle());
//                } else {
//                    item.setParentTitle("一级部门");
//                }
//            });
        } else {
            return new ReturnCode.Builder().success().msg("当前登陆用户已失效").build();
        }
        return new ReturnCode.Builder().success().msg("查询成功").object(list).build();
    }

    @GetMapping("checkHasChildren")
    @ApiOperation(value = "部门/地点下是否有事项")
    public ReturnCode checkHasChildren(@RequestParam String deptId) {
        if(deptId == null || "".equals(deptId)){
            return new ReturnCode.Builder().failed().msg("输入为空").build();
        }
        departmentService.checkHasChildren(deptId);
        return new ReturnCode.Builder().success().msg("成功").build();
    }

    @RequestMapping(value = "/sendPlace/{areaCode}", method = RequestMethod.GET)
    @ApiOperation(value = "推送地点下所有部门")
    public ReturnCode sendPlace(@PathVariable String areaCode) {
        if(StringUtils.isEmpty(areaCode)){
            return new ReturnCode.Builder().failed().msg("地点为空").build();
        }
        return departmentService.sendPlace(areaCode);
    }



}
