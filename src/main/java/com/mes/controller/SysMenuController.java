package com.mes.controller;

import com.mes.dto.SysLoginDTO;
import com.mes.dto.response.Result;
import com.mes.entity.SysMenu;
import com.mes.service.SysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜单控制器
 */
@RestController
@RequestMapping("/sys/menu")
public class SysMenuController {

    @Autowired
    private SysMenuService sysMenuService;

    /**
     * 查询所有菜单列表
     */
    @GetMapping("/list")
    public Result<List<SysMenu>> getMenuList() {
        List<SysMenu> menuList = sysMenuService.list();
        return Result.success(menuList);
    }

    @PostMapping("/add")
    public Result<Void> addMenu(@RequestBody SysMenu sysMenu) {
        try {
            boolean success = sysMenuService.addMenu(sysMenu);
            return Result.success();
        }catch (Exception e) {
            return Result.fail(500, e.getMessage());
        }
    }

    /**
     * 3. 编辑菜单
     */
    @PutMapping("/update")
    public Result<Void> updateMenu(@RequestBody SysMenu sysMenu) {
        try {
            if (sysMenu.getId() == null) {
                return Result.fail(500, "菜单ID不能为空！");
            }
            boolean success = sysMenuService.updateMenu(sysMenu);
            return Result.success();
        }catch (Exception e) {
            return Result.fail(500, e.getMessage());
        }

    }

    /**
     * 4. 删除菜单
     */
    @DeleteMapping("/delete/{menuId}")
    public Result<Void> deleteMenu(@PathVariable Long menuId) {
        try {
            boolean success = sysMenuService.deleteMenu(menuId);
            if (success) {
                return Result.success();
            } else {
                return Result.fail(500, "删除菜单失败！");
            }
        } catch (RuntimeException e) {
            // 捕获子菜单异常，返回友好提示
            return Result.fail(500, e.getMessage());
        }
    }
}