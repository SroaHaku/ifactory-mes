package com.mes.entity.VO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class MaterialStockDetailVO {
    private Long id;
    private String warehouseName;
    private String location;
    private Integer quantity;
    private Integer lockQuantity;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastUpdateTime;
}
