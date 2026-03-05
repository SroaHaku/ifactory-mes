package com.mes.entity.VO;

import lombok.Data;

@Data
public class MaterialQueryVO {
    private String materialCode;
    private String materialName;
    private Long categoryId;
    private String status;
    private String stockStatus; // normal-正常，alert-预警，empty-无库存
    private Integer pageNum = 1;
    private Integer pageSize = 10;
}
