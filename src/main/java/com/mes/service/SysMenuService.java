package com.mes.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mes.entity.SysMenu;
import com.mes.entity.dto.SysMenuDTO;

import java.util.List;

/**
 * 菜单服务接口
 */
public interface SysMenuService extends IService<SysMenu> {
    /**
     * 查询所有菜单列表
     */
    List<SysMenu> selectMenuList();

    /**
     * 根据用户ID查询菜单列表
     */
    List<SysMenu> selectMenuListByUserId(Long userId);

    boolean addMenu(SysMenu sysMenu);

    boolean updateMenu(SysMenu sysMenu);

    boolean deleteMenu(Long menuId);

    List<SysMenuDTO> getMenuList();
}