package com.runonce.util;

import com.runonce.model.system.Role;
import com.runonce.util.constant.CommonConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Exrickx
 */
@Component
public class SecurityUtil {



    @Autowired
    private StringRedisTemplate redisTemplate;

    public String getToken(String userId, Boolean saveLogin){

        String webToken = WebTokenUtil.getWebToken("userId", userId);
        return webToken;
    }

    /**
     * 获取当前登录用户
     * @return
     */
//    public User getCurrUser(){
//
////        UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
////        return userService.findByUsername(user.getUsername());
//    }

//    /**
//     * 获取当前用户数据权限 null代表具有所有权限
//     */
//    public List<String> getDeparmentIds(HttpServletRequest request){
//
//        List<String> deparmentIds = new ArrayList<>();
//       // User u = getCurrUser();
//        String userId=WebTokenUtil.getUserByWebToken(request);
//        // 用户角色
//        List<Role> userRoleList = iUserRoleService.findByUserId(userId);
//        // 判断有无全部数据的角色
//       Boolean flagAll = false;
//        for(Role r : userRoleList){
//            if(r.getDataType()==null||r.getDataType().equals(CommonConstant.DATA_TYPE_ALL)){
//                flagAll = true;
//                break;
//            }
//        }
//        if(flagAll){
//           return null;
//        }
//        // 查找自定义
//       return iUserRoleService.findDepIdsByUserId(userId);
//    }
}
