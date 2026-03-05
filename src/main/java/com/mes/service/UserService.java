package com.mes.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
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
import org.springframework.security.authentication.AuthenticationManager;

import java.util.List;
import java.util.Map;

public interface UserService extends IService<User> {
    /**
     * 用户登录
     */
    LoginResponse login(LoginRequest loginRequest, AuthenticationManager authenticationManager);

    /**
     * 用户注册
     */
    UserResponse register(RegisterRequest registerRequest);

    /**
     * 根据用户名查询用户
     */
    User findByUsername(String username);

    /**
     * 获取当前登录用户信息
     */
    UserResponse getCurrentUser();

    //登出
    void logout();

    //查询用户关联的角色
    List<String> getRolesByUsername(String username);

    //逻辑删除
    void updateUserStatus(Integer id);

    //更新状态
    void updateUserStatusById(Integer id, Integer status);

    //新增用户
    void addUser(UserPO userPO) throws BusinessException;

    //用户删除
    void deleteUserById(Integer id);

    //查询用户
    IPage<User> allList(UserDTO userDTO);

    //根据id查询用户
    UserPO getUserById(Integer id);

    List<Role> getAllRoles();

    boolean updateUser(UserPO userPO);
}
