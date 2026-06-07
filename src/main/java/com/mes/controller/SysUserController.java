package com.mes.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
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
    @GetMapping("/list")
    public Result<IPage<SysUser>> getUserList(int currentPage, int pageSize) {
        IPage<SysUser> page = sysUserService.getUserList(currentPage, pageSize);
        return Result.success(page);
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
    public Result<?> saveUser(@RequestBody SysUser sysUser) {
        try {
            sysUserService.saveUser(sysUser);
            return Result.success();
        }catch (Exception e) {
            return Result.fail(500,e.getMessage());
        }
    }

    /**
     * 修改用户
     */
    @PutMapping
    public Result<?> updateUser(@RequestBody SysUser sysUser) {
        sysUserService.updateUser(sysUser);
        return Result.success();
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/{id}")
    public Result<?> removeUser(@PathVariable Long id) {
        sysUserService.removeUserById(id);
        return Result.success();
    }
}