package com.mes.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mes.entity.MaterialStockDetail;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MaterialStockDetailMapper extends BaseMapper<MaterialStockDetail> {

    List<MaterialStockDetail> selectByMaterialId(Long id);
}
