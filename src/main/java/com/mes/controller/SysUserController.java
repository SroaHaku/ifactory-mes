package com.mes.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mes.dto.SysLoginDTO;
import com.mes.dto.response.Result;
import com.mes.entity.SysUser;
import com.mes.entity.SysUserRole;
import com.mes.service.SysUserService;
import com.mes.service.ISysUserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 用户控制器
 */
@RestController
@RequestMapping("/sys/user")
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private ISysUserRoleService sysUserRoleService;

    @PostMapping("/login")
    public Map<String, Object> login(@Validated @RequestBody SysLoginDTO loginDTO) {
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

    @GetMapping("/list")
    public Result<IPage<SysUser>> getUserList(Integer currentPage, Integer pageSize) {
        IPage<SysUser> page = sysUserService.getUserList(currentPage, pageSize);
        return Result.success(page);
    }

    @GetMapping("/{id}")
    public SysUser getUserById(@PathVariable Long id) {
        return sysUserService.getById(id);
    }

    @PostMapping
    public Result<?> saveUser(@RequestBody SysUser sysUser) {
        try {
            sysUserService.saveUser(sysUser);
            return Result.success();
        }catch (Exception e) {
            return Result.fail(500,e.getMessage());
        }
    }

    @PutMapping
    public Result<?> updateUser(@RequestBody SysUser sysUser) {
        sysUserService.updateUser(sysUser);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<?> removeUser(@PathVariable Long id) {
        sysUserService.removeUserById(id);
        return Result.success();
    }

    @PostMapping("/assignRole/{userId}")
    public Result<Void> assignRole(@PathVariable Long userId, @RequestBody List<Long> roleIds) {
        try {
            sysUserService.assignRole(userId, roleIds);
            return Result.success();
        } catch (Exception e) {
            return Result.fail(500, e.getMessage());
        }
    }

    @GetMapping("/roleIds/{userId}")
    public Result<List<Long>> getUserRoleIds(@PathVariable Long userId) {
        List<Long> roleIds = sysUserService.getUserRoleIds(userId);
        return Result.success(roleIds);
    }
}