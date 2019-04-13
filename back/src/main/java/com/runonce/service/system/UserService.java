package com.runonce.service.system;

import com.runonce.ReturnCode;
import com.runonce.httpbean.assets.PageBean;
import com.runonce.httpbean.assets.reqbean.ResetPassWordReq;
import com.runonce.httpbean.assets.reqbean.RevisePassWordReq;
import com.runonce.httpbean.assets.reqbean.UserReq;
import com.runonce.model.system.Department;
import com.runonce.model.system.Role;
import com.runonce.model.system.RolePermission;
import com.runonce.model.system.User;
import com.runonce.service.MyBaseJpaService;
import com.runonce.service.Service;
import com.runonce.util.SearchVo;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 用户接口
 * @author Exrickx
 */
public interface UserService extends MyBaseJpaService<User,String> {


    /**
     * 通过用户id获取用户
     * @param userId
     * @return
     */
    User findByUserId(String userId);

    /**
     * 通过手机获取用户
     * @param mobile
     * @return
     */
    User findByMobile(String mobile);

    /**
     * 通过邮件和状态获取用户
     * @param email
     * @return
     */
    User findByEmail(String email);


    /**
     * 多条件分页获取用户
     * @param userReq
     * @param pageBean
     * @param userId
     * @return
     */
    Map findByCondition(UserReq userReq, PageBean pageBean, String userId);
      /**
     * 通过部门id获取
     * @param departmentId
     * @return
     */
    List<User> findByDepartmentId(String departmentId);

    /**
     * 通过userId只查询user信息
     * @param userId
     * @return
     */
    User findUserByUserId(String userId);


    /**
     * 根据用户名查询
     * @param UserName
     * @return
     */
    User findByUserName(String UserName);


    /**
     * 根据用户地点  获取用户角色的部门权限
     * @param userId
     * @param openDataFilter
     * @return List<String>
     */
    List<Department> getDeparmentIdList(String userId,Boolean openDataFilter,String locationId);

    /**
     * 根据用户id 获取用户角色的部门权限
     * @param userId
     * @param openDataFilter
     * @return List<String>
     */
    List<String> getDeparmentIdList(String userId,Boolean openDataFilter,Boolean IsLocation);
    /**
     * 根据用户id 获取用户角色的所属区域
     * @param userId
     * @param openDataFilter
     * @return List<String>
     */
    List<Department> getLocationIdList(String userId,Boolean openDataFilter);

    /**
     * 根据用户id批量删除
     * @param list
     */
    void deleteByIdIn(List<String> list);


    /**
     * 查询部门下是否有用户
     * @param departmentIdList
     * @return
     */
    int countByDepartmentIdIn(List<String> departmentIdList);


    /**
     * 密码修改
     * @param revisePassWordReq
     * @param userId
     * @return
     */
    ReturnCode revisePassWord(RevisePassWordReq revisePassWordReq, String userId);

    /**
     * 密码重置
     * @param resetPassWordReq
     * @return
     */
    ReturnCode resetPassWord(ResetPassWordReq resetPassWordReq);

    void getBelongDept(String departmentId, List<Department> allDept, List<Department> departmentList);
}
