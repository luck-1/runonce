package com.runonce.controller.system;


import com.runonce.ReturnCode;
import com.runonce.model.system.*;
import com.runonce.service.system.*;
import com.runonce.util.PageUtil;
import com.runonce.util.WebTokenUtil;
import com.runonce.util.constant.CommonConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import com.runonce.model.PageVo;
import org.springframework.data.domain.Page;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * @author Exrickx
 */
@RestController
@Api(description = "角色管理接口")
@RequestMapping("/xboot/role")
@Transactional(rollbackFor = Exception.class)
public class RoleController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private RolePermissionService rolePermissionService;

    @Autowired
    private RoleDepartmentService roleDepartmentService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @RequestMapping(value = "/getAllList", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部角色")
    public ReturnCode roleGetAll(HttpServletRequest request) {
        String userId = WebTokenUtil.getUserByWebToken(request);
        User user = userService.findByUserId(userId);
        //判断有无全部数据的角色
        int flag = 2;
        for (Role r : user.getRoles()) {
            if (r.getDataType() < flag) {
                flag = r.getDataType();
            }
        }
        List<Role> roleList =new ArrayList<>();
        //所有权限
        if (flag == CommonConstant.DATA_TYPE_ALL) {
            roleList = roleService.getAll();
        }
        //所有部门和部门子部门
        if (flag == CommonConstant.DATA_TYPE_DEPT_CUSTOM) {
            roleList = roleService.findByDataType(2);
            //  return departmentIds;
        }
        //当前部门
        if (flag == CommonConstant.DATA_TYPE_CARDING) {

        }


        return new ReturnCode.Builder().success().msg("获取成功").object(roleList).build();
    }

    @RequestMapping(value = "/getAllByPage", method = RequestMethod.POST)
    @ApiOperation(value = "分页获取角色")
    public ReturnCode getRoleByPage(@RequestBody PageVo page) {


        Page<Role> list = roleService.findAll(PageUtil.initPage(page));

        for (Role role : list.getContent()) {
            // 角色拥有权限
            List<RolePermission> permissions = rolePermissionService.findByRoleId(role.getId());
            role.setPermissions(permissions);
            // 角色拥有数据权限
            List<RoleDepartment> departments = roleDepartmentService.findByRoleId(role.getId());
            role.setDepartments(departments);
        }
        return new ReturnCode.Builder().success().object(list).msg("查询成功").build();
    }

    @RequestMapping(value = "/setDefault", method = RequestMethod.GET)
    @ApiOperation(value = "设置或取消默认角色")
    public ReturnCode setDefault(@RequestParam String id,
                                 @RequestParam Boolean isDefault) {

        Role role = roleService.get(id);
        if (role == null) {
            return new ReturnCode.Builder().failed().msg("角色不存在").build();
        }
        role.setDefaultRole(isDefault);
        roleService.update(role);
        return new ReturnCode.Builder().success().msg("设置成功").build();
    }

    @RequestMapping(value = "/editRolePerm", method = RequestMethod.GET)
    @ApiOperation(value = "编辑角色分配菜单权限")
    public ReturnCode editRolePerm(@RequestParam String roleId,
                                   @RequestParam(required = false) String[] permIds) {

        //删除其关联权限
        rolePermissionService.deleteByRoleId(roleId);
        //分配新权限
        for (String permId : permIds) {
            RolePermission rolePermission = new RolePermission();
            rolePermission.setRoleId(roleId);
            rolePermission.setPermissionId(permId);
            rolePermissionService.save(rolePermission);
        }
        //手动批量删除缓存
        //  Set<String> keysUser = redisTemplate.keys("user:" + "*");
        // redisTemplate.delete(keysUser);
        // Set<String> keysUserRole = redisTemplate.keys("userRole:" + "*");
        // redisTemplate.delete(keysUserRole);
        // Set<String> keysUserPerm = redisTemplate.keys("userPermission:" + "*");
        // redisTemplate.delete(keysUserPerm);
        // Set<String> keysUserMenu = redisTemplate.keys("permission::userMenuList:*");
        // redisTemplate.delete(keysUserMenu);
        return new ReturnCode.Builder().success().msg("设置成功").build();
    }

    @RequestMapping(value = "/editRoleDep", method = RequestMethod.GET)
    @ApiOperation(value = "编辑角色分配数据权限")
    public ReturnCode editRoleDep(@RequestParam String roleId,
                                  @RequestParam Integer dataType,
                                  @RequestParam(required = false) String[] depIds) {

        Role r = roleService.get(roleId);
        r.setDataType(dataType);
        roleService.update(r);
        // 删除其关联数据权限
        roleDepartmentService.deleteByRoleId(roleId);
        // 分配新数据权限
        for (String depId : depIds) {
            RoleDepartment roleDepartment = new RoleDepartment();
            roleDepartment.setRoleId(roleId);
            roleDepartment.setDepartmentId(depId);
            roleDepartmentService.save(roleDepartment);
        }
        // 手动删除相关缓存
        // Set<String> keys = redisTemplate.keys("department:" + "*");
        //redisTemplate.delete(keys);
        //Set<String> keysUserRole = redisTemplate.keys("userRole:" + "*");
        //redisTemplate.delete(keysUserRole);

        return new ReturnCode.Builder().success().msg("设置成功").build();
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ApiOperation(value = "保存数据")
    public ReturnCode save(@RequestBody Role role) {

        Role r = roleService.save(role);
        return new ReturnCode.Builder().success().msg("设置成功").object(r).build();
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation(value = "更新数据")
    public ReturnCode edit(@RequestBody Role entity) {

        Role r = roleService.update(entity);
        //手动批量删除缓存
        //Set<String> keysUser = redisTemplate.keys("user:" + "*");
        // redisTemplate.delete(keysUser);
        //Set<String> keysUserRole = redisTemplate.keys("userRole:" + "*");
        //redisTemplate.delete(keysUserRole);
        return new ReturnCode.Builder().success().msg("设置成功").object(r).build();
    }

    @RequestMapping(value = "/delAllByIds/{ids}", method = RequestMethod.DELETE)
    @ApiOperation(value = "批量通过ids删除")
    public ReturnCode delByIds(@PathVariable String[] ids) {

        for (String id : ids) {
            List<UserRole> list = userRoleService.findByRoleId(id);
            if (list != null && list.size() > 0) {
                return new ReturnCode.Builder().failed().msg("删除失败，包含正被用户使用关联的角色").build();
            }
        }
        List<String> IdList = Arrays.asList(ids);

        roleService.deleteByIdIn(IdList);
        //删除关联菜单权限
        rolePermissionService.deleteByRoleIdIn(IdList);
        //删除关联数据权限
        roleDepartmentService.deleteByRoleIdIn(IdList);

        return new ReturnCode.Builder().success().msg("批量通过id删除数据成功").build();
    }

}
