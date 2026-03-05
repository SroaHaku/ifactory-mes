package com.mes.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class MaterialHistory {
    private Long id;
    private Long materialId;
    private String operationType;
    private String operationContent;
    private String operator;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date operateTime;
    
    private String remark;
}
