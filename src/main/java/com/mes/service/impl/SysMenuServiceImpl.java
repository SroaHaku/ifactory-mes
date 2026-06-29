package com.mes.service.impl;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.mes.entity.SysMenu;
import com.mes.entity.dto.SysMenuDTO;
import com.mes.exception.BusinessException;
import com.mes.mapper.SysMenuMapper;
import com.mes.service.SysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 菜单服务实现类
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {
    @Autowired
    private SysMenuMapper sysMenuMapper;

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
        menu.setCreateTime(LocalDateTime.now());
        menu.setUpdateTime(LocalDateTime.now());
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
        menu.setUpdateTime(LocalDateTime.now());
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

    @Override
    public List<SysMenuDTO> getMenuList() {
        // 1. 查询所有正常、未删除的菜单（扁平列表）
        LambdaQueryWrapper<SysMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysMenu::getDelFlag, 0)
                .eq(SysMenu::getStatus, 1)
                .orderByAsc(SysMenu::getSort);
        List<SysMenu> menuList = sysMenuMapper.selectList(wrapper);

        // 2. 实体转 DTO
        List<SysMenuDTO> dtoList = menuList.stream().map(menu -> {
            SysMenuDTO dto = new SysMenuDTO();
            dto.setId(menu.getId())
                    .setParentId(menu.getParentId())
                    .setMenuName(menu.getMenuName())
                    .setMenuType(menu.getMenuType())
                    .setPath(menu.getPath())
                    .setComponent(menu.getComponent())
                    .setPerms(menu.getPerms())
                    .setSort(menu.getSort())
                    .setStatus(menu.getStatus())
                    .setCreateTime(menu.getCreateTime())
                    .setUpdateTime(menu.getUpdateTime())
                    .setDelFlag(menu.getDelFlag());
            return dto;
        }).collect(Collectors.toList());

        // 3. 构建树形结构
        return buildMenuTree(dtoList);
    }

    /**
     * 递归构建菜单树
     * @param allMenu 全量扁平菜单数据
     * @return 树形菜单
     */
    private List<SysMenuDTO> buildMenuTree(List<SysMenuDTO> allMenu) {
        // 根节点：parentId = 0
        List<SysMenuDTO> rootList = allMenu.stream()
                .filter(menu -> menu.getParentId() == 0)
                .collect(Collectors.toList());

        // 遍历根节点，递归挂载子节点
        rootList.forEach(root -> {
            root.setChildren(getChildList(root.getId(), allMenu));
        });
        return rootList;
    }

    /**
     * 递归查询子菜单
     * @param parentId 父ID
     * @param allMenu 全量数据
     * @return 子节点集合
     */
    private List<SysMenuDTO> getChildList(Long parentId, List<SysMenuDTO> allMenu) {
        // 找到当前父ID的所有直接子节点
        List<SysMenuDTO> childList = allMenu.stream()
                .filter(menu -> parentId.equals(menu.getParentId()))
                .collect(Collectors.toList());

        // 递归继续找下级
        childList.forEach(child -> {
            child.setChildren(getChildList(child.getId(), allMenu));
        });
        return childList;
    }
}