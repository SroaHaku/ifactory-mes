package com.mes.service;

import cn.hutool.json.JSONObject;

import java.util.Map;

public interface DashboardService {

    /**
     * 获取生产汇总数据
     */
    JSONObject getProductionSummary();

    /**
     * 获取设备状态数据
     */
    Map<String, Object> getEquipmentStatus();

    /**
     * 获取质量统计数据
     */
    Map<String, Object> getQualityStatistics();
}
