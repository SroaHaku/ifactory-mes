package com.mes.dto.properties;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.util.Date;
import java.util.List;

/**
 * 系统菜单实体类（适配你的数据库表结构）
 */
@Data
@TableName("sys_menu")
public class SysMenu {
    /** 菜单ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 父菜单ID（默认0） */
    private Long parentId = 0L;

    /** 菜单名称 */
    private String menuName;

    /** 路由路径 */
    private String path;

    /** 组件路径 */
    private String component;

    /** 图标 */
    private String icon;

    /** 排序（默认0） */
    private Integer sort = 0;

    /** 类型(1:菜单,2:按钮) */
    private Integer type;

    /** 权限标识 */
    private String permission;

    /** 是否可见(1:是,0:否，默认1) */
    private Integer visible = 1;

    /** 创建时间（默认当前时间） */
    private Date createTime;

    /** 更新时间（默认当前时间，更新时自动刷新） */
    private Date updateTime;

    // 非数据库字段：子菜单列表（用于树形结构）
    private List<SysMenu> children;
}