package com.mes.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mes.dto.response.Result;
import com.mes.dto.response.UserResponse;
import com.mes.entity.DTO.UserDTO;
import com.mes.entity.PO.UserPO;
import com.mes.entity.Role;
import com.mes.entity.User;
import com.mes.entity.UserRole;
import com.mes.exception.BusinessException;
import com.mes.service.UserService;
import io.swagger.v3.oas.annotations.Parameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserList {

    @Autowired
    private UserService userService;

    //用户列表
    @PostMapping("/allList")
    public Result<IPage<User>> allList(@RequestBody UserDTO userDTO) {
        IPage<User> userIPage = userService.allList(userDTO);
        System.out.println(userIPage);
        return Result.success(userIPage);
    }

    //逻辑删除用户
    @PostMapping("/{id}/status")
    public Result<User> updateStatus(@RequestParam Integer id, @RequestParam Integer status) {
        userService.updateUserStatusById(id,status);
        return Result.success();
    }

    /**
     * 新增用户
     */
    @PostMapping("/addUser")
    public Result<Void> addUser(@RequestBody UserPO userPO) throws BusinessException {
        userService.addUser(userPO);
        return Result.success();
    }

    /**
     * 删除用户
     */
    @PostMapping("/deleteUser")
    public Result<UserResponse> deleteUser(@RequestParam Integer id) {
        userService.deleteUserById(id);
        return Result.success();
    }

    /**
     * 更新用户
     */
    @PostMapping("/update")
    public Result<?> updateUser(@RequestBody UserPO userPO) {
        boolean updated = userService.updateUser(userPO);
        return Result.success(updated);
    }

    //根据id查询用户
    @GetMapping("/{id}")
    public Result<UserPO> getUserById(@PathVariable Integer id) {
        return Result.success(userService.getUserById(id));
    }

    //获取用户权限角色
    @GetMapping("/roles")
    public Result<List<Role>> roles() {
        return Result.success(userService.getAllRoles());
    }
}
