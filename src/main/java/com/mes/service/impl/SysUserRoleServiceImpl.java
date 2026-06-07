package com.mes.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mes.entity.SysMenu;
import com.mes.entity.SysUserRole;
import com.mes.mapper.SysUserRoleMapper;
import com.mes.service.ISysUserRoleService;
import com.mes.service.SysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户角色关联表 服务实现类
 *
 * @author xuanyang
 * @since 2026-06-07
 */
@Service
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements ISysUserRoleService {

    @Autowired
    private SysMenuService sysMenuService;

    @Override
    public List<Long> getAssignedMenuIds(Long roleId) {
        return Collections.emptyList();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean assignMenu(Long roleId, List<Long> menuIds) {
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteRole(Long roleId) {
        LambdaQueryWrapper<SysUserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUserRole::getRoleId, roleId);
        return remove(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateRole(SysUserRole role) {
        return updateById(role);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addRole(SysUserRole userRole) {
        userRole.setDelFlag(0);
        userRole.setCreateTime(LocalDateTime.now());
        return save(userRole);
    }

    @Override
    public List<SysUserRole> selectRoleList() {
        List<SysUserRole> allUserRoles = list();
        
        Map<Long, SysUserRole> roleMap = new LinkedHashMap<>();
        for (SysUserRole userRole : allUserRoles) {
            if (userRole.getRoleId() != null && !roleMap.containsKey(userRole.getRoleId())) {
                SysUserRole role = new SysUserRole();
                role.setId(userRole.getRoleId());
                role.setRoleId(userRole.getRoleId());
//                role.setRoleName(userRole.getRoleName());
//                role.setRoleCode(userRole.getRoleCode());
                roleMap.put(userRole.getRoleId(), role);
            }
        }
        
        return new ArrayList<>(roleMap.values());
    }

    /**
     * 根据用户ID查询菜单列表
     */
    public List<SysMenu> getMenuListByUserId(Long userId) {
        return sysMenuService.selectMenuListByUserId(userId);
    }

}