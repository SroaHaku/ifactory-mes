package com.mes.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mes.entity.SysMenu;
import com.mes.mapper.SysMenuMapper;
import com.mes.service.SysMenuService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜单服务实现类
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    @Override
    public List<SysMenu> selectMenuList() {
        LambdaQueryWrapper<SysMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysMenu::getStatus, 1)
                .orderByAsc(SysMenu::getSort);
        return list(queryWrapper);
    }

    @Override
    public List<SysMenu> selectMenuListByUserId(Long userId) {
        // 管理员查询所有菜单，普通用户查询权限内菜单
        if (1L == userId) { // 假设ID=1是管理员
            return selectMenuList();
        }
        return baseMapper.selectMenuListByUserId(userId);
    }

    @Override
    public List<SysMenu> buildMenuTree(List<SysMenu> menuList) {
        // 构建树形结构
        List<SysMenu> treeList = new ArrayList<>();
        for (SysMenu menu : menuList) {
            // 根节点
            if (0 == menu.getParentId()) {
                treeList.add(findChildren(menu, menuList));
            }
        }
        return treeList;
    }

    /**
     * 递归查找子菜单
     */
    private SysMenu findChildren(SysMenu parentMenu, List<SysMenu> menuList) {
        for (SysMenu menu : menuList) {
            if (parentMenu.getId().equals(menu.getParentId())) {
                if (parentMenu.getChildren() == null) {
                    parentMenu.setChildren(new ArrayList<>());
                }
                parentMenu.getChildren().add(findChildren(menu, menuList));
            }
        }
        return parentMenu;
    }
}