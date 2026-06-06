package com.mes.service.impl;

import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mes.entity.SysUser;
import com.mes.mapper.SysUserMapper;
import com.mes.service.SysUserService;
import com.mes.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户服务实现类
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public Map<String, Object> login(String username, String password) {
        Map<String, Object> result = new HashMap<>();

        // 1. 校验参数
        if (!StringUtils.hasText(username) || !StringUtils.hasText(password)) {
            result.put("code", 500);
            result.put("msg", "用户名或密码不能为空");
            return result;
        }

        // 2. 查询用户
        SysUser user = baseMapper.selectUserByUsername(username);
        if (user == null) {
            result.put("code", 500);
            result.put("msg", "用户不存在");
            return result;
        }

        // 3. 校验密码（密码加密存储，这里用MD5示例）
        String encryptPwd = DigestUtil.md5Hex(password);
        if (!encryptPwd.equals(user.getPassword())) {
            result.put("code", 500);
            result.put("msg", "密码错误");
            return result;
        }

        // 4. 校验状态
        if (0 == user.getStatus()) {
            result.put("code", 500);
            result.put("msg", "用户已禁用");
            return result;
        }

        // ========== 核心修复：调用实例方法 ==========
        String token = jwtUtil.generateToken(user.getId().toString(), username);

        // 6. 返回结果
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
    public boolean saveUser(SysUser sysUser) {
        // 密码加密
        sysUser.setPassword(DigestUtil.md5Hex(sysUser.getPassword()));
        return save(sysUser);
    }

    @Override
    public boolean updateUser(SysUser sysUser) {
        // 如果修改密码，需要重新加密
        if (StringUtils.hasText(sysUser.getPassword())) {
            sysUser.setPassword(DigestUtil.md5Hex(sysUser.getPassword()));
        } else {
            // 不修改密码则清空密码字段，避免覆盖
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
}