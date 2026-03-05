package com.mes.entity.VO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class MaterialVO {
    private Long id;
    private String materialCode;
    private String materialName;
    private Long categoryId;
    private String categoryName; // 分类名称
    private String specification;
    private String unit;
    private String brand;
    private Long supplierId;
    private String supplierName;
    private BigDecimal purchasePrice;
    private Integer stockQuantity;
    private Integer minStock;
    private String status; // active-在用，inactive-停用
    private Boolean isCritical; // 是否关键物料
    private Boolean stockAlert; // 是否库存预警
    private String imageUrl;
    private String description;
    private String createBy;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    
    private String updateBy;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
    
    // 规格参数列表
    private List<MaterialSpecParamVO> specParams;
    
    // 库存详情列表
    private List<MaterialStockDetailVO> stockDetails;
}
