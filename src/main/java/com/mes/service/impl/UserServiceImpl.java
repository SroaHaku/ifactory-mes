package com.mes.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mes.dto.request.LoginRequest;
import com.mes.dto.request.RegisterRequest;
import com.mes.dto.response.LoginResponse;
import com.mes.dto.response.UserResponse;
import com.mes.entity.DTO.UserDTO;
import com.mes.entity.PO.UserPO;
import com.mes.entity.Role;
import com.mes.entity.User;
import com.mes.entity.UserRole;
import com.mes.exception.BusinessException;
import com.mes.mapper.RoleMapper;
import com.mes.mapper.UserMapper;
import com.mes.mapper.UserRoleMapper;
import com.mes.security.JwtTokenProvider;
import com.mes.service.UserService;
import com.mes.utils.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Lazy
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public LoginResponse login(LoginRequest loginRequest, AuthenticationManager authenticationManager) {
        // 1. 增加参数校验，避免空值或空格导致的认证失败
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("用户名不能为空");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("密码不能为空");
        }

        // 去除前后空格（防止前端输入时误加空格）
        username = username.trim();
        password = password.trim();

        try {
            // 2. 认证用户 - 使用处理后的用户名和密码
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 3. 生成令牌
            String jwt = tokenProvider.generateToken(authentication);

            // 4. 更新最后登录时间（建议使用Service层方法而非直接调用mapper）
            User user = findByUsername(username);  // 这里复用处理后的用户名
            user.setLastLoginTime(new Date());
            userMapper.updateById(user);  // 建议改为userService.updateLastLoginTime(user)

            // 5. 构建响应
            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setToken(jwt);

            UserResponse userResponse = new UserResponse();
            BeanUtils.copyProperties(user, userResponse);
            loginResponse.setUser(userResponse);

            return loginResponse;
        } catch (BadCredentialsException e) {
            // 6. 捕获密码错误异常，增加日志便于排查
            log.error("用户登录失败：密码错误",e);
            log.debug("原始密码："+password);
            throw new BusinessException("用户名或密码错误");  // 抛出自定义业务异常，避免暴露具体错误信息
        } catch (Exception e) {
            log.error("用户{}登录异常", e);
            throw new BusinessException("登录失败，请稍后重试");
        }
    }

    @Override
    @Transactional
    public UserResponse register(RegisterRequest registerRequest) {
        // 检查用户名是否已存在
        if (findByUsername(registerRequest.getUsername()) != null) {
            throw new BusinessException("用户名已存在");
        }
        // 创建新用户
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setRealName(registerRequest.getRealName());
        user.setEmail(registerRequest.getEmail());
        user.setPhone(registerRequest.getPhone());
        user.setStatus(1); // 默认为启用状态
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());

        userMapper.insert(user);

        UserResponse userResponse = new UserResponse();
        BeanUtils.copyProperties(user, userResponse);
        return userResponse;
    }

    @Override
    public User findByUsername(String username) {
        return userMapper.findByUsername(username);
    }

    @Override
    public UserResponse getCurrentUser() {
        // 从Security上下文获取当前登录用户
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new BusinessException("未获取到当前登录用户");
        }
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        User user = findByUsername(username);
        UserResponse userResponse = new UserResponse();
        BeanUtils.copyProperties(user, userResponse);
        List<String> roles = getRolesByUsername(username);
        userResponse.setRoles(roles);
        return userResponse;
    }

    @Override
    public void logout() {
        SecurityContextHolder.clearContext();
    }

    @Override
    public List<String> getRolesByUsername(String username) {
        // 1. 先通过用户名获取用户ID
        User user = findByUsername(username);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        // 2. 通过用户ID查询关联的角色标识
        List<String> roleCodes = userRoleMapper.selectRoleCodesByUserId(user.getId());
        // 3. 处理空值：避免返回null，空角色时返回空列表（或默认角色）
        return roleCodes != null ? roleCodes : Collections.emptyList();
    }

    @Override
    public void updateUserStatus(Integer id) {
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        user.setStatus(0);
        userMapper.updateById(user);
    }

    @Override
    public void updateUserStatusById(Integer id, Integer status) {
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        user.setStatus(status);
        userMapper.updateById(user);
    }

    //新增用户
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addUser(UserPO userPO) throws BusinessException {
        String username=userPO.getUsername();
        User u = userMapper.findByUsername(username);
        if (u != null) {
            throw new BusinessException("用户名已经存在");
        }
        User user = new User();
        BeanUtils.copyProperties(userPO, user);
        String password = userPO.getPassword();
        user.setPassword(passwordEncoder.encode(password));
        Date date= new Date();
        user.setCreateTime(date);
        user.setUpdateTime(date);
        user.setLastLoginTime(date);
        userMapper.insert(user);
        Long userId = user.getId();
        List<Long> roleIds = userPO.getRoleIds();
        if (roleIds == null || roleIds.isEmpty()) {
            throw new BusinessException("请至少选择一个角色");
        }
        List<UserRole> userRoles = roleIds.stream()
                .filter(Objects::nonNull)
                .map(roleId -> {
                    UserRole userRole = new UserRole();
                    userRole.setUserId(userId);
                    userRole.setRoleId(roleId);
                    return userRole;
                })
                .collect(Collectors.toList());
        if (!userRoles.isEmpty()) {
            userRoleMapper.batchInsert(userRoles);
        }
    }

    @Override
    public void deleteUserById(Integer id) {
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        userMapper.deleteById(id);
        QueryWrapper<UserRole> wrapper = new QueryWrapper<UserRole>();
        wrapper.in("user_id", user.getId());
        userRoleMapper.delete(wrapper);
    }

    @Override
    public IPage<User> allList(UserDTO userDTO) {
        // 处理默认分页参数
        int pageNum = userDTO.getPageNum() == null ? 1 : userDTO.getPageNum();
        int pageSize = userDTO.getPageSize() == null ? 10 : userDTO.getPageSize();
        // 1. 构建分页对象
        IPage<User> page = new Page<>(pageNum, pageSize);
        // 2. 构建查询条件（根据前端参数过滤）
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        // 用户名模糊查询
        if (userDTO.getUsername() != null && !userDTO.getUsername().isEmpty()) {
            queryWrapper.like("username", userDTO.getUsername());
        }
        // 状态精确查询
        if (userDTO.getStatus() != null) {
            queryWrapper.eq("status", userDTO.getStatus());
        }
        queryWrapper.orderByAsc("id");
        //  查询用户列表
        IPage<User> userPage = userMapper.selectPage(page, queryWrapper); // 注意：MyBatis-Plus 推荐用 selectPage 而非 page
        List<User> userList = userPage.getRecords();

        //  为每个用户查询并设置角色列表
        for (User user : userList) {
            Long userId = user.getId();
            // 调用 UserRoleMapper 查询该用户的角色
            List<String> roles = userRoleMapper.selectRolesByUserId(userId);
            user.setRoles(roles);
        }

        return userPage;
    }

    @Override
    public UserPO getUserById(Integer id) {
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        UserPO userPO = new UserPO();
        BeanUtils.copyProperties(user, userPO);
        userPO.setRoleIds(userRoleMapper.selectRolesIdByUserId(user.getId()));
        return userPO;
    }

    @Override
    public List<Role> getAllRoles() {
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Role::getRoleName); // 按角色名升序
        return roleMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUser(UserPO userPO) {
        Long userId = userPO.getId();
        if (userId == null) {
            throw new IllegalArgumentException("用户ID不能为空");
        }
        boolean isPasswordUpdated = false;
        // 1. 更新用户基本信息
        User user = new User();
        BeanUtils.copyProperties(userPO, user);
        // 处理密码：若传递了新密码则加密，否则不更新密码（避免覆盖原有密码）
        if (user.getPassword() != null && !user.getPassword().trim().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            isPasswordUpdated = true;
        } else {
            // 若密码为空，查询原有密码并设置，避免更新为 null
            User oldUser = baseMapper.selectById(userId);
            if (oldUser != null) {
                user.setPassword(oldUser.getPassword());
            }
            isPasswordUpdated = false;
        }
        Date date = new Date();
        user.setUpdateTime(date);
        baseMapper.updateById(user);

        // 2. 更新用户角色关联：先删除旧关联，再插入新关联
        // 2.1 删除用户原有角色关联
        QueryWrapper<UserRole> deleteWrapper = new QueryWrapper<>();
        deleteWrapper.in("user_id", userId);
        userRoleMapper.delete(deleteWrapper);

        // 2.2 插入新的角色关联
        List<Long> roleIds = userPO.getRoleIds();
        if (roleIds != null && !roleIds.isEmpty()) {
            List<UserRole> userRoleList = new ArrayList<>();
            for (Long roleId : roleIds) {
                UserRole userRole = new UserRole();
                userRole.setUserId(userId);
                userRole.setRoleId(roleId);
                userRoleList.add(userRole);
            }
            userRoleMapper.batchInsert(userRoleList);
        }
        return isPasswordUpdated;
    }
}
