package com.mes.service;

import com.mes.entity.SysRoleMenu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface ISysRoleMenuService extends IService<SysRoleMenu> {

    void assignMenu(Long roleId, List<Long> menuIds);

    List<Long> getMenuIdsByRoleId(Long roleId);
}
