package com.mes.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mes.entity.MaterialSupplierRelation;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface MaterialSupplierRelationMapper extends BaseMapper<MaterialSupplierRelation> {
    // 根据物料ID查询关联的所有供应商ID
    List<Long> selectSupplierIdsByMaterialId(@Param("materialId") Long materialId);

    // 根据物料ID删除所有关联记录（用于更新时先清空旧关联）
    int deleteByMaterialId(@Param("materialId") Long materialId);
}