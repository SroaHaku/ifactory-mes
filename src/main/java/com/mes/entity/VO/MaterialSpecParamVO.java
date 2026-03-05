package com.mes.entity.VO;

import lombok.Data;

@Data
public class MaterialSpecParamVO {
    private Long id;
    private String paramName;
    private String paramValue;
    private String paramUnit;
    private String paramDesc;
    private Integer sort;
}
