package com.mes.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mes.entity.Material;
import com.mes.entity.VO.MaterialQueryVO;
import com.mes.entity.VO.MaterialVO;

import javax.servlet.ServletOutputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * 继承MyBatis-Plus的IService，获得更多高级查询功能
 */
public interface MaterialService extends IService<Material> {

    // 获取物料详情
    MaterialVO getMaterialDetail(Long id);

    // 分页查询物料列表
    IPage<Material> getMaterialList(MaterialQueryVO query);

    // 复制物料
    boolean copyMaterial(Long sourceId, Material newMaterial);

    /**
     * 批量删除物料及关联数据
     */
    boolean batchDeleteWithRelations(List<Long> ids);

    Boolean checkMaterialCode(String materialCode);

    void saveMaterial(Material material);

    void updateMaterial(Material material);

    Material getMaterialWithSuppliers(Long id);

    Material getMaterialFullInfo(Long id);

    List<Material> getMaterialListForExport(MaterialQueryVO query);
}
