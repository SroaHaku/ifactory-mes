package com.mes.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mes.dto.response.Result;
import com.mes.entity.SysRole;
import com.mes.service.ISysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sys/role")
public class SysRoleController {

    @Autowired
    private ISysRoleService sysRoleService;

    @GetMapping("/list")
    public Result<IPage<SysRole>> getRoleList(Integer currentPage, Integer pageSize) {
        Page<SysRole> page = new Page<>(currentPage, pageSize);
        QueryWrapper<SysRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("del_flag", 0);
        queryWrapper.orderByDesc("create_time");
        IPage<SysRole> rolePage = sysRoleService.page(page, queryWrapper);
        return Result.success(rolePage);
    }

    @GetMapping("/all")
    public Result<List<SysRole>> getAllRoles() {
        QueryWrapper<SysRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("del_flag", 0);
        queryWrapper.eq("status", 1);
        queryWrapper.orderByDesc("create_time");
        List<SysRole> roles = sysRoleService.list(queryWrapper);
        return Result.success(roles);
    }

    @GetMapping("/{id}")
    public Result<SysRole> getRoleById(@PathVariable Long id) {
        SysRole role = sysRoleService.getById(id);
        if (role == null || role.getDelFlag() != 0) {
            return Result.fail(500, "角色不存在");
        }
        return Result.success(role);
    }

    @PostMapping
    public Result<?> saveRole(@RequestBody SysRole sysRole) {
        sysRole.setDelFlag(0);
        sysRoleService.save(sysRole);
        return Result.success();
    }

    @PutMapping
    public Result<?> updateRole(@RequestBody SysRole sysRole) {
        sysRoleService.updateById(sysRole);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<?> removeRole(@PathVariable Long id) {
        SysRole role = sysRoleService.getById(id);
        if (role != null) {
            role.setDelFlag(1);
            sysRoleService.updateById(role);
        }
        return Result.success();
    }
}