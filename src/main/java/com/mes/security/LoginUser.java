package com.mes.security;

import com.mes.entity.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

// 极简版LoginUser，仅保留核心功能
public class LoginUser implements UserDetails {
    // 对外提供获取userId的方法（后续拿ID全靠它）
    @Getter
    private Long userId; // 关键：存储用户ID
    private final User user; // 关联数据库User实体
    private final Collection<? extends GrantedAuthority> authorities;

    // 构造器：初始化userId、user、authorities
    public LoginUser(User user, Collection<? extends GrantedAuthority> authorities) {
        this.userId = user.getId();
        this.user = user;
        this.authorities = authorities;
    }

    // ------------------------------
    // 实现UserDetails的必要方法（按规则返回即可）
    // ------------------------------
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword(); // 返回数据库中的加密密码
    }

    @Override
    public String getUsername() {
        return user.getUsername(); // 返回登录用户名
    }

    // 以下4个方法默认返回true（简化处理，无特殊账号状态控制时够用）
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.getStatus() == 1; // 关联User的status字段（1=启用）
    }
}