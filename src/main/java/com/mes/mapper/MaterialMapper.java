package com.mes.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mes.entity.Material;
import com.mes.entity.VO.MaterialQueryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MaterialMapper extends BaseMapper<Material> {

    // 根据ID查询物料
    Material selectById(Long id);

    // 根据编码查询物料
    Material selectByCode(String materialCode);

    // 分页查询物料列表
    IPage<Material> selectByPage(Page<Material> page, @Param("query") MaterialQueryVO query);

    // 查询总数
    int selectTotal(@Param("query") MaterialQueryVO query);

    // 更新物料
    int update(Material material);

    // 删除物料
    int deleteById(@Param("id")Long id);

    // 批量删除物料
    int batchDelete(@Param("ids") List<Long> ids);

    // 查询物料详情（关联查询供应商信息）
    Material selectMaterialWithSuppliers(@Param("id") Long id);

    Material getMaterialFullInfo(@Param("id")Long id);
}
