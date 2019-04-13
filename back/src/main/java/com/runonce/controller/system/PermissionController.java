package com.runonce.controller.system;

import com.runonce.ReturnCode;
import com.runonce.model.system.Permission;
import com.runonce.model.system.RolePermission;
import com.runonce.model.system.User;
import com.runonce.service.system.PermissionService;
import com.runonce.service.system.RolePermissionService;
import com.runonce.service.system.UserService;
import com.runonce.util.WebTokenUtil;
import com.runonce.util.constant.CommonConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;


/**
 * @author Exrick
 */
@RestController
@Api(description = "菜单/权限管理接口")
@RequestMapping("/xboot/permission")
@Transactional(rollbackFor = Exception.class)
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private RolePermissionService rolePermissionService;

    @Autowired
    private UserService userService;

    //  @Autowired
    //private MySecurityMetadataSource mySecurityMetadataSource;

    @RequestMapping(value = "/getMenuList", method = RequestMethod.GET)
    @ApiOperation(value = "获取用户页面菜单数据")
    public ReturnCode getAllMenuList(HttpServletRequest request) {

        List<Permission> list = new ArrayList<>();
        User user = userService.findByUserId(WebTokenUtil.getUserByWebToken(request));
        // 读取缓存
        // User u = securityUtil.getCurrUser();
        // String key = "permission::userMenuList:" + u.getId();
        //  String v = redisTemplate.opsForValue().get(key);
        // if(StrUtil.isNotBlank(v)){
        //  list = new Gson().fromJson(v, new TypeToken<List<Permission>>(){}.getType());
        //  return new ReturnCode.Builder().msg("查询成功").success().object(list).build();
        //  }

        // 用户所有权限 已排序去重

        list = user.getPermissions();

        List<Permission> menuList = new ArrayList<>();
        // 筛选一级页面
        for (Permission p : list) {
            if (CommonConstant.PERMISSION_PAGE==(p.getType()) && CommonConstant.LEVEL_ONE==(p.getLevel())) {
                menuList.add(p);
            }
        }
        // 筛选二级页面
        List<Permission> secondMenuList = new ArrayList<>();
        for (Permission p : list) {
            if (CommonConstant.PERMISSION_PAGE==(p.getType()) && CommonConstant.LEVEL_TWO==(p.getLevel())) {
                secondMenuList.add(p);
            }
        }
        // 筛选二级页面拥有的按钮权限
        List<Permission> buttonPermissions = new ArrayList<>();
        for (Permission p : list) {
            if (CommonConstant.PERMISSION_OPERATION==(p.getType()) && CommonConstant.LEVEL_THREE==(p.getLevel())) {
                buttonPermissions.add(p);
            }
        }

        // 匹配二级页面拥有权限
        for (Permission p : secondMenuList) {
            List<String> permTypes = new ArrayList<>();
            for (Permission pe : buttonPermissions) {
                if (p.getId().equals(pe.getParentId())) {
                    permTypes.add(pe.getButtonType());
                }
            }
            p.setPermTypes(permTypes);
        }
        // 匹配一级页面拥有二级页面
        for (Permission p : menuList) {
            List<Permission> secondMenu = new ArrayList<>();
            for (Permission pe : secondMenuList) {
                if (p.getId().equals(pe.getParentId())) {
                    secondMenu.add(pe);
                }
            }
            p.setChildren(secondMenu);
        }

        // 缓存
        //redisTemplate.opsForValue().set(key, new Gson().toJson(menuList));
        return new ReturnCode.Builder().success().msg("查询成功").object(menuList).build();
    }

    @RequestMapping(value = "/getAllList", method = RequestMethod.GET)
    @ApiOperation(value = "获取权限菜单树")
    public ReturnCode getAllList() {

        // 一级
        List<Permission> list = permissionService.findByLevelOrderBySortOrder(CommonConstant.LEVEL_ONE);
        // 二级
        for (Permission p1 : list) {
            List<Permission> children1 = permissionService.findByParentIdOrderBySortOrder(p1.getId());
            p1.setChildren(children1);
            // 三级
            for (Permission p2 : children1) {
                List<Permission> children2 = permissionService.findByParentIdOrderBySortOrder(p2.getId());
                p2.setChildren(children2);
            }
        }
        return new ReturnCode.Builder().success().msg("获取成功").object(list).build();
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation(value = "添加")
    public ReturnCode add(@RequestBody Permission permission) {


        List<Permission> list = permissionService.findByTitle(permission.getTitle());
        if (list != null && list.size() > 0) {
            return new ReturnCode.Builder().paramError().msg("名称已经存在").object(list).build();
        }

        Permission u = permissionService.save(permission);
        //重新加载权限
        //  mySecurityMetadataSource.loadResourceDefine();
        //手动删除缓存
        // redisTemplate.delete("permission::allList");
        return new ReturnCode.Builder().success().msg("添加成功").object(u).build();
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation(value = "编辑")
    public ReturnCode edit(@RequestBody Permission permission) {

        // 判断拦截请求的操作权限按钮名是否已存在

        // 若名称修改
        Permission p = permissionService.findByTitleAndNotId(permission.getTitle(), permission.getId());
        if (p != null) {
            return new ReturnCode.Builder().paramError().msg("名称已经存在").object(p).build();
        }

        Permission u = permissionService.update(permission);
        //重新加载权限
        //  mySecurityMetadataSource.loadResourceDefine();
        //手动批量删除缓存
        //Set<String> keys = redisTemplate.keys("userPermission:" + "*");
        /// redisTemplate.delete(keys);
        // Set<String> keysUser = redisTemplate.keys("user:" + "*");
        // redisTemplate.delete(keysUser);
        // Set<String> keysUserMenu = redisTemplate.keys("permission::userMenuList:*");
        // redisTemplate.delete(keysUserMenu);
        // redisTemplate.delete("permission::allList");
        return new ReturnCode.Builder().success().msg("编辑成功").object(u).build();
    }

    @RequestMapping(value = "/delByIds/{ids}", method = RequestMethod.DELETE)
    @ApiOperation(value = "批量通过id删除")
    public ReturnCode delByIds(@PathVariable String[] ids) {

        for (String id : ids) {
            List<RolePermission> list = rolePermissionService.findByPermissionId(id);
            if (list != null && list.size() > 0) {
                return new ReturnCode.Builder().failed().msg("删除失败，包含正被角色使用关联的菜单或权限").build();
            }
        }
        List<String> idList = Arrays.asList(ids);
        permissionService.deleteByIdIn(idList);

        return new ReturnCode.Builder().success().msg("批量通过id删除数据成功").build();
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    @ApiOperation(value = "搜索菜单")
    public ReturnCode searchPermissionList(@RequestParam String title) {

        List<Permission> list = permissionService.findByTitleLikeOrderBySortOrder(title);
        return new ReturnCode.Builder().success().msg("查询成功").object(list).build();
    }
}
