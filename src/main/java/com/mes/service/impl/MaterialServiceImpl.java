package com.mes.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mes.entity.Material;
import com.mes.entity.MaterialSpecParam;
import com.mes.entity.MaterialStockDetail;
import com.mes.entity.MaterialSupplierRelation;
import com.mes.mapper.*;
import com.mes.service.MaterialService;
import com.mes.entity.VO.MaterialQueryVO;
import com.mes.entity.VO.MaterialSpecParamVO;
import com.mes.entity.VO.MaterialStockDetailVO;
import com.mes.entity.VO.MaterialVO;
import com.mes.utils.SecurityUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MaterialServiceImpl extends ServiceImpl<MaterialMapper, Material> implements MaterialService {

    @Autowired
    private MaterialMapper materialMapper;

    @Autowired
    private MaterialCategoryMapper categoryMapper;

    @Autowired
    private MaterialSpecParamMapper specParamMapper;

    @Autowired
    private MaterialStockDetailMapper stockDetailMapper;

    @Autowired
    private MaterialSupplierRelationMapper relationMapper;

    @Override
    public MaterialVO getMaterialDetail(Long id) {
        // 使用MyBatis-Plus的getById方法查询
        Material material = getById(id);
        if (material == null) {
            return null;
        }

        MaterialVO materialVO = new MaterialVO();
        BeanUtils.copyProperties(material, materialVO);

        // 设置分类名称
        if (material.getCategoryId() != null) {
            String categoryName = categoryMapper.selectNameById(material.getCategoryId());
            materialVO.setCategoryName(categoryName);
        }

        // 转换布尔类型
        materialVO.setIsCritical(material.getIsCritical() == 1);
        materialVO.setStockAlert(material.getStockAlert() == 1);

        // 查询规格参数
        List<MaterialSpecParam> specParams = specParamMapper.selectByMaterialId(id);
        if (specParams != null && !specParams.isEmpty()) {
            List<MaterialSpecParamVO> specParamVOs = specParams.stream().map(param -> {
                MaterialSpecParamVO vo = new MaterialSpecParamVO();
                BeanUtils.copyProperties(param, vo);
                return vo;
            }).collect(Collectors.toList());
            materialVO.setSpecParams(specParamVOs);
        } else {
            materialVO.setSpecParams(new ArrayList<>());
        }

        // 查询库存详情
        List<MaterialStockDetail> stockDetails = stockDetailMapper.selectByMaterialId(id);
        if (stockDetails != null && !stockDetails.isEmpty()) {
            List<MaterialStockDetailVO> stockDetailVOs = stockDetails.stream().map(detail -> {
                MaterialStockDetailVO vo = new MaterialStockDetailVO();
                BeanUtils.copyProperties(detail, vo);
                return vo;
            }).collect(Collectors.toList());
            materialVO.setStockDetails(stockDetailVOs);
        } else {
            materialVO.setStockDetails(new ArrayList<>());
        }

        return materialVO;
    }

    @Override
    public IPage<Material> getMaterialList(MaterialQueryVO query) {
        // 使用MyBatis-Plus的分页对象
        Page<Material> page = new Page<>(query.getPageNum(), query.getPageSize());

        // 调用自定义分页查询
        IPage<Material> materialPage = materialMapper.selectByPage(page, query);

        // 可以在这里处理分页结果，如设置分类名称等
        materialPage.getRecords().forEach(material -> {
            if (material.getCategoryId() != null) {
                String categoryName = categoryMapper.selectNameById(material.getCategoryId());
                material.setCategoryName(categoryName);
            }
        });

        return materialPage;
    }

    @Override
    @Transactional
    public boolean copyMaterial(Long sourceId, Material newMaterial) {
        // 使用MyBatis-Plus的getById查询源物料
        Material source = getById(sourceId);
        if (source == null) {
            throw new RuntimeException("源物料不存在");
        }

        // 检查新编码是否已存在
        Material exist = lambdaQuery()
                .eq(Material::getMaterialCode, newMaterial.getMaterialCode())
                .one();
        if (exist != null) {
            throw new RuntimeException("新物料编码已存在");
        }

        // 设置创建时间
        newMaterial.setCreateTime(new Date());
        newMaterial.setUpdateTime(new Date());
        newMaterial.setId(null); // 确保是新增

        // 使用MyBatis-Plus的save方法插入
        boolean saveSuccess = save(newMaterial);
        if (!saveSuccess) {
            return false;
        }

        // 复制规格参数
        List<MaterialSpecParam> specParams = specParamMapper.selectByMaterialId(sourceId);
        if (specParams != null && !specParams.isEmpty()) {
            specParams.forEach(param -> {
                param.setId(null);
                param.setMaterialId(newMaterial.getId());
                specParamMapper.insert(param);
            });
        }

        return true;
    }

    /**
     * 批量删除物料及关联数据
     */
    @Override
    @Transactional
    public boolean batchDeleteWithRelations(List<Long> ids) {
        // 1. 删除关联的规格参数
        ids.forEach(id -> specParamMapper.deleteById(id));

        // 2. 删除关联的库存详情
        ids.forEach(id -> stockDetailMapper.deleteById(id));

        // 3. 批量删除物料主表数据
        return this.removeByIds(ids);
    }

    @Override
    public Boolean checkMaterialCode(String materialCode) {
        QueryWrapper<Material> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("material_code", materialCode);
        Long count = materialMapper.selectCount(queryWrapper);
        return count == 0;
    }

    /**
     * 新增物料
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveMaterial(Material material) {
        String name = SecurityUtils.getUserRealName();
        material.setCreateTime(new Date());
        material.setUpdateTime(new Date());
        material.setCreateBy(name);
        material.setUpdateBy(name);
        Integer stockQuantity = material.getStockQuantity();
        Integer minStock = material.getMinStock();
        if (stockQuantity >minStock) {
            material.setStockAlert(0);
        }else {
            material.setStockAlert(1);
        }
        // 1. 保存物料主表
        materialMapper.insert(material);
        // 2. 保存物料与供应商的关联关系
        saveMaterialSupplierRelations(material.getId(), material.getSupplierIds());
    }

    /**
     * 更新物料（含供应商关联）
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateMaterial(Material material) {
        String name = SecurityUtils.getUserRealName();
        material.setUpdateTime(new Date());
        material.setUpdateBy(name);
        Integer stockQuantity = material.getStockQuantity();
        Integer minStock = material.getMinStock();
        if (stockQuantity >minStock) {
            material.setStockAlert(0);
        }else {
            material.setStockAlert(1);
        }
        // 1. 更新物料主表
        materialMapper.updateById(material);
        // 2. 先删除旧的关联关系
        relationMapper.deleteByMaterialId(material.getId());
        // 3. 保存新的关联关系
        saveMaterialSupplierRelations(material.getId(), material.getSupplierIds());
    }

    /**
     * 批量保存物料与供应商的关联关系
     */
    private void saveMaterialSupplierRelations(Long materialId, List<Long> supplierIds) {
        if (supplierIds == null || supplierIds.isEmpty()) {
            return; // 没有供应商关联时直接返回
        }

        // 转换为关联表实体列表（默认第一个为主要供应商）
        List<MaterialSupplierRelation> relations = supplierIds.stream().map(supplierId -> {
            MaterialSupplierRelation relation = new MaterialSupplierRelation();
            relation.setMaterialId(materialId);
            relation.setSupplierId(supplierId);
            // 第一个供应商设为主供应商，其余为非主供应商
            relation.setIsMain(supplierIds.indexOf(supplierId) == 0 ? 1 : 0);
            return relation;
        }).collect(Collectors.toList());

        // 批量插入关联记录
        relations.forEach(relation -> {
            relationMapper.insert(relation);
        });
    }

    /**
     * 查询物料详情（含关联的供应商信息）
     */
    public Material getMaterialWithSuppliers(Long id) {
        return materialMapper.selectMaterialWithSuppliers(id);
    }

    @Override
    public Material getMaterialFullInfo(Long id) {
        return baseMapper.getMaterialFullInfo(id);
    }

    @Override
    public List<Material> getMaterialListForExport(MaterialQueryVO query) {
        // 构建查询条件（根据query中的筛选条件，如分类ID、状态等）
        QueryWrapper<Material> queryWrapper = new QueryWrapper<>();
        // 示例：按状态筛选（若query中有status）
        if (query.getStatus() != null && !query.getStatus().isEmpty()) {
            queryWrapper.eq("status", query.getStatus());
        }
        // 示例：按分类ID筛选（若query中有categoryId）
        if (query.getCategoryId() != null) {
            queryWrapper.eq("category_id", query.getCategoryId());
        }
        // 按创建时间排序（最新的在前）
        queryWrapper.orderByDesc("create_time");

        // 查询全部符合条件的数据（不分页）
        return materialMapper.selectList(queryWrapper);
    }
}
