package com.mes.service.impl;

import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mes.entity.SysUser;
import com.mes.entity.SysUserRole;
import com.mes.exception.BusinessException;
import com.mes.mapper.SysUserMapper;
import com.mes.service.ISysUserRoleService;
import com.mes.service.SysUserService;
import com.mes.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户服务实现类
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private ISysUserRoleService sysUserRoleService;

    @Override
    public Map<String, Object> login(String username, String password) {
        Map<String, Object> result = new HashMap<>();

        if (!StringUtils.hasText(username) || !StringUtils.hasText(password)) {
            result.put("code", 500);
            result.put("msg", "用户名或密码不能为空");
            return result;
        }

        SysUser user = baseMapper.selectUserByUsername(username);
        if (user == null) {
            result.put("code", 500);
            result.put("msg", "用户不存在");
            return result;
        }

        String encryptPwd = DigestUtil.md5Hex(password);
        if (!encryptPwd.equals(user.getPassword())) {
            result.put("code", 500);
            result.put("msg", "密码错误");
            return result;
        }

        if (0 == user.getStatus()) {
            result.put("code", 500);
            result.put("msg", "用户已禁用");
            return result;
        }

        String token = jwtUtil.generateToken(user.getId().toString(), username);

        result.put("code", 200);
        result.put("msg", "登录成功");
        result.put("token", token);
        result.put("user", user);

        return result;
    }

    @Override
    public SysUser getUserByUsername(String username) {
        return baseMapper.selectUserByUsername(username);
    }

    @Override
    public void saveUser(SysUser sysUser) {
        try {
            QueryWrapper<SysUser> wrapper = new QueryWrapper<SysUser>();
            wrapper.eq("username", sysUser.getUsername());
            wrapper.eq("del_flag",0);
            Long count = sysUserMapper.selectCount(wrapper);
            if (count > 0) {
                throw new BusinessException("已经存在相同用户名的用户！");
            }
            sysUser.setPassword(DigestUtil.md5Hex(sysUser.getPassword()));
            save(sysUser);
        }catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
    }

    @Override
    public boolean updateUser(SysUser sysUser) {
        if (StringUtils.hasText(sysUser.getPassword())) {
            sysUser.setPassword(DigestUtil.md5Hex(sysUser.getPassword()));
        } else {
            sysUser.setPassword(null);
        }
        return updateById(sysUser);
    }

    @Override
    public boolean removeUserById(Long id) {
        return removeById(id);
    }

    @Override
    public boolean logout() {
        return false;
    }

    @Override
    public IPage<SysUser> getUserList(Integer currentPage, Integer pageSize) {
        Page<SysUser> page = new Page<>(currentPage, pageSize);
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("create_time");
        return sysUserMapper.selectPage(page, queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean assignRole(Long userId, List<Long> roleIds) {
        LambdaQueryWrapper<SysUserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUserRole::getUserId, userId);
        sysUserRoleService.remove(wrapper);

        if (roleIds != null && !roleIds.isEmpty()) {
            List<SysUserRole> userRoles = new ArrayList<>();
            for (Long roleId : roleIds) {
                SysUserRole userRole = new SysUserRole();
                userRole.setUserId(userId);
                userRole.setRoleId(roleId);
                userRole.setDelFlag(0);
                userRole.setCreateTime(LocalDateTime.now());
                userRoles.add(userRole);
            }
            sysUserRoleService.saveBatch(userRoles);
        }
        return true;
    }

    @Override
    public List<Long> getUserRoleIds(Long userId) {
        LambdaQueryWrapper<SysUserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUserRole::getUserId, userId)
                .eq(SysUserRole::getDelFlag, 0);
        List<SysUserRole> userRoles = sysUserRoleService.list(wrapper);
        List<Long> roleIds = new ArrayList<>();
        for (SysUserRole userRole : userRoles) {
            roleIds.add(userRole.getRoleId());
        }
        return roleIds;
    }
}