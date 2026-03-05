package com.mes.utils;

import com.mes.entity.User;
import com.mes.mapper.UserMapper;
import com.mes.security.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils {
    @Autowired
    private static UserMapper userMapper;
    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        SecurityUtils.userMapper = userMapper;
    }
    public static Long getUserId() {
        // 获取认证信息
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        // 非空判断 + 类型校验
        if (auth == null || !(auth.getPrincipal() instanceof LoginUser)) {
            throw new RuntimeException("未登录或用户信息异常");
        }
        // 安全获取userId
        return ((LoginUser) auth.getPrincipal()).getUserId();
    }

    public static String getUserName() {
        // 获取认证信息
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        // 非空判断 + 类型校验
        if (auth == null || !(auth.getPrincipal() instanceof LoginUser)) {
            throw new RuntimeException("未登录或用户信息异常");
        }
        // 安全获取Username
        return ((LoginUser) auth.getPrincipal()).getUsername();
    }

    public static String getUserRealName() {
        // 获取用户ID
        Long userId = getUserId();  // 复用已有的getUserId()方法
        // 通过userMapper查询用户信息
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        return user.getRealName();
    }
}