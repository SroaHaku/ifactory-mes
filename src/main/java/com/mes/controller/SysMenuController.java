package com.mes.controller;

import com.mes.dto.properties.SysMenu;
import com.mes.dto.response.Result;
import com.mes.service.SysMenuService;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;

/**
 * 菜单管理控制器（适配你的表结构）
 */
@RestController
@RequestMapping("/mes/system/menu")
public class SysMenuController {

    @Resource
    private SysMenuService menuService;

    /**
     * 获取当前用户的菜单树
     */
    @GetMapping("/userMenuTree")
    public Result<List<SysMenu>> getUserMenuTree() {
        // 实际项目中从登录上下文获取用户ID（示例用1L）
        Long userId = 1L;
        List<SysMenu> menuTree = menuService.getUserMenuTree(userId);
        return Result.success(menuTree);
    }

    /**
     * 新增/修改菜单
     */
    @PostMapping("/saveOrUpdate")
    public Result<Boolean> saveOrUpdateMenu(@RequestBody SysMenu menu) {
        boolean result = menuService.saveOrUpdateMenu(menu);
        return Result.success(result);
    }

    /**
     * 删除菜单
     */
    @DeleteMapping("/{menuId}")
    public Result<Boolean> deleteMenu(@PathVariable Long menuId) {
        boolean result = menuService.deleteMenu(menuId);
        return Result.success(result);
    }

    /**
     * 查询所有菜单（用于菜单管理页面）
     */
    @GetMapping("/listAll")
    public Result<List<SysMenu>> listAllMenu() {
        List<SysMenu> menuList = menuService.list();
        return Result.success(menuList);
    }
}
