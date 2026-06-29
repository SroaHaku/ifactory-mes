package com.mes.controller;

import com.mes.dto.response.Result;
import com.mes.entity.SysMenu;
import com.mes.entity.SysRoleMenu;
import com.mes.entity.dto.SysMenuDTO;
import com.mes.service.SysMenuService;
import com.mes.service.ISysRoleMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 菜单控制器
 */
@RestController
@RequestMapping("/sys/menu")
public class SysMenuController {

    @Autowired
    private SysMenuService sysMenuService;

    @Autowired
    private ISysRoleMenuService sysRoleMenuService;

    /**
     * 查询所有菜单列表
     */
    @GetMapping("/list")
    public Result<List<SysMenu>> getMenuList() {
        List<SysMenu> menuList = sysMenuService.list();
        return Result.success(menuList);
    }

    /**
     * 查询所有菜单列表
     */
    @GetMapping("/getMenuList")
    public Result<List<SysMenuDTO>> getMenuListCode() {
        List<SysMenuDTO> menuList = sysMenuService.getMenuList();
        return Result.success(menuList);
    }

    /**
     * 根据用户ID查询菜单树
     */
    @GetMapping("/tree/{userId}")
    public Result<List<Map<String, Object>>> getMenuTreeByUserId(@PathVariable Long userId) {
        List<SysMenu> menuList = sysMenuService.selectMenuListByUserId(userId);
        List<Map<String, Object>> tree = buildMenuTree(menuList, 0L);
        return Result.success(tree);
    }

    private List<Map<String, Object>> buildMenuTree(List<SysMenu> menuList, Long parentId) {
        List<Map<String, Object>> tree = new ArrayList<>();
        for (SysMenu menu : menuList) {
            if (parentId.equals(menu.getParentId())) {
                Map<String, Object> node = new HashMap<>();
                node.put("id", menu.getId());
                node.put("parentId", menu.getParentId());
                node.put("menuName", menu.getMenuName());
                node.put("menuType", menu.getMenuType());
                node.put("path", menu.getPath());
                node.put("component", menu.getComponent());
                node.put("perms", menu.getPerms());
                node.put("sort", menu.getSort());
                node.put("status", menu.getStatus());
                
                List<Map<String, Object>> children = buildMenuTree(menuList, menu.getId());
                if (!children.isEmpty()) {
                    node.put("children", children);
                }
                
                tree.add(node);
            }
        }
        return tree;
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
            return Result.fail(500, e.getMessage());
        }
    }

    @PostMapping("/assignMenu/{roleId}")
    public Result<Void> assignRoleMenu(@PathVariable Long roleId, @RequestBody List<Long> menuIds) {
        try {
            sysRoleMenuService.assignMenu(roleId, menuIds);
            return Result.success();
        } catch (Exception e) {
            return Result.fail(500, e.getMessage());
        }
    }

    @GetMapping("/roleMenuIds/{roleId}")
    public Result<List<Long>> getRoleMenuIds(@PathVariable Long roleId) {
        List<Long> menuIds = sysRoleMenuService.getMenuIdsByRoleId(roleId);
        return Result.success(menuIds);
    }
}