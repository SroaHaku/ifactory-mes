package com.mes.controller;

import com.mes.dto.response.Result;
import com.mes.entity.VO.FileInfoVO;
import com.mes.entity.VO.MergeChunkVO;
import com.mes.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    private FileService fileService;

    /**
     * 普通文件上传
     */
    @PostMapping("/upload")
    public Result<FileInfoVO> uploadFile(@RequestParam("file") MultipartFile file,
                                         @RequestParam(value = "type", required = false) String type) {
        FileInfoVO fileInfo = fileService.uploadFile(file, type);
        return Result.success(fileInfo);
    }

    /**
     * 分片上传 - 检查分片是否已存在
     */
    @GetMapping("/chunk/check")
    public Result<Map<String, Boolean>> checkChunk(@RequestParam("fileMd5") String fileMd5,
                                                           @RequestParam("chunkIndex") int chunkIndex) {
        System.out.println("收到checkChunk请求：fileMd5=" + fileMd5 + ", chunkIndex=" + chunkIndex);
        boolean exists = fileService.checkChunk(fileMd5, chunkIndex);
        Map<String, Boolean> result = new HashMap<>();
        result.put("exists", exists);
        return Result.success(result);
    }

    /**
     * 分片上传 - 上传单个分片
     */
    @PostMapping("/chunk/upload")
    public Result<Map<String, Boolean>> uploadChunk(@RequestParam("file") MultipartFile file,
                                                            @RequestParam("fileMd5") String fileMd5,
                                                            @RequestParam("chunkIndex") int chunkIndex,
                                                            @RequestParam("totalChunks") int totalChunks) {
        boolean success = fileService.uploadChunk(file, fileMd5, chunkIndex, totalChunks);
        Map<String, Boolean> result = new HashMap<>();
        result.put("success", success);
        return Result.success(result);
    }

    /**
     * 分片上传 - 合并分片
     */
    @PostMapping("/chunk/merge")
    public Result<FileInfoVO> mergeChunks(@RequestBody MergeChunkVO param) {
        FileInfoVO fileInfo = fileService.mergeChunks(
                param.getFileMd5(),
                param.getFileName(),
                param.getType(),
                param.getTotalSize()
        );
        return Result.success(fileInfo);
    }

    /**
     * 获取文件列表（分页）
     */
    @GetMapping("/list")
    public Result<List<FileInfoVO>> getFileList(@RequestParam(value = "type", required = false) String type,
                                                @RequestParam(defaultValue = "1") int page,
                                                @RequestParam(defaultValue = "10") int size) {
        List<FileInfoVO> fileList = fileService.getFileList(type, page, size);
        return Result.success(fileList);
    }

    /**
     * 根据ID下载文件
     */
    @GetMapping("/download/{id}")
    public void downloadFile(@PathVariable Long id, HttpServletResponse response) {
        try {
            FileInfoVO fileInfo = fileService.getFileById(id);
            File file = new File(fileInfo.getFilePath());

            // 设置响应头
            response.setContentType(fileInfo.getFileType());
            response.setContentLengthLong(fileInfo.getFileSize());
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=\"" + fileInfo.getFileName() + "\"");

            // 写入文件内容到响应流
            Files.copy(file.toPath(), response.getOutputStream());
            response.getOutputStream().flush();
        } catch (IOException e) {
            throw new RuntimeException("文件下载失败: " + e.getMessage());
        }
    }

    /**
     * 根据ID获取文件信息
     */
    @GetMapping("/{id}")
    public Result<FileInfoVO> getFileInfo(@PathVariable Long id) {
        FileInfoVO fileInfo = fileService.getFileById(id);
        return Result.success(fileInfo);
    }

    /**
     * 根据ID删除文件
     */
    @DeleteMapping("/{id}")
    public Result<Map<String, Boolean>> deleteFile(@PathVariable Long id) {
        boolean success = fileService.deleteFileById(id);
        Map<String, Boolean> result = new HashMap<>();
        result.put("success", success);
        return Result.success(result);
    }
}
