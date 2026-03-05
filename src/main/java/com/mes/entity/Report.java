package com.mes.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 报表实体类，对应数据库中的报表记录
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_report") // 数据库表名
public class Report {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 报表名称
     */
    @TableField("report_name")
    private String reportName;

    /**
     * 报表类型（如：production-生产报表，quality-质量报表，equipment-设备报表）
     */
    @TableField("report_type")
    private String reportType;

    /**
     * 报表生成时间
     */
    @TableField("generate_time")
    private Date generateTime;

    /**
     * 报表生成人
     */
    @TableField("generate_by")
    private String generateBy;

    /**
     * 报表查询参数，存储为JSON字符串
     */
    @TableField("parameters")
    private String parameters;

    /**
     * 报表摘要数据，存储为JSON字符串
     */
    @TableField("summary_data")
    private String summaryData;

    /**
     * 报表文件存储路径
     */
    @TableField("report_file_path")
    private String reportFilePath;
}
