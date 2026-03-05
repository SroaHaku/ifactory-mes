package com.mes.controller;

import cn.hutool.json.JSONObject;
import com.mes.dto.response.Result;
import com.mes.service.DashboardService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/dashboard")
@Api(tags = "看板管理")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/summary")
    @ApiOperation("获取生产汇总数据")
    public Result<Map<String, Object>> getProductionSummary() {
        JSONObject summaryData = dashboardService.getProductionSummary();
        return Result.success(summaryData);
    }

    @GetMapping("/equipment/status")
    @ApiOperation("获取设备状态数据")
    public Result<Map<String, Object>> getEquipmentStatus() {
        Map<String, Object> statusData = dashboardService.getEquipmentStatus();
        return Result.success(statusData);
    }

    @GetMapping("/quality/statistics")
    @ApiOperation("获取质量统计数据")
    public Result<Map<String, Object>> getQualityStatistics() {
        Map<String, Object> qualityData = dashboardService.getQualityStatistics();
        return Result.success(qualityData);
    }
}
