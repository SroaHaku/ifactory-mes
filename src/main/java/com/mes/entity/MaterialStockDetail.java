package com.mes.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class MaterialStockDetail {
    private Long id;
    private Long materialId;
    private String warehouseName;
    private String location;
    private Integer quantity;
    private Integer lockQuantity;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastUpdateTime;
}
