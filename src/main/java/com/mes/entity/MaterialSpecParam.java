package com.mes.entity;

import lombok.Data;

@Data
public class MaterialSpecParam {
    private Long id;
    private Long materialId;
    private String paramName;
    private String paramValue;
    private String paramUnit;
    private String paramDesc;
    private Integer sort;
}
