package com.mes.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 文件信息实体类
 * 对应数据库中的文件信息表
 */
@Data
@TableName("sys_file_info")
public class FileInfo {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 文件名（系统生成的唯一名称）
     */
    @TableField("file_name")
    private String fileName;

    /**
     * 文件原始名称（上传时的名称）
     */
    @TableField("original_name")
    private String originalName;

    /**
     * 文件存储路径
     */
    @TableField("file_path")
    private String filePath;

    /**
     * 文件大小（字节）
     */
    @TableField("file_size")
    private Long fileSize;

    /**
     * 文件类型（如：image/jpeg, application/pdf）
     */
    @TableField("file_type")
    private String fileType;

    /**
     * 上传人ID
     */
    @TableField("upload_user_id")
    private Long uploadUserId;

    /**
     * 上传时间
     */
    @TableField("upload_time")
    private Date uploadTime;

}
