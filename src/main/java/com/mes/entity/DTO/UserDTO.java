package com.mes.entity.DTO;

import lombok.Data;

@Data
public class UserDTO {
    private String username;
    private Integer status;
    private Integer pageNum;
    private Integer pageSize;
}
