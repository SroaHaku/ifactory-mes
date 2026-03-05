package com.mes.security;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mes.entity.User;
import com.mes.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. 根据用户名查询数据库用户
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        User user = userMapper.selectOne(queryWrapper);
        // 2. 用户不存在则抛异常（Spring Security自动处理）
        if (user == null) {
            throw new UsernameNotFoundException("用户名不存在：" + username);
        }
        // 3. 构建用户权限（简化处理，给默认角色）
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_USER");

        // 4. 返回自定义LoginUser（关键：携带userId）
        return new LoginUser(user, Collections.singletonList(authority));
    }
}