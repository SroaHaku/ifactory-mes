package com.mes.controller;

import com.mes.entity.SysMenu;
import com.mes.service.SysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public List<SysMenu> getMenuList() {
        return sysMenuService.selectMenuList();
    }

    /**
     * 根据用户ID查询菜单列表（树形结构）
     */
    @GetMapping("/tree/{userId}")
    public List<SysMenu> getMenuTreeByUserId(@PathVariable Long userId) {
        List<SysMenu> menuList = sysMenuService.selectMenuListByUserId(userId);
        return sysMenuService.buildMenuTree(menuList);
    }
}