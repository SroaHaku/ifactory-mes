package com.mes.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@TableName("material") // 对应数据库表名
public class Material {

    @TableId(type = IdType.AUTO) // 自增主键
    private Long id;

    @TableField("material_code") // 对应数据库字段名
    private String materialCode;

    @TableField("material_name")
    private String materialName;

    @TableField("category_id")
    private Long categoryId;

    @TableField("specification")
    private String specification;

    @TableField("unit")
    private String unit;

    @TableField("brand")
    private String brand;

    @TableField("purchase_price")
    private BigDecimal purchasePrice;

    @TableField("stock_quantity")
    private Integer stockQuantity;

    @TableField("min_stock")
    private Integer minStock;

    @TableField("status")
    private String status;

    @TableField("is_critical")
    private Integer isCritical; // 0-否，1-是

    @TableField("stock_alert")
    private Integer stockAlert; // 0-否，1-是

    @TableField("image_url")
    private String imageUrl;

    @TableField("description")
    private String description;

    @TableField("create_by")
    private String createBy;

    @TableField("warehouse_id")
    private Long warehouseId;

    @TableField(value = "create_time", fill = com.baomidou.mybatisplus.annotation.FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @TableField("update_by")
    private String updateBy;

    @TableField(value = "update_time", fill = com.baomidou.mybatisplus.annotation.FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    // 数据库表中不存在的字段，用于VO转换
    @TableField(exist = false)
    private String categoryName;

    // 新增：关联的供应商ID列表（用于前端传递多选的供应商ID）
    @TableField(exist = false)
    private List<Long> supplierIds;
}
