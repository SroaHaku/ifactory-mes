package com.mes.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mes.entity.VO.FileInfoVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService extends IService<FileInfoVO>{

    /**
     * 上传文件
     */
    FileInfoVO uploadFile(MultipartFile file, String type);

    /**
     * 获取文件列表
     */
    List<FileInfoVO> getFileList(String type, int page, int size);

    /**
     * 根据ID获取文件信息
     */
    FileInfoVO getFileById(Long id);

    /**
     * 删除文件
     */
    boolean deleteFileById(Long id);

    boolean checkChunk(String fileMd5, int chunkIndex);

    boolean uploadChunk(MultipartFile file, String fileMd5, int chunkIndex, int totalChunks);

    FileInfoVO mergeChunks(String fileMd5, String fileName, String type, long totalSize);






}
