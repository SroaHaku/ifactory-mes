package com.mes.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mes.dto.request.ReportQueryRequest;
import com.mes.dto.response.EquipmentResponse;
import com.mes.dto.response.ReportResponse;
import com.mes.entity.Report;
import com.mes.entity.User;
import com.mes.mapper.EquipmentResponseMapper;
import com.mes.mapper.ReportMapper;
import com.mes.mapper.UserMapper;
import com.mes.service.ReportService;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import com.mes.utils.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ReportServiceImpl extends ServiceImpl<ReportMapper, Report> implements ReportService{

    @Autowired
    private ReportMapper reportMapper;

    @Value("${file.upload-path}/reports/")
    private String reportPath;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private EquipmentResponseMapper equipmentResponseMapper;

    @Override
    public List<ReportResponse> getReportList(String type, String generateBy, Date startDate,Date endDate, int page, int size) {
        Page<Report> pagination = new Page<>(page, size);
        QueryWrapper<Report> queryWrapper = new QueryWrapper<>();
        // 如果指定了报表类型，则添加查询条件
        if (type != null && !type.trim().isEmpty()) {
            queryWrapper.eq("report_type", type);
        }
        // 如果指定了生成人，则添加查询条件
        if (generateBy != null && !generateBy.trim().isEmpty()) {
            String username = userMapper.selectById(generateBy).getRealName();
            queryWrapper.eq("generate_by", username);
        }
        // 如果指定了时间，则添加查询条件
        if (startDate != null && endDate!= null) {
            if (startDate.after(endDate)) {
                // 交换日期
                Date temp = startDate;
                startDate = endDate;
                endDate = temp;
            }
            queryWrapper.ge("generate_time", startDate);
            queryWrapper.le("generate_time", endDate);
        }else if (startDate != null) {
            // 只有开始日期
            queryWrapper.ge("generate_time", startDate);
        } else if (endDate != null) {
            // 只有结束日期
            queryWrapper.le("generate_time", endDate);
        }
        // 按生成时间降序排列
        queryWrapper.orderByDesc("generate_time");
        IPage<Report> reportPage = reportMapper.selectPage(pagination, queryWrapper);
        // 转换为响应DTO
        List<ReportResponse> result = new ArrayList<>();
        for (Report report : reportPage.getRecords()) {
            ReportResponse response = new ReportResponse();
            BeanUtils.copyProperties(report, response);
            result.add(response);
        }
        return result;
    }

    @Override
    public ReportResponse getReportDetail(Long id) {
        Report report = reportMapper.selectById(id);
        if (report == null) {
            throw new RuntimeException("报表不存在");
        }

        ReportResponse response = new ReportResponse();
        BeanUtils.copyProperties(report, response);

        // 解析报表参数
        if (report.getParameters() != null) {
            response.setParameters(parseParameters(report.getParameters()));
        }

        // 解析报表摘要数据
        if (report.getSummaryData() != null) {
            response.setSummaryData(parseSummaryData(report.getSummaryData()));
        }

        return response;
    }

    @Override
    public ReportResponse generateReport(ReportQueryRequest request) {
        // 使用Hutool的FileUtil创建目录（如果不存在则创建）
        FileUtil.mkdir(reportPath);

        // 生成报表数据
        Map<String, Object> summaryData = generateReportData(request);

        // 生成报表文件
        String reportFileName = generateReportFileName(request);
        String reportFilePath = reportPath + File.separator + reportFileName;
        generateReportFile(reportFilePath, request, summaryData);

        // 保存报表信息到数据库
        Report report = new Report();
        report.setReportName(generateReportName(request));
        report.setReportType(request.getReportType());
        report.setGenerateTime(new Date());
        Long userId = SecurityUtils.getUserId();
        String username = userMapper.selectById(userId).getRealName();
        report.setGenerateBy(username);
        report.setParameters(convertParametersToString(request));
        report.setSummaryData(convertSummaryDataToString(summaryData));
        report.setReportFilePath(reportFilePath);
        reportMapper.insert(report);
        // 转换为响应DTO
        ReportResponse response = new ReportResponse();
        BeanUtils.copyProperties(report, response);
        response.setParameters(summaryData);
        response.setSummaryData(summaryData);
        System.out.println(response);
        return response;
    }

    @Override
    public void exportReport(Long id, String format, HttpServletResponse response) {
        Report report = reportMapper.selectById(id);
        if (report == null) {
            throw new RuntimeException("报表不存在");
        }

        // 检查文件是否存在
        if (!FileUtil.exist(report.getReportFilePath())) {
            throw new RuntimeException("报表文件不存在");
        }

        try {
            // 设置响应头
            response.setContentType("application/octet-stream");
            String fileName = URLEncoder.encode(report.getReportName() + "." + format, StandardCharsets.UTF_8.name());
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);

            // 使用Hutool的IoUtil进行流拷贝
            try (InputStream in = FileUtil.getInputStream(report.getReportFilePath());
                 OutputStream out = response.getOutputStream()) {
                IoUtil.copy(in, out);
                out.flush();
            }
        } catch (IOException e) {
            throw new RuntimeException("报表导出失败: " + e.getMessage());
        }
    }

    @Override
    public boolean deleteReport(Long id) {
        // 查询报表信息
        Report report = reportMapper.selectById(id);
        if (report == null) {
            return false;
        }

        // 删除数据库记录
        int rows = reportMapper.deleteById(id);

        // 删除报表文件（使用Hutool的FileUtil）
        if (rows > 0 && report.getReportFilePath() != null) {
            return FileUtil.del(report.getReportFilePath());
        }

        return false;
    }

    @Override
    public ReportResponse viewReport(Long id) throws Exception {
        Report report = reportMapper.selectById(id);
        if (report == null) {
            throw new Exception("查询不到该报表！");
        }
        ReportResponse response = new ReportResponse();
        BeanUtils.copyProperties(report, response);
        return response;
    }

    /**
     * 设备报表详情
     * @param equipmentName
     * @param updateUser
     * @param startDate
     * @param endDate
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public List<EquipmentResponse> getEquipmentReportList(String equipmentName, String updateUser, Date startDate, Date endDate, int pageNum, int pageSize) {
        Page<EquipmentResponse> pagination = new Page<>(pageNum, pageSize);
        QueryWrapper<EquipmentResponse> queryWrapper = new QueryWrapper<>();
        // 如果指定了报表类型，则添加查询条件
        if (equipmentName != null && !equipmentName.trim().isEmpty()) {
            queryWrapper.eq("name", equipmentName);
        }
        // 如果指定了生成人，则添加查询条件
        if (updateUser != null && !updateUser.trim().isEmpty()) {
            String username = userMapper.selectById(updateUser).getRealName();
            queryWrapper.eq("update_user", username);
        }
        // 如果指定了时间，则添加查询条件
        if (startDate != null && endDate!= null) {
            if (startDate.after(endDate)) {
                // 交换日期
                Date temp = startDate;
                startDate = endDate;
                endDate = temp;
            }
            queryWrapper.ge("last_update_time", startDate);
            queryWrapper.le("last_update_time", endDate);
        }else if (startDate != null) {
            // 只有开始日期
            queryWrapper.ge("last_update_time", startDate);
        } else if (endDate != null) {
            // 只有结束日期
            queryWrapper.le("last_update_time", endDate);
        }
        // 按生成时间降序排列
        queryWrapper.orderByDesc("last_update_time");
        IPage<EquipmentResponse> reportPage = equipmentResponseMapper.selectPage(pagination, queryWrapper);
        // 转换为响应DTO
        List<EquipmentResponse> result = new ArrayList<>();
        reportPage.getRecords().forEach(item -> {
            EquipmentResponse equipmentResponse = new EquipmentResponse();
            BeanUtils.copyProperties(item, equipmentResponse);
            result.add(equipmentResponse);
        });
        return result;
    }


    // 生成报表数据（实际项目中应从数据库查询）
    private Map<String, Object> generateReportData(ReportQueryRequest request) {
        Map<String, Object> data = new HashMap<>();

        // 根据报表类型生成不同的数据
        switch (request.getReportType()) {
            case "production":
                data.put("totalProduction", 1250);
                data.put("yieldRate", 98.2);
                data.put("targetAchievementRate", 95.5);
                data.put("productionTrend", generateProductionTrend());
                break;
            case "quality":
                data.put("qualifiedRate", 99.1);
                data.put("defectCount", 12);
                data.put("defectTypes", generateDefectTypes());
                break;
            case "equipment":
                data.put("averageUtilizationRate", 85.3);
                data.put("downtime", 12.5);
                data.put("maintenanceCount", 8);
                break;
            case "user":
                data.put("totalProduction", 1250);
                data.put("yieldRate", 98.2);
                data.put("targetAchievementRate", 95.5);
                data.put("productionTrend", generateProductionTrend());
                break;
            case "file":
                data.put("qualifiedRate", 99.1);
                data.put("defectCount", 12);
                data.put("defectTypes", generateDefectTypes());
                break;
            case "system":
                data.put("averageUtilizationRate", 85.3);
                data.put("downtime", 12.5);
                data.put("maintenanceCount", 8);
                break;
            default:
                throw new RuntimeException("不支持的报表类型");
        }

        return data;
    }

    // 生成报表文件名
    private String generateReportFileName(ReportQueryRequest request) {
        return request.getReportType() + "_" + System.currentTimeMillis() + ".xlsx";
    }

    // 生成报表名称
    private String generateReportName(ReportQueryRequest request) {
        String typeName = "";
        switch (request.getReportType()) {
            case "production":
                typeName = "生产报表";
                break;
            case "quality":
                typeName = "质量报表";
                break;
            case "equipment":
                typeName = "设备报表";
                break;
        }
        return typeName + "_" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
    }

    // 生成报表文件（简化实现）
    private void generateReportFile(String filePath, ReportQueryRequest request, Map<String, Object> data) {
        try {
            // 使用Hutool创建空文件
            FileUtil.touch(filePath);
        } catch (Exception e) {
            throw new RuntimeException("报表文件生成失败: " + e.getMessage());
        }
    }

    // 转换查询参数为字符串（实际项目中应使用JSON工具）
    private String convertParametersToString(ReportQueryRequest request) {
        // 简化实现，实际项目中应使用Jackson或Gson转换为JSON字符串
        return "{'reportType':'" + request.getReportType() + "'}";
    }

    // 转换摘要数据为字符串（实际项目中应使用JSON工具）
    private String convertSummaryDataToString(Map<String, Object> summaryData) {
        // 简化实现，实际项目中应使用Jackson或Gson转换为JSON字符串
        return summaryData.toString();
    }

    // 解析参数（实际项目中应使用JSON工具）
    private Map<String, Object> parseParameters(String parameters) {
        // 简化实现，实际项目中应使用Jackson或Gson解析JSON字符串
        Map<String, Object> result = new HashMap<>();
        result.put("reportType", parameters.contains("production") ? "production" :
                parameters.contains("quality") ? "quality" : "equipment");
        return result;
    }

    // 解析摘要数据（实际项目中应使用JSON工具）
    private Map<String, Object> parseSummaryData(String summaryData) {
        // 简化实现，实际项目中应使用Jackson或Gson解析JSON字符串
        Map<String, Object> result = new HashMap<>();
        if (summaryData.contains("totalProduction")) {
            result.put("totalProduction", 1250);
            result.put("yieldRate", 98.2);
        } else if (summaryData.contains("qualifiedRate")) {
            result.put("qualifiedRate", 99.1);
            result.put("defectCount", 12);
        }
        return result;
    }

    // 生成生产趋势数据
    private Map<String, Integer> generateProductionTrend() {
        Map<String, Integer> trend = new LinkedHashMap<>();
        trend.put("1月", 156);
        trend.put("2月", 189);
        trend.put("3月", 210);
        trend.put("4月", 195);
        trend.put("5月", 220);
        trend.put("6月", 245);
        return trend;
    }

    // 生成缺陷类型数据
    private Map<String, Integer> generateDefectTypes() {
        Map<String, Integer> defectTypes = new HashMap<>();
        defectTypes.put("外观缺陷", 5);
        defectTypes.put("尺寸超差", 3);
        defectTypes.put("性能不达标", 2);
        defectTypes.put("其他", 2);
        return defectTypes;
    }
}
