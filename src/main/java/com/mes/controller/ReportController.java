package com.mes.controller;

import com.mes.dto.request.ReportQueryRequest;
import com.mes.dto.response.EquipmentResponse;
import com.mes.dto.response.Result;
import com.mes.dto.response.ReportResponse;
import com.mes.service.ReportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/report")
@Api(tags = "报表管理")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/list")
    @ApiOperation("获取报表列表")
    public Result<List<ReportResponse>> getReportList(
            @ApiParam("报表类型") @RequestParam(value = "reportType", required = false) String reportType,
            @ApiParam("生成人") @RequestParam(value = "generateBy", required = false) String generateBy,
            @ApiParam("开始时间") @RequestParam(value = "startDate", required = false)  @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @ApiParam("结束时间") @RequestParam(value = "endDate", required = false)  @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
            @ApiParam("页码") @RequestParam(defaultValue = "1") int pageNum,
            @ApiParam("每页数量") @RequestParam(defaultValue = "10") int pageSize) {
        List<ReportResponse> reports = reportService.getReportList(reportType, generateBy, startDate,endDate,pageNum, pageSize);
        return Result.success(reports);
    }

    @GetMapping("/{id}")
    @ApiOperation("获取报表详情")
    public Result<ReportResponse> getReportDetail(@ApiParam("报表ID") @PathVariable Long id) {
        ReportResponse report = reportService.getReportDetail(id);
        return Result.success(report);
    }

    @PostMapping("/generate")
    @ApiOperation("生成报表")
    public Result<ReportResponse> generateReport(@RequestBody ReportQueryRequest request) {
        ReportResponse report = reportService.generateReport(request);
        return Result.success(report);
    }

    @GetMapping("/export/{id}")
    @ApiOperation("导出报表")
    public void exportReport(
            @ApiParam("报表ID") @PathVariable Long id,
            @ApiParam("导出格式") @RequestParam(defaultValue = "excel") String format,
            HttpServletResponse response) {
        reportService.exportReport(id, format, response);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除报表")
    public Result<Boolean> deleteReport(@ApiParam("报表ID") @PathVariable Long id) {
        boolean result = reportService.deleteReport(id);
        return Result.success(result);
    }

    @GetMapping("/equipment")
    @ApiOperation("设备报表")
    public Result<List<EquipmentResponse>> getEquipmentReportList(
            @ApiParam("设备名称") @RequestParam(value = "equipmentName", required = false) String equipmentName,
            @ApiParam("更新人") @RequestParam(value = "updateUser", required = false) String updateUser,
            @ApiParam("开始时间") @RequestParam(value = "startDate", required = false)  @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @ApiParam("结束时间") @RequestParam(value = "endDate", required = false)  @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
            @ApiParam("页码") @RequestParam(defaultValue = "1") int pageNum,
            @ApiParam("每页数量") @RequestParam(defaultValue = "10") int pageSize) {
        List<EquipmentResponse> reports = reportService.getEquipmentReportList(equipmentName, updateUser, startDate,endDate,pageNum, pageSize);
        return Result.success(reports);
    }
}
