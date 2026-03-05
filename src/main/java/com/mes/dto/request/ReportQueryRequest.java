package com.mes.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel("报表查询请求参数")
public class ReportQueryRequest {

    @ApiModelProperty("报表类型：production-生产报表，quality-质量报表，equipment-设备报表")
    private String reportType;

    @ApiModelProperty("开始时间")
    private Date startTime;

    @ApiModelProperty("结束时间")
    private Date endTime;

    @ApiModelProperty("生产线ID")
    private Long productionLineId;

    @ApiModelProperty("设备ID")
    private Long equipmentId;

    @ApiModelProperty("是否包含详细数据")
    private boolean includeDetails = false;
}
