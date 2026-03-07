package com.mes.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mes.dto.properties.SysMenu;

import java.util.List;

public interface SysMenuService extends IService<SysMenu> {
    List<SysMenu> getUserMenuTree(Long userId);

    boolean saveOrUpdateMenu(SysMenu menu);

    boolean deleteMenu(Long menuId);
}
