package com.runonce.controller.system;

import cn.hutool.core.util.StrUtil;
import com.runonce.ReturnCode;
import com.runonce.httpbean.assets.PageBean;
import com.runonce.httpbean.assets.reqbean.ResetPassWordReq;
import com.runonce.httpbean.assets.reqbean.RevisePassWordReq;
import com.runonce.httpbean.assets.reqbean.SaveUserReq;
import com.runonce.httpbean.assets.reqbean.UserReq;
import com.runonce.model.system.User;
import com.runonce.model.system.UserRole;
import com.runonce.service.system.DepartmentService;
import com.runonce.service.system.UserRoleService;
import com.runonce.util.MD5;
import com.runonce.service.system.UserService;
import com.runonce.util.WebTokenUtil;
import com.runonce.util.constant.CommonConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;


/**
 * @author Exrickx
 */
@RestController
@Api(description = "用户接口")
@RequestMapping("/xboot/user")
@Transactional(rollbackFor = Exception.class)
public class UserController {

    @Autowired
    private UserService userService;


    @Autowired
    private UserRoleService userRoleService;
//    @Autowired
//    private RoleService roleService;

//
   @Autowired
  private DepartmentService departmentService;

   // @Resource
   //private IUserRoleService iUserRoleService;
//
//    @Autowired
//    private UserRoleService userRoleService;


//    @Autowired
//    private StringRedisTemplate redisTemplate;


    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ApiOperation(value = "登录接口")
    public ReturnCode smsLogin(@RequestParam String userName,
                               @RequestParam String code,
                               @RequestParam(required = false) Boolean saveLogin) {

        User user = userService.findByUserName(userName);

        if (user == null) {
            return new ReturnCode.Builder().failed().msg("用户名不存在。").build();
        }
        if (user.getStatus() == -1){
            return new ReturnCode.Builder().failed().msg("用户名已被拉黑。").build();
        }

        String md5Code = MD5.md5(code);
        if (!md5Code.equals(user.getPassword())) {
            return new ReturnCode.Builder().failed().msg("密码错误。").build();
        }
        //生成WebToken
        String webToken = WebTokenUtil.getWebToken("user", user.getId());
        user.setWebToken(webToken);

        return new ReturnCode.Builder().success().msg("登录成功").object(user).build();
    }


    @RequestMapping(value = "/info", method = RequestMethod.GET)
    @ApiOperation(value = "获取当前登录用户接口")
    public ReturnCode getUserInfo(HttpServletRequest request) {
        String s = WebTokenUtil.getUserByWebToken(request);
        User user=   userService.findByUserId(s);
        user.setPassword(null);

        return new ReturnCode.Builder().success().msg("获取成功").object(user).build();
    }


    @RequestMapping(value = "/changeMobile", method = RequestMethod.POST)
    @ApiOperation(value = "修改绑定手机")
    public ReturnCode changeMobile(@RequestParam String mobile,
                                   @RequestParam String code) {

        User user = userService.findByMobile(mobile);

        if (user == null) {
            return new ReturnCode.Builder().msg("该手机号已绑定其他账户").build();
        }

        return new ReturnCode.Builder().build();
    }


    @RequestMapping(value = "/unlock", method = RequestMethod.POST)
    @ApiOperation(value = "解锁验证密码")
    public ReturnCode unLock(@RequestParam String password, HttpServletRequest request) {

        String s = WebTokenUtil.getUserByWebToken(request);
        User user=   userService.findByUserId(s);
        String word = MD5.md5(password);

        if (word.equals(user.getPassword())) {

            return new ReturnCode.Builder().msg("密码不正确").build();
        }
        return new ReturnCode.Builder().msg("解锁成功").build();
    }

    @RequestMapping(value = "/getByCondition",method = RequestMethod.POST)
    @ApiOperation(value = "多条件分页获取用户列表")
    public ReturnCode getByCondition(@RequestBody UserReq userReq,HttpServletRequest request){
        PageBean pageBean = new PageBean();
        pageBean.setCurrentPage(userReq.getCurrentPage());
        pageBean.setPageSize(userReq.getPageSize());
        String userId = WebTokenUtil.getUserByWebToken(request);


        Map map = userService.findByCondition(userReq, pageBean,userId);

        return new ReturnCode.Builder().success().msg("查询成功").object(map).build();
    }
//
//    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
//    @ApiOperation(value = "获取全部用户数据")
//    public ReturnCode getAll() {
//        UserReq userReq = new UserReq();
//        List<User> list = userService.findByCondition(userReq, null,null);
//        return new ReturnCode.Builder().success().msg("查询成功").object(list).build();
//    }


