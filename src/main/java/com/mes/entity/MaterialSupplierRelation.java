package com.mes.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MaterialSupplierRelation {
    private Long id;
    private Long materialId;   // 物料ID
    private Long supplierId;   // 供应商ID
    private Integer isMain;    // 是否主供应商（1-是，0-否）
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
