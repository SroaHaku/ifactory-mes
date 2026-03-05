package com.mes.dto.response;

import lombok.Data;
import java.util.Date;
import java.util.List;

@Data
public class UserResponse {
    private Long id;
    private String username;
    private String realName;
    private String email;
    private String phone;
    private Integer status;
    private Date createTime;
    private Date lastLoginTime;
    private List<String> roles;
}
