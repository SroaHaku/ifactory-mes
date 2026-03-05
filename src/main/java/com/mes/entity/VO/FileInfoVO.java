package com.mes.entity.VO;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.util.Date;

/**
 * 文件信息实体类
 * 对应数据库中的文件信息表
 */
@Data
@TableName("sys_file_info")
public class FileInfoVO {

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
    @TableField("file_type")  // 与数据库字段保持一致
    private String fileType;  // 属性名修改为fileType，与字段名语义一致

    /**
     * 文件后缀名（如：.jpg, .pdf）
     */
    @TableField("file_suffix")
    private String fileSuffix;

    /**
     * 上传人ID
     */
    @TableField("upload_user_id")
    private Long uploadUserId;

    /**
     * 上传人名称
     */
    @TableField("upload_user_name")
    private String uploadUserName;

    /**
     * 文件分类（可自定义，如：report, template, image）
     */
    @TableField("file_category")
    private String fileCategory;

    /**
     * 上传时间
     */
    @TableField("upload_time")
    private Date uploadTime;

    /**
     * 是否删除（0-未删除，1-已删除）
     */
    @TableField("is_deleted")
    private Integer isDeleted;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private Date updateTime;

    /**
     * 文件MD5值，用于大文件分片上传时的校验和合并
     */
    @TableField("file_md5")
    private String fileMd5;

    /**
     * 文件上传状态（0-上传中，1-上传完成，2-上传失败）
     */
    @TableField("upload_status")
    private Integer uploadStatus;
}
