package com.mes.service;

import com.mes.entity.SysUserRole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 用户角色关联表 服务类
 * </p>
 *
 * @author xuanyang
 * @since 2026-06-07
 */
public interface ISysUserRoleService extends IService<SysUserRole> {

    List<Long> getAssignedMenuIds(Long roleId);

    boolean assignMenu(Long roleId, List<Long> menuIds);

    boolean deleteRole(Long roleId);

    boolean updateRole(SysUserRole role);

    boolean addRole(SysUserRole role);

    List<SysUserRole> selectRoleList();
}
