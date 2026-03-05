package com.mes.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mes.entity.MaterialSpecParam;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MaterialSpecParamMapper extends BaseMapper<MaterialSpecParam> {

    List<MaterialSpecParam> selectByMaterialId(Long id);
}
