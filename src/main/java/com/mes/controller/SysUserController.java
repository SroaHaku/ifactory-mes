package com.mes.controller;

import com.mes.dto.SysLoginDTO;
import com.mes.dto.response.Result;
import com.mes.entity.SysUser;
import com.mes.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 用户控制器
 */
@RestController
@RequestMapping("/sys/user")
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;

    /**
     * 用户登录（修复：用@RequestBody接收JSON参数，添加参数校验）
     */
    @PostMapping("/login")
    public Map<String, Object> login(@Validated @RequestBody SysLoginDTO loginDTO) {
        // 调用服务层，传递DTO中的参数
        return sysUserService.login(loginDTO.getUsername(), loginDTO.getPassword());
    }

    @PostMapping("/logout")
    public Result<Void> logout() {
        try {
            boolean success = sysUserService.logout();
            if (success) {
                return Result.success();
            } else {
                return Result.fail(500, "登出失败！");
            }
        } catch (Exception e) {
            return Result.fail(500, "登出异常：" + e.getMessage());
        }
    }

    /**
     * 查询用户详情
     */
    @GetMapping("/{id}")
    public SysUser getUserById(@PathVariable Long id) {
        return sysUserService.getById(id);
    }

    /**
     * 新增用户
     */
    @PostMapping
    public boolean saveUser(@RequestBody SysUser sysUser) {
        return sysUserService.saveUser(sysUser);
    }

    /**
     * 修改用户
     */
    @PutMapping
    public boolean updateUser(@RequestBody SysUser sysUser) {
        return sysUserService.updateUser(sysUser);
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/{id}")
    public boolean removeUser(@PathVariable Long id) {
        return sysUserService.removeUserById(id);
    }
}