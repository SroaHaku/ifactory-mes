package com.mes.service.impl;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mes.entity.PO.FileInfoPO;
import com.mes.entity.User;
import com.mes.mapper.FileInfoPOMapper;
import com.mes.mapper.FileInfoVOMapper;
import com.mes.mapper.UserMapper;
import com.mes.service.DashboardService;
import com.mes.service.FileService;
import com.mes.service.ReportService;
import com.mes.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private UserService userService;

    @Autowired
    private FileService fileService;

    @Autowired
    private FileInfoVOMapper fileInfoVOMapper;

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private FileInfoPOMapper fileInfoPOMapper;
    @Autowired
    private ReportService reportService;

    @Override
    public JSONObject getProductionSummary() {
        // 实际项目中应从数据库查询
        JSONObject data = new JSONObject();
        long users = userService.count();
        long files = fileService.count();
        long reports = reportService.count();
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        queryWrapper.eq("status", 1);
        Long enabledUserCount = userMapper.selectCount(queryWrapper);
        List<FileInfoPO> fileInfos = fileInfoPOMapper.selectList(new QueryWrapper<FileInfoPO>());
        fileInfos.forEach(fileInfoPO -> {
            Integer uploadUserId = fileInfoPO.getUploadUserId();
            String username = "未知用户";
            // 2. 只有当ID不为null时，才查询用户名
            try {
                username = userMapper.selectByUserId(uploadUserId);
                // 处理查询结果为null的情况（如用户已被删除）
                if (username == null) {
                    username = "用户已删除";
                }
            } catch (Exception e) {
                // 记录异常日志，不影响主流程
                log.error("查询用户失败：userId={}", uploadUserId, e);
                username = "查询失败";
            }
            // 3. 赋值上传人名称
            fileInfoPO.setUploadUser(username);
        });
        data.putOpt("userCount", users);
        data.putOpt("fileCount", files);
        data.putOpt("reportCount", reports);
        data.putOpt("onlineUserCount", enabledUserCount);
        data.putOpt("recentFiles", fileInfos);
        return data;
    }

    @Override
    public Map<String, Object> getEquipmentStatus() {
        // 实际项目中应从数据库查询
        Map<String, Object> data = new HashMap<>();
        
        // 设备状态统计
        Map<String, Integer> statusCount = new HashMap<>();
        statusCount.put("运行中", 28);
        statusCount.put("停机", 5);
        statusCount.put("维护中", 3);
        statusCount.put("故障", 1);
        
        data.put("statusCount", statusCount);
        data.put("utilizationRate", 82.4);
        data.put("averageRuntime", "8.5h");
        
        return data;
    }

    @Override
    public Map<String, Object> getQualityStatistics() {
        // 实际项目中应从数据库查询
        Map<String, Object> data = new HashMap<>();
        
        data.put("qualifiedRate", 98.2);
        data.put("defectiveProducts", 12);
        data.put("majorDefectCount", 2);
        data.put("minorDefectCount", 10);
        
        // 近7天质量趋势
        Map<String, Double> trend = new HashMap<>();
        trend.put("1月1日", 97.8);
        trend.put("1月2日", 98.1);
        trend.put("1月3日", 97.9);
        trend.put("1月4日", 98.5);
        trend.put("1月5日", 98.3);
        trend.put("1月6日", 98.0);
        trend.put("1月7日", 98.2);
        data.put("qualityTrend", trend);
        
        return data;
    }
}
