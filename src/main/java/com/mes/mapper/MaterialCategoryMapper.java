package com.mes.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mes.entity.MaterialCategory;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MaterialCategoryMapper extends BaseMapper<MaterialCategory> {

    String selectNameById(Long categoryId);
}
