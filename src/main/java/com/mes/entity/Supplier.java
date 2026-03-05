package com.mes.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;

@Data
@TableName("supplier")
public class Supplier implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("name")
    private String name; // 供应商名称

    @TableField("contact_person")
    private String contactPerson; // 联系人（可选）

    @TableField("contact_phone")
    private String contactPhone; // 联系电话（可选）

    @TableField("status")
    private Integer status; // 状态：1-合作中，0-已停用

    @TableField("create_time")
    private Date createTime;

    @TableField("update_time")
    private Date updateTime;
}
