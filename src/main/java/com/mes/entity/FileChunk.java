package com.mes.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.util.Date;

/**
 * 文件分片实体类
 * 用于存储大文件分片上传的分片信息
 */
@Data
@TableName("sys_file_chunk") // 数据库表名，可根据实际情况修改
public class FileChunk {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 文件MD5值，用于标识同一个文件的分片
     */
    @TableField("file_md5")
    private String fileMd5;

    /**
     * 分片索引（从0开始）
     */
    @TableField("chunk_index")
    private Integer chunkIndex;

    /**
     * 总分片数量
     */
    @TableField("total_chunks")
    private Integer totalChunks;

    /**
     * 分片大小（字节）
     */
    @TableField("chunk_size")
    private Long chunkSize;

    /**
     * 分片文件存储路径
     */
    @TableField("chunk_path")
    private String chunkPath;

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
     * 分片临时存储路径
     */
    @TableField("file_path")
    private String filePath;
}
