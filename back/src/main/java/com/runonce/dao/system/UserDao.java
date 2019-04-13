package com.runonce.dao.system;

import com.runonce.dao.MyBaseJpaDao;
import com.runonce.model.system.User;

import java.util.List;

/**
 * 用户数据处理层
 * @author Exrickx
 */
public interface UserDao extends MyBaseJpaDao<User,String> {

    /**
     * 通过用户名获取用户
     * @param username
     * @return
     */
    List<User> findByUserNameAndDelFlag(String username,int delFlag);

    /**
     * 通过手机获取用户
     * @param mobile
     * @return
     */
    List<User> findByMobile(String mobile);

    /**
     * 通过邮件获取用户
     * @param email
     * @return
     */
    List<User> findByEmail(String email);

    /**
     * 通过部门id获取
     * @param departmentId
     * @return
     */
    List<User> findByDepartmentIdAndDelFlag(String departmentId,Integer delFlag);


    /**
     * 批量删除
     * @param list
     */
    void deleteByIdIn(List<String> list);

    /**
     * 查询部门存在人员
     * @param departmentIdList
     * @param delFlag
     * @return
     */
    int  countByDepartmentIdInAndDelFlag(List<String> departmentIdList,Integer delFlag);
}