    @RequestMapping(value = "/admin/save", method = RequestMethod.POST)
    @ApiOperation(value = "添加/保存用户")
    public ReturnCode regist(@RequestBody SaveUserReq saveUserReq,HttpServletRequest request) {

        String userId = WebTokenUtil.getUserByWebToken(request);
        if (StrUtil.isBlank(saveUserReq.getUser().getUserName())) {
            return new ReturnCode.Builder().failed().msg("缺少必需表单字段").build();
        }

        User findUser = userService.findByUserName(saveUserReq.getUser().getUserName());
        if (findUser != null&&!findUser.getId().equals(saveUserReq.getUser().getId()) ) {
            return new ReturnCode.Builder().failed().msg("该用户名已被注册").build();
        }
        User user = new User();

        if (saveUserReq.getUser().getId()!=null) {

            // 修改
            user = userService.get(saveUserReq.getUser().getId());
            saveUserReq.getUser().setCreateBy(user.getCreateBy());
            saveUserReq.getUser().setCreateTime(user.getCreateTime());
            saveUserReq.getUser().setUpdateBy(userId);
            saveUserReq.getUser().setUpdateTime(new Date());
        }else {
            //新增
            saveUserReq.getUser().setCreateBy(userId);
            saveUserReq.getUser().setCreateTime(new Date());
            saveUserReq.getUser().setUpdateBy(userId);
            saveUserReq.getUser().setUpdateTime(new Date());
        }


        if (saveUserReq.getUser().getPassword() == null){
            saveUserReq.getUser().setPassword(user.getPassword());
        }else {
            String md5Word = MD5.md5(saveUserReq.getUser().getPassword());
            saveUserReq.getUser().setPassword(md5Word);
        }


        User saveUser = userService.save(saveUserReq.getUser());
        if (saveUser == null) {
            return new ReturnCode.Builder().failed().msg("添加/保存用户").build();
        }
        //删除角色信息
        userRoleService.deleteByUserId(saveUser.getId());
        if (saveUserReq.getRoles() != null && saveUserReq.getRoles().size() > 0) {
            //添加角色
            List<UserRole> userRoleList = new ArrayList<>();
            for (String roleId : saveUserReq.getRoles()) {
                UserRole ur = new UserRole();
                ur.setUserId(saveUser.getId());
                ur.setRoleId(roleId);
                userRoleList.add(ur);
            }
            userRoleService.saveOrUpdateAll(userRoleList);
        }

        return new ReturnCode.Builder().success().msg("保存成功").build();
    }

    @RequestMapping(value = "/delByIds/{ids}", method = RequestMethod.DELETE)
    @ApiOperation(value = "批量通过ids删除")
    public ReturnCode delAllByIds(@PathVariable String[] ids) {

        List<String> list = Arrays.asList(ids);
        userService.deleteByIdIn(list);
        //删除关联角色
        userRoleService.deleteByUserIdIn(list);
        return new ReturnCode.Builder().success().msg("批量通过id删除数据成功").build();
    }

    @RequestMapping(value = "/admin/enable/{userId}",method = RequestMethod.GET)
    @ApiOperation(value = "后台启用用户")
    public ReturnCode enable(@ApiParam("用户唯一id标识") @PathVariable String userId){

        User user = userService.get(userId);
        if(user==null){
            return new  ReturnCode.Builder().failed().msg("通过userId获取用户失败").build();
        }
        user.setStatus(CommonConstant.USER_STATUS_NORMAL);
        userService.update(user);
        return new ReturnCode.Builder().success().msg("操作成功").build();
    }

    @RequestMapping(value = "/admin/disable/{userId}", method = RequestMethod.GET)
    @ApiOperation(value = "后台禁用用户")
    public ReturnCode disable(@ApiParam("用户唯一id标识") @PathVariable String userId) {

        User user = userService.get(userId);
        if (user == null) {
            return new ReturnCode.Builder().failed().msg("通过userId获取用户失败").build();
        }
        user.setStatus(CommonConstant.USER_STATUS_LOCK);
        userService.save(user);
        return new ReturnCode.Builder().success().msg("操作成功").build();
    }



    @RequestMapping(value = "/admin/revisePassWord", method = RequestMethod.POST)
    @ApiOperation(value = "修改密码")
    public ReturnCode revisePassWord(@ApiParam("修改密码表单") @RequestBody @Valid RevisePassWordReq revisePassWordReq,BindingResult bindingResult,HttpServletRequest request) {
        //验证是否有错，如果有错就输出错误信息
        if (bindingResult.hasErrors()) {
            return new ReturnCode.Builder().failed().msg(bindingResult.getFieldError().getDefaultMessage()).build();
        }
        String userId = WebTokenUtil.getUserByWebToken(request);


        return userService.revisePassWord(revisePassWordReq,userId);
    }


    @RequestMapping(value = "/admin/resetPassWord", method = RequestMethod.POST)
    @ApiOperation(value = "重置密码")
    public ReturnCode resetPassWord(@ApiParam("重置密码表单") @RequestBody @Valid ResetPassWordReq resetPassWordReq, BindingResult bindingResult,HttpServletRequest request) {
       // 验证是否有错，如果有错就输出错误信息
        if (bindingResult.hasErrors()) {
            return new ReturnCode.Builder().failed().msg(bindingResult.getFieldError().getDefaultMessage()).build();
        }
        return userService.resetPassWord(resetPassWordReq);
    }

}
