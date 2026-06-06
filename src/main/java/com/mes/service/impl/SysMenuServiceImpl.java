package com.mes.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mes.entity.SysMenu;
import com.mes.exception.BusinessException;
import com.mes.mapper.SysMenuMapper;
import com.mes.service.SysMenuService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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

    /**
     * 新增菜单
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addMenu(SysMenu menu) {
        // 设置默认值
        menu.setDelFlag(0); // 未删除
        menu.setCreateTime(new Date());
        menu.setUpdateTime(new Date());
        // 保存菜单
        return save(menu);
    }

    /**
     * 编辑菜单
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateMenu(SysMenu menu) {
        // 补充更新时间
        menu.setUpdateTime(new Date());
        // 更新菜单（MyBatis-Plus会根据id更新）
        return updateById(menu);
    }

    /**
     * 删除菜单（逻辑删除）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteMenu(Long menuId) {
        QueryWrapper<SysMenu> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id", menuId);
        wrapper.eq("del_flag", 0);
        List<SysMenu> sysMenus = baseMapper.selectList(wrapper);
        if (!sysMenus.isEmpty()) {
            throw new BusinessException("该菜单包含子菜单，无法删除！");
        }
        return this.removeById(menuId);
    }
}