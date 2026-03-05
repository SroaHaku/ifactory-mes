package com.mes.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mes.entity.FileInfo;
import com.mes.entity.VO.FileInfoVO;
import com.mes.entity.FileChunk;
import com.mes.mapper.FileInfoMapper;
import com.mes.mapper.FileChunkMapper;
import com.mes.mapper.FileInfoVOMapper;
import com.mes.service.FileService;
import com.mes.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class FileServiceImpl extends ServiceImpl<FileInfoVOMapper, FileInfoVO> implements FileService {

    @Autowired
    private FileInfoVOMapper fileInfoVOMapper;

    @Autowired
    private FileChunkMapper fileChunkMapper;

    @Value("${file.upload-path}")
    private String uploadPath;

    @Value("${file.chunk-size}") // 默认10MB
    private long chunkSize;

    /**
     * 普通文件上传
     */
    @Override
    public FileInfoVO uploadFile(MultipartFile file, String type) {
        long fileSize = file.getSize();
        if (fileSize == 0) {
            throw new RuntimeException("不允许上传空文件");
        }
        if (fileSize > 1024 * 1024 * 100) {
            throw new RuntimeException("文件大小超过限制（最大100MB）");
        }
        // 创建上传目录（如果不存在）
        FileUtil.mkdir(uploadPath);
        // 生成唯一文件名
        String originalFilename = file.getOriginalFilename();
        String fileExt = FileUtil.extName(originalFilename);
        String fileName = System.currentTimeMillis() + "_" + RandomUtil.randomNumbers(6) + "." + fileExt;
        String filePath = uploadPath + File.separator + fileName;

        try {
            Long currentUserId = SecurityUtils.getUserId();
            // 保存文件到本地
            file.transferTo(new File(filePath));
            // 保存文件信息到数据库
            FileInfoVO fileInfo = new FileInfoVO();
            fileInfo.setFileName(fileName);
            fileInfo.setOriginalName(originalFilename);
            fileInfo.setFileSize(file.getSize());
            fileInfo.setFileType(file.getContentType());
            fileInfo.setFilePath(filePath);
            fileInfo.setUploadTime(new Date());
            fileInfo.setFileMd5(null); // 普通上传不计算MD5
            fileInfo.setUploadUserId(currentUserId);
            fileInfoVOMapper.insert(fileInfo);
            return fileInfo;
        } catch (IOException e) {
            throw new RuntimeException("文件上传失败: " + e.getMessage());
        }
    }

    /**
     * 分片上传 - 检查分片是否已存在
     */
    @Override
    public boolean checkChunk(String fileMd5, int chunkIndex) {
        QueryWrapper<FileChunk> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("file_md5", fileMd5)
                .eq("chunk_index", chunkIndex);
        return fileChunkMapper.selectCount(queryWrapper) > 0;
    }

    /**
     * 分片上传 - 上传单个分片
     */
    @Override
    public boolean uploadChunk(MultipartFile file, String fileMd5, int chunkIndex, int totalChunks) {
        try {
            if (checkChunk(fileMd5, chunkIndex)) {
                log.info("分片已存在，跳过上传：fileMd5={}, chunkIndex={}", fileMd5, chunkIndex);
                return true;
            }
            // 创建分片临时存储目录
            String chunkDir = uploadPath + File.separator + "chunks" + File.separator + fileMd5;
            FileUtil.mkdir(chunkDir);
            // 保存分片文件
            String chunkFileName = chunkIndex + ".part";
            String chunkFilePath = chunkDir + File.separator + chunkFileName;
            file.transferTo(new File(chunkFilePath));
            // 保存分片信息到数据库
            FileChunk fileChunk = new FileChunk();
            fileChunk.setFileMd5(fileMd5);
            fileChunk.setChunkIndex(chunkIndex);
            fileChunk.setTotalChunks(totalChunks);
            fileChunk.setChunkSize(file.getSize());
            fileChunk.setChunkPath(chunkFilePath);
            // 捕获唯一索引冲突异常，避免并发场景下的错误
            try {
                fileChunkMapper.insert(fileChunk);
            } catch (DuplicateKeyException e) {
                log.warn("分片并发上传冲突，已忽略：fileMd5={}, chunkIndex={}", fileMd5, chunkIndex);
                // 可选：删除已保存的重复分片文件
                FileUtil.del(chunkFilePath);
            }
            return true;
        } catch (IOException e) {
            throw new RuntimeException("分片上传失败: " + e.getMessage());
        }
    }

    /**
     * 分片上传 - 合并分片
     */
    @Override
    public FileInfoVO mergeChunks(String fileMd5, String fileName, String type, long totalSize) {
        try {
            // 分片存储目录
            String chunkDir = uploadPath + File.separator + "chunks" + File.separator + fileMd5;

            // 检查分片是否完整
            QueryWrapper<FileChunk> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("file_md5", fileMd5);
            List<FileChunk> chunks = fileChunkMapper.selectList(queryWrapper);

            if (chunks.isEmpty()) {
                throw new RuntimeException("未找到分片文件");
            }

            int totalChunks = chunks.get(0).getTotalChunks();
            if (chunks.size() != totalChunks) {
                throw new RuntimeException("分片不完整，无法合并");
            }

            // 生成最终文件路径
            String fileExt = FileUtil.extName(fileName);
            String finalFileName = System.currentTimeMillis() + "_" + RandomUtil.randomNumbers(6) + "." + fileExt;
            String finalFilePath = uploadPath + File.separator + finalFileName;

            // 合并所有分片
            Path finalFile = Paths.get(finalFilePath);
            for (int i = 0; i < totalChunks; i++) {
                String chunkFilePath = chunkDir + File.separator + i + ".part";
                Path chunkFile = Paths.get(chunkFilePath);

                // 追加分片内容到最终文件
                Files.write(finalFile, Files.readAllBytes(chunkFile),
                        java.nio.file.StandardOpenOption.CREATE,
                        java.nio.file.StandardOpenOption.APPEND);
            }

            // 验证合并后的文件大小
            if (FileUtil.size(new File(finalFilePath)) != totalSize) {
                FileUtil.del(finalFilePath); // 删除不完整文件
                throw new RuntimeException("文件合并后大小不一致，合并失败");
            }

            // 保存文件信息到数据库
            FileInfoVO fileInfo = new FileInfoVO();
            fileInfo.setFileName(finalFileName);
            fileInfo.setOriginalName(fileName);
            fileInfo.setFileSize(totalSize);
            fileInfo.setFileType(FileUtil.getType(new File(finalFilePath)));
            fileInfo.setFilePath(finalFilePath);
            fileInfo.setUploadTime(new Date());
            fileInfo.setFileMd5(fileMd5);
            fileInfo.setUploadUserId(SecurityUtils.getUserId());

            fileInfoVOMapper.insert(fileInfo);

            // 清理分片文件和记录
            FileUtil.del(chunkDir);
            fileChunkMapper.delete(queryWrapper);

            return fileInfo;
        } catch (IOException e) {
            throw new RuntimeException("文件合并失败: " + e.getMessage());
        }
    }

    /**
     * 获取文件列表（分页）
     */
    @Override
    public List<FileInfoVO> getFileList(String type, int page, int size) {
        Page<FileInfoVO> pagination = new Page<>(page, size);
        QueryWrapper<FileInfoVO> queryWrapper = new QueryWrapper<>();

        // 如果指定了文件类型，则添加查询条件
        if (type != null && !type.isEmpty()) {
            queryWrapper.eq("business_type", type);
        }

        // 按上传时间降序排列
        queryWrapper.orderByDesc("upload_time");

        IPage<FileInfoVO> filePage = fileInfoVOMapper.selectPage(pagination, queryWrapper);
        return filePage.getRecords();
    }

    /**
     * 根据ID获取文件信息
     */
    @Override
    public FileInfoVO getFileById(Long id) {
        FileInfoVO fileInfo = fileInfoVOMapper.selectById(id);
        if (fileInfo == null) {
            throw new RuntimeException("文件不存在");
        }
        return fileInfo;
    }

    /**
     * 根据ID删除文件
     */
    @Override
    public boolean deleteFileById(Long id) {
        // 先查询文件信息
        FileInfoVO fileInfo = getFileById(id);

        // 删除数据库记录
        int rows = fileInfoVOMapper.deleteById(id);

        // 删除本地文件
        if (rows > 0) {
            FileUtil.del(fileInfo.getFilePath());
            return true;
        }

        return false;
    }
}
