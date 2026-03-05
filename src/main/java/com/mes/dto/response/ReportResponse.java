package com.mes.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.Map;

@Data
@ApiModel("报表响应数据")
public class ReportResponse {

    @ApiModelProperty("报表ID")
    private Long id;

    @ApiModelProperty("报表名称")
    private String reportName;

    @ApiModelProperty("报表类型")
    private String reportType;

    @ApiModelProperty("报表生成时间")
    private Date generateTime;

    @ApiModelProperty("报表生成人")
    private String generateBy;

    @ApiModelProperty("报表参数")
    private Map<String, Object> parameters;

    @ApiModelProperty("报表摘要数据")
    private Map<String, Object> summaryData;

    @ApiModelProperty("报表详细数据")
    private Object detailData;

    @ApiModelProperty("报表文件路径")
    private String reportFilePath;
}
