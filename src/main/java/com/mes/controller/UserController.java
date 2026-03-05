package com.mes.controller;

import com.mes.dto.request.LoginRequest;
import com.mes.dto.request.RegisterRequest;
import com.mes.dto.response.LoginResponse;
import com.mes.dto.response.Result;
import com.mes.dto.response.UserResponse;
import com.mes.entity.PO.UserPO;
import com.mes.entity.User;
import com.mes.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Result<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        LoginResponse response = userService.login(loginRequest, authenticationManager);
        return Result.success(response);
    }

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public Result<UserResponse> register(@Validated @RequestBody RegisterRequest registerRequest) {
        UserResponse userResponse = userService.register(registerRequest);
        return Result.success(userResponse);
    }

    /**
     * 获取当前登录用户信息
     */
    @GetMapping("/currentUser")
    public Result<UserResponse> getCurrentUser() {
        UserResponse userResponse = userService.getCurrentUser();
        return Result.success(userResponse);
    }
    /**
     * 用户登出
     */
    @PostMapping("/logout")
    public Result<Void> logout() {
        userService.logout();
        return Result.success();
    }

    /**
     * 删除用户 逻辑删除
     */
    @PostMapping("/{id}/status")
    public Result<UserResponse> updateUserStatus(@RequestParam Integer id) {
        userService.updateUserStatus(id);
        return Result.success();
    }


}
