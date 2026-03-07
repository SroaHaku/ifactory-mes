package com.mes.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 菜单实体类
 */
@Data
@TableName("sys_menu")
public class SysMenu implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 父菜单ID 0表示根菜单 */
    private Long parentId;

    /** 菜单名称 */
    private String menuName;

    /** 菜单类型 0-目录 1-菜单 2-按钮 */
    private Integer menuType;

    /** 路由地址 */
    private String path;

    /** 组件路径 */
    private String component;

    /** 权限标识 */
    private String perms;

    /** 排序 */
    private Integer sort;

    /** 状态 0-禁用 1-正常 */
    private Integer status;

    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /** 更新时间 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /** 逻辑删除 0-未删除 1-已删除 */
    @TableLogic
    private Integer delFlag;

    // ========== 新增 children 字段 ==========
    /** 子菜单列表（非数据库字段） */
    @TableField(exist = false) // 标记为非数据库字段，避免MyBatisPlus映射
    private List<SysMenu> children;
}