package com.runonce.dao.system.mapper;

import com.runonce.dao.MyBaseJpaDao;
import com.runonce.dao.MyMapper;
import com.runonce.httpbean.assets.Paging;
import com.runonce.httpbean.assets.reqbean.UserReq;
import com.runonce.model.system.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户数据处理层
 * @author Exrickx
 */
@Mapper
public interface UserMapper extends MyMapper<User> {


    /**
     * 按条件获取用户信息
     * @param userReq
     * @param page
     * @return
     */
    List<User> findInfo(@Param("userReq") UserReq userReq, @Param("page") Paging page,@Param("deptList") List<String> deptList);

    int findInfoCount(@Param("userReq")UserReq userReq,@Param("deptList") List<String> deptList);
}
