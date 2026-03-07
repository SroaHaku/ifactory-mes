package com.mes.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mes.dto.properties.SysMenu;
import com.mes.mapper.SysMenuMapper;
import com.mes.service.SysMenuService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜单管理服务实现类（适配你的表结构）
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    @Resource
    private SysMenuMapper menuMapper;

    /**
     * 获取当前用户的菜单树（仅返回类型为1的菜单，排除按钮；仅返回可见的菜单）
     */
    @Override
    public List<SysMenu> getUserMenuTree(Long userId) {
        // 1. 查询用户有权限的菜单：类型=1（菜单）+ 可见=1（是）
        List<SysMenu> menuList = menuMapper.selectMenuListByUserId(userId);

        // 2. 过滤：仅保留菜单类型（排除按钮）、可见的菜单
        List<SysMenu> validMenuList = menuList.stream()
                .filter(menu -> menu.getType() == 1) // 只保留菜单类型（1=菜单）
                .filter(menu -> menu.getVisible() == 1) // 只保留可见的菜单
                .sorted((m1, m2) -> m1.getSort().compareTo(m2.getSort())) // 按排序升序
                .collect(Collectors.toList());

        // 3. 构建树形结构
        return buildMenuTree(validMenuList, 0L);
    }

    /**
     * 递归构建菜单树
     */
    private List<SysMenu> buildMenuTree(List<SysMenu> menuList, Long parentId) {
        List<SysMenu> treeMenu = new ArrayList<>();

        for (SysMenu menu : menuList) {
            if (parentId.equals(menu.getParentId())) {
                // 递归查询子菜单
                List<SysMenu> children = buildMenuTree(menuList, menu.getId());
                menu.setChildren(children);
                treeMenu.add(menu);
            }
        }
        return treeMenu;
    }

    /**
     * 新增/修改菜单
     */
    @Override
    public boolean saveOrUpdateMenu(SysMenu menu) {
        // 补充时间字段（如果是新增）
        if (menu.getId() == null) {
            menu.setCreateTime(new Date());
        }
        menu.setUpdateTime(new Date()); // 新增/修改都更新时间
        return this.saveOrUpdate(menu);
    }

    /**
     * 删除菜单（级联删除子菜单）
     */
    @Override
    public boolean deleteMenu(Long menuId) {
        // 1. 查询该菜单下的所有子菜单
        QueryWrapper<SysMenu> id = new QueryWrapper<SysMenu>().eq("id", menuId);
        List<SysMenu> childMenuList = menuMapper.selectList(id);

        // 2. 级联删除子菜单
        if (!childMenuList.isEmpty()) {
            List<Long> childIds = childMenuList.stream().map(SysMenu::getId).collect(Collectors.toList());
            this.removeByIds(childIds);
        }

        // 3. 删除当前菜单
        return this.removeById(menuId);
    }
}