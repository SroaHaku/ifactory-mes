package com.mes.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mes.entity.SysUser;

import java.util.Map;

/**
 * 用户服务接口
 */
public interface SysUserService extends IService<SysUser> {
    /**
     * 用户登录
     */
    Map<String, Object> login(String username, String password);

    /**
     * 根据用户名查询用户
     */
    SysUser getUserByUsername(String username);

    /**
     * 新增用户
     */
    boolean saveUser(SysUser sysUser);

    /**
     * 修改用户
     */
    boolean updateUser(SysUser sysUser);

    /**
     * 删除用户
     */
    boolean removeUserById(Long id);

    boolean logout();
}