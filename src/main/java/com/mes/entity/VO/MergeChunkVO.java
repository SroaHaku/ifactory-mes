package com.mes.entity.VO;

import lombok.Data;

// 用 Lombok 的 @Data 自动生成 Getter、Setter、toString（无 Lombok 则手动写）
@Data
public class MergeChunkVO {
    // 对应前端 JSON 的 "fileMd5"
    private String fileMd5;
    // 对应前端 JSON 的 "fileName"
    private String fileName;
    // 对应前端 JSON 的 "type"，required = false 表示非必传
    private String type;
    // 对应前端 JSON 的 "totalSize"，前端是数字，后端用 long 类型
    private long totalSize;
}