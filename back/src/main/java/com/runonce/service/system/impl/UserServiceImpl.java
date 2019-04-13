package com.runonce.service.system.impl;


import com.runonce.ReturnCode;
import com.runonce.dao.system.DepartmentDao;
import com.runonce.dao.system.UserDao;
import com.runonce.dao.system.mapper.DepartmentMapper;
import com.runonce.dao.system.mapper.UserMapper;
import com.runonce.httpbean.assets.PageBean;
import com.runonce.httpbean.assets.Paging;
import com.runonce.httpbean.assets.reqbean.ResetPassWordReq;
import com.runonce.httpbean.assets.reqbean.RevisePassWordReq;
import com.runonce.httpbean.assets.reqbean.UserReq;
import com.runonce.model.system.Department;
import com.runonce.model.system.Role;
import com.runonce.model.system.User;

import com.runonce.service.system.UserRoleService;
import com.runonce.service.system.UserService;
import com.runonce.util.MD5;
import com.runonce.util.SecurityUtil;
import com.runonce.util.constant.CommonConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;
import java.util.stream.Collectors;


/**
 * 用户接口实现
 *
 * @author Exrickx
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl implements UserService {

//    @Resource
//    private DepartmentService departmentService;
//
//    @Resource
//    private UserRoleService userRoleService;

    private SecurityUtil securityUtil;
    @Resource
    private UserMapper userMapper;

    @Resource
    private UserRoleService userRoleService;

    @Resource
    private UserDao userDao;
    @Resource
    private DepartmentDao departmentDao;

    @Resource
    private DepartmentMapper departmentMapper;


    @Resource
    private DepartmentServiceImpl departmentService;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public UserDao getRepository() {
        return userDao;
    }


    @Override
    public User findByUserId(String userId) {


        UserReq userReq = new UserReq();
        userReq.setId(userId);
        List<User> findUser = userMapper.findInfo(userReq, null, null);
//            // 关联部门
//            if(StrUtil.isNotBlank(findUser.getDepartmentId())){
//                Department department = departmentService.findById(findUser.getDepartmentId());
//                findUser.setDepartmentTitle(department.getTitle());
//            }
//            // 关联角色
//            List<Role> roleList = userRoleService.findBy( ""  ,findUser.getId());
//            findUser.setRoles(roleList);
//            // 关联权限菜单
//            List<Permission> permissionList = permissionMapper.findByUserId(findUser.getId());
//            findUser.setPermissions(permissionList);
        User user = null;
        if (findUser.size() > 0) {
            user = findUser.get(0);
        }
        return user;
    }

    /**
     * 通过手机获取用户
     *
     * @param mobile
     * @return
     */
    @Override
    public User findByMobile(String mobile) {
        List<User> list = userDao.findByMobile(mobile);
        if (list != null && list.size() > 0) {
            User user = list.get(0);
            return user;
        }
        return null;
    }

    /**
     * 通过邮件和状态获取用户
     *
     * @param email
     * @return
     */
    @Override
    public User findByEmail(String email) {
        List<User> list = userDao.findByEmail(email);
        if (list != null && list.size() > 0) {
            User user = list.get(0);
            return user;
        }
        return null;
    }

    /**
     * 多条件分页获取用户
     *
     * @param userReq
     * @param pageBean
     * @return
     */
    @Override
    public Map findByCondition(UserReq userReq, PageBean pageBean, String userId) {


        User user = findByUserId(userId);

        List<Department> deparmentList = new ArrayList<>();
        //判断有无全部数据的角色
        int flag = 2;
        for (Role r : user.getRoles()) {
            if (r.getDataType() < flag) {
                flag = r.getDataType();
            }
        }
        Paging page = new Paging();
        if (pageBean == null) {
            page = null;
        } else {
            page.setPageIndex(pageBean.getCurrentPage());
            page.setPageSize(pageBean.getPageSize());
        }


        List<String> deptList = getDeparmentIdList(userId, true, false);


        int findUserCount = userMapper.findInfoCount(userReq, deptList);

        List<User> findUser = userMapper.findInfo(userReq, page, deptList);

        Map map = new HashMap();

        map.put("total", findUserCount);
        map.put("list", findUser);

        return map;
    }


    /**
     * @描述 通过部门id获取
     * @参数 [departmentId]
     * @返回值 java.util.List<com.runonce.model.system.User>
     * @创建人 swq
     * @创建时间 2018/12/13 0013
     * @修改人和其它信息
     */
    @Override
    public List<User> findByDepartmentId(String departmentId) {
        List<User> userList = userDao.findByDepartmentIdAndDelFlag(departmentId, 0);
        return userList;
    }

    /**
     * @描述 通过userId只查询user信息
     * @参数 [userId]
     * @返回值 com.runonce.model.system.User
     * @创建人 swq
     * @创建时间 2018/12/13 0013
     * @修改人和其它信息
     */
    @Override
    public User findUserByUserId(String userId) {
        User user = userDao.findOne(userId);
        return user;
    }

    @Override
    public User findByUserName(String UserName) {

        UserReq userReq = new UserReq();
        userReq.setUserName(UserName);


        List<User> findUser = userDao.findByUserNameAndDelFlag(UserName, 0);

        User user = null;
        if (findUser.size() > 0) {
            user = findUser.get(0);
        }
        return user;
    }

    /**
     * 根据用户id 获取用户角色的部门权限
     *
     * @param userId
     */
    @Override
    public List<Department> getDeparmentIdList(String userId, Boolean openDataFilter, String locationId) {
        User user = findByUserId(userId);

        List<Department> deparmentList = new ArrayList<>();
        //判断有无全部数据的角色
        int flag = 2;
        for (Role r : user.getRoles()) {
            if (r.getDataType() < flag) {
                flag = r.getDataType();
            }
        }

        List<Department> allDept = departmentMapper.getAllByIsLocation(false);
        //不开启数据权限
        if (!openDataFilter) {
            getBelongDept(locationId, allDept, deparmentList);
        }

        //所有权限
        if (flag == CommonConstant.DATA_TYPE_ALL) {
            getBelongDept(locationId, allDept, deparmentList);
        }

        //所有部门和部门子部门
        if (flag == CommonConstant.DATA_TYPE_DEPT_CUSTOM) {
            Department department = departmentDao.findByIdAndDelFlag(user.getDepartmentId(), 0);
            if (department != null) {
                deparmentList.add(department);
            }
            if (locationId.equals(user.getLocationId())) {
                getBelongDept(user.getDepartmentId(), allDept, deparmentList);
            }

            //  return departmentIds;
        }
        //当前部门
        if (flag == CommonConstant.DATA_TYPE_CARDING) {
            Department department = departmentDao.findByIdAndDelFlag(user.getDepartmentId(), 0);
            if (department != null) {
                deparmentList.add(department);
            }

        }

//        // 查找自定义
        return deparmentList;

    }

    /**
     * 根据用户id 获取用户角色的部门权限
     * @param userId
     */
    @Override
    public List<String> getDeparmentIdList(String userId, Boolean openDataFilter, Boolean IsLocation) {
        User user = findByUserId(userId);
        List<String> deparmentIdList = new ArrayList<>();
        //判断有无全部数据的角色
        int flag = 2;
        for (Role r : user.getRoles()) {
            if (r.getDataType() < flag) {
                flag = r.getDataType();
            }
        }
        //不开启数据权限
        if (!openDataFilter) {
            return null;
        }
        //所有权限
        if (flag == CommonConstant.DATA_TYPE_ALL) {
            return null;
        }
        //所有部门和部门子部门
        if (flag == CommonConstant.DATA_TYPE_DEPT_CUSTOM) {

            List<Department> allDept = departmentMapper.getAllByIsLocation(IsLocation);
            deparmentIdList.add(user.getDepartmentId());
            getBelongDeptId(user.getDepartmentId(), allDept, deparmentIdList);
            //  return departmentIds;
        }
        //当前部门
        if (flag == CommonConstant.DATA_TYPE_CARDING) {

            deparmentIdList.add(user.getDepartmentId());
        }

        // 查找自定义
        return deparmentIdList;

    }

    /**
     * 根据用户id 获取用户角色的所属区域
     *
     * @param userId
     * @param openDataFilter
     * @return List<String>
     */
    @Override
    public List<Department> getLocationIdList(String userId, Boolean openDataFilter) {
        User user = findByUserId(userId);
        List<Department> deparmentList = new ArrayList<>();
        //判断有无全部数据的角色
        int flag = 2;
        for (Role r : user.getRoles()) {
            if (r.getDataType() < flag) {
                flag = r.getDataType();
            }
        }
        //不开启数据权限
        if (!openDataFilter) {
            deparmentList = departmentDao.findByDelFlagAndIsLocationOrderBySortOrder(0, true);
        }

        //所有权限
        if (flag == CommonConstant.DATA_TYPE_ALL) {

            deparmentList = departmentDao.findByDelFlagAndIsLocationOrderBySortOrder(0, true);
        } else {
            Department department = departmentDao.findByIdAndDelFlag(user.getLocationId(), 0);
            department.setParentId("0");
            entityManager.clear();
            deparmentList.add(department);

        }


        return deparmentList;
    }

    /**
     * 根据用户id批量删除
     *
     * @param list
     */
    @Override
    public void deleteByIdIn(List<String> list) {

        userDao.deleteByIdIn(list);
    }


    /**
     * 部门
     *
     * @param departmentIdList
     * @return
     */
    @Override
    public int countByDepartmentIdIn(List<String> departmentIdList) {

        int count = userDao.countByDepartmentIdInAndDelFlag(departmentIdList, 0);
        return count;
    }

    /**
     * 密码修改
     *
     * @param revisePassWordReq
     * @param userId
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ReturnCode revisePassWord(RevisePassWordReq revisePassWordReq, String userId) {

        //验证用户是否存在
        User user = userDao.findOne(userId);
        if (user == null) {
            return new ReturnCode.Builder().failed().msg("当前登录用户不存在。").build();
        }

        String md5OldPass = MD5.md5(revisePassWordReq.getOldPass());

        String md5NewPass = MD5.md5(revisePassWordReq.getNewPass());
        //验证用户旧密码
        if (!md5OldPass.equals(user.getPassword())) {
            return new ReturnCode.Builder().failed().msg("旧密码错误，请检查。").build();
        }

        //保存用户新密码
        user.setPassword(md5NewPass);
        userDao.save(user);
        return new ReturnCode.Builder().success().msg("修改成功。").build();
    }

    /**
     * 密码重置
     *
     * @param resetPassWordReq
     * @return
     */
    @Override
    public ReturnCode resetPassWord(ResetPassWordReq resetPassWordReq) {

        //验证用户是否存在
        User user = userDao.findOne(resetPassWordReq.getUserId());
        if (user == null) {
            return new ReturnCode.Builder().failed().msg("当前登录用户不存在。").build();
        }
        String md5PassWord = MD5.md5(resetPassWordReq.getPassWord());
        //保存用户新密码
        user.setPassword(md5PassWord);
        userDao.save(user);
        return new ReturnCode.Builder().success().msg("重置成功。").build();
    }


    /**
     * 获取子部门id
     */
    public void getBelongDept(String departmentId, List<Department> allDept, List<Department> departmentList) {

        List<Department> list = allDept.stream().filter(a -> a.getParentId().equals(departmentId)).collect(Collectors.toList());
        if (list != null && list.size() > 0) {
            departmentList.addAll(list);
            for (Department item : list) {
                getBelongDept(item.getId(), allDept, departmentList);
            }
        }
    }
    /**
     * 获取子部门id
     */
    public void getBelongDeptId(String departmentId, List<Department> allDept, List<String> deparmentIdList) {


        List<String> list = allDept.stream().filter(a -> a.getParentId().equals(departmentId)).map(b ->b.getId()).collect(Collectors.toList());
        if (list != null && list.size() > 0) {

                deparmentIdList.addAll(list);

            for (String id : list) {
                getBelongDeptId(id, allDept, deparmentIdList);
            }
        }
    }

}


