package com.mes.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;

@Data
@TableName("warehouse")
public class Warehouse implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("name")
    private String name; // 仓库名称

    @TableField("address")
    private String address; // 仓库地址（可选）

    @TableField("manager")
    private String manager; // 仓库管理员（可选）

    @TableField("status")
    private Integer status; // 状态：1-启用，0-禁用

    @TableField("create_time")
    private Date createTime;

    @TableField("update_time")
    private Date updateTime;
}
