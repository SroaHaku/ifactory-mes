package com.mes.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

@Mapper
public interface CommonMapper {
    /**
     * 动态指定表名 + Map字段插入
     * @param tableName 目标表
     * @param map 字段&值
     */
    int insertByMap(@Param("tableName") String tableName, @Param("data") Map<String, Object> map);
}