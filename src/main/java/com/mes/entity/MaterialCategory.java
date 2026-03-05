package com.mes.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 物料分类表 Entity
 * 对应数据库表：material_category
 */
@Data // Lombok 注解，自动生成 getter/setter、toString、equals 等方法（需引入 Lombok 依赖）
@TableName("material_category") // 指定对应数据库表名（若类名与表名一致可省略，此处显式声明更规范）
public class MaterialCategory {

    /**
     * 分类ID（主键，自增）
     * 对应表字段：id
     */
    @TableId(type = IdType.AUTO) // 主键策略：自增（与表结构 auto_increment 匹配）
    private Long id;

    /**
     * 分类名称（非空）
     * 对应表字段：name
     */
    @TableField("name") // 若字段名与属性名一致可省略，此处显式声明便于维护
    private String name;

    /**
     * 父分类ID（默认值 0，代表顶级分类）
     * 对应表字段：parent_id
     */
    @TableField(value = "parent_id", fill = FieldFill.INSERT) // 插入时填充默认值
    private Long parentId = 0L; // 显式设置默认值，与表结构 default 0 匹配

    /**
     * 排序（默认值 0）
     * 对应表字段：sort
     */
    @TableField(value = "sort", fill = FieldFill.INSERT)
    private Integer sort = 0; // 显式设置默认值，与表结构 default 0 匹配

    /**
     * 创建时间（默认值：当前时间）
     * 对应表字段：create_time
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT) // 插入时自动填充时间
    private LocalDateTime createTime;

    /**
     * 更新时间（默认值：当前时间，更新时自动刷新）
     * 对应表字段：update_time
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE) // 插入/更新时自动填充时间
    private LocalDateTime updateTime;
}