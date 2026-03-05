package com.mes.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mes.dto.request.ReportQueryRequest;
import com.mes.dto.response.EquipmentResponse;
import com.mes.dto.response.ReportResponse;
import com.mes.entity.Report;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

public interface ReportService extends IService<Report> {

    /**
     * 获取报表列表
     */
    List<ReportResponse> getReportList(String type, String generateBy, Date startDate, Date endDate,int page, int size);

    /**
     * 获取报表详情
     */
    ReportResponse getReportDetail(Long id);

    /**
     * 生成报表
     */
    ReportResponse generateReport(ReportQueryRequest request);

    /**
     * 导出报表
     */
    void exportReport(Long id, String format, HttpServletResponse response);

    /**
     * 删除报表
     */
    boolean deleteReport(Long id);

    ReportResponse viewReport(Long id) throws Exception;

    List<EquipmentResponse> getEquipmentReportList(String equipmentName, String updateUser, Date startDate, Date endDate, int pageNum, int pageSize);
}
