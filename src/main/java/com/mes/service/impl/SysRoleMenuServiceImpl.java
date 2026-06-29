package com.mes.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mes.entity.SysRoleMenu;
import com.mes.mapper.SysRoleMenuMapper;
import com.mes.service.ISysRoleMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuMapper, SysRoleMenu> implements ISysRoleMenuService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignMenu(Long roleId, List<Long> menuIds) {
        LambdaQueryWrapper<SysRoleMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysRoleMenu::getRoleId, roleId);
        remove(wrapper);

        if (menuIds != null && !menuIds.isEmpty()) {
            List<SysRoleMenu> roleMenus = new ArrayList<>();
            for (Long menuId : menuIds) {
                SysRoleMenu roleMenu = new SysRoleMenu();
                roleMenu.setRoleId(roleId);
                roleMenu.setMenuId(menuId);
                roleMenu.setDelFlag(0);
                roleMenu.setCreateTime(LocalDateTime.now());
                roleMenus.add(roleMenu);
            }
            saveBatch(roleMenus);
        }
    }

    @Override
    public List<Long> getMenuIdsByRoleId(Long roleId) {
        LambdaQueryWrapper<SysRoleMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysRoleMenu::getRoleId, roleId)
                .eq(SysRoleMenu::getDelFlag, 0);
        List<SysRoleMenu> roleMenus = list(wrapper);
        List<Long> menuIds = new ArrayList<>();
        for (SysRoleMenu roleMenu : roleMenus) {
            menuIds.add(roleMenu.getMenuId());
        }
        return menuIds;
    }
}
