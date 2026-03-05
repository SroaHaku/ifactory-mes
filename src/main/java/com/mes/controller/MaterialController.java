package com.mes.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mes.dto.response.Result;
import com.mes.entity.Material;
import com.mes.entity.MaterialCategory;
import com.mes.entity.Supplier;
import com.mes.entity.VO.MaterialQueryVO;
import com.mes.entity.VO.MaterialVO;
import com.mes.entity.Warehouse;
import com.mes.exception.BusinessException;
import com.mes.service.MaterialCategoryService;
import com.mes.service.MaterialService;
import com.mes.service.SupplierService;
import com.mes.service.WarehouseService;
import com.mes.utils.SecurityUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/material")
public class MaterialController {

    @Autowired
    private MaterialService materialService;

    @Autowired
    private MaterialCategoryService materialCategoryService;

    @Autowired
    private SupplierService supplierService;

    @Autowired
    private WarehouseService warehouseService;

    /**
     * 获取物料详情
     */
    @GetMapping("/detail/{id}")
    public Result<MaterialVO> getMaterialDetail(@PathVariable Long id) {
        try {
            MaterialVO materialVO = materialService.getMaterialDetail(id);
            return Result.success(materialVO);
        } catch (Exception e) {
            return Result.fail("查询失败：" + e.getMessage());
        }
    }

    /**
     * 分页查询物料列表
     */
    @GetMapping("/list")
    public Result<Map<String, Object>> getMaterialList(MaterialQueryVO query) {
        try {
            IPage<Material> materialPage = materialService.getMaterialList(query);

            Map<String, Object> data = new HashMap<>();
            data.put("records", materialPage.getRecords());
            data.put("total", materialPage.getTotal());
            data.put("pageNum", materialPage.getCurrent());
            data.put("pageSize", materialPage.getSize());
            data.put("pages", materialPage.getPages());

            return Result.success(data);
        } catch (Exception e) {
            return Result.fail("查询失败：" + e.getMessage());
        }
    }

    /**
     * 新增物料
     */
    @PostMapping("/add")
    public Result<Void> addMaterial(@RequestBody Material material) {
        try {
            // 检查编码是否已存在
            Material exist = materialService.lambdaQuery()
                    .eq(Material::getMaterialCode, material.getMaterialCode())
                    .one();
            if (exist != null) {
                return Result.fail(400, "物料编码已存在");
            }

            boolean success = materialService.save(material);
            return success ? Result.success() : Result.fail("新增失败");
        } catch (Exception e) {
            return Result.fail("新增失败：" + e.getMessage());
        }
    }

    /**
     * 批量删除物料
     */
    @PostMapping("/batchDelete")
    public Result<Void> batchDeleteMaterial(@RequestBody List<Long> ids) {
        try {
            boolean success = materialService.batchDeleteWithRelations(ids);
            return success ? Result.success() : Result.fail("批量删除失败");
        } catch (Exception e) {
            return Result.fail("批量删除失败：" + e.getMessage());
        }
    }

    /**
     * 单个删除物料
     */
    @PostMapping("/delete")
    public Result<Void> deleteMaterial(@RequestBody Map<String, Long> param) {
        try {
            Long id = param.get("id");
            if (id == null) {
                return Result.fail(400, "物料ID不能为空");
            }

            boolean success = materialService.batchDeleteWithRelations(Collections.singletonList(id));
            return success ? Result.success() : Result.fail("删除失败");
        } catch (Exception e) {
            return Result.fail("删除失败：" + e.getMessage());
        }
    }

    /**
     * 获取物料类型列表
     */
    @GetMapping("/category")
    public Result<List<MaterialCategory>> getMaterialCategoryList() {
        try {
            QueryWrapper<MaterialCategory> queryWrapper = new QueryWrapper<>();
            queryWrapper.orderByAsc("sort");
            List<MaterialCategory> categoryList = materialCategoryService.list(queryWrapper);

            return Result.success(categoryList);
        } catch (Exception e) {
            return Result.fail("查询物料分类失败：" + e.getMessage());
        }
    }

    /**
     * 获取仓库列表
     */
    @GetMapping("/warehouse/list")
    public Result<List<Warehouse>> getWarehouseList() {
        try {
            QueryWrapper<Warehouse> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("status", 1).orderByAsc("name");
            List<Warehouse> warehouseList = warehouseService.list(queryWrapper);

            return Result.success(warehouseList);
        } catch (Exception e) {
            return Result.fail("查询仓库列表失败：" + e.getMessage());
        }
    }

    /**
     * 获取供应商列表
     */
    @GetMapping("/supplier/list")
    public Result<List<Supplier>> getSupplierList() {
        try {
            QueryWrapper<Supplier> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("status", 1).orderByAsc("name");
            List<Supplier> supplierList = supplierService.list(queryWrapper);

            return Result.success(supplierList);
        } catch (Exception e) {
            return Result.fail("查询供应商列表失败：" + e.getMessage());
        }
    }

    /**
     * 校验物料编码唯一性
     */
    @GetMapping("/checkCode")
    public Result<Map<String, Boolean>> checkMaterialCode(@RequestParam String materialCode) {
        try {
            if (materialCode == null || materialCode.trim().isEmpty()) {
                return Result.fail(400, "物料编码不能为空");
            }
            boolean exists = materialService.lambdaQuery()
                    .eq(Material::getMaterialCode, materialCode.trim())
                    .exists();
            Map<String, Boolean> data = new HashMap<>();
            data.put("exists", exists);
            return Result.success(data);
        } catch (Exception e) {
            return Result.fail("编码校验失败：" + e.getMessage());
        }
    }

    /**
     * 新增物料信息
     */
    @PostMapping("/save")
    public Result<Material> save(@RequestBody Material material) {
        materialService.saveMaterial(material);
        return Result.success();
    }

    /**
     * 更新物料信息
     */
    @PostMapping("/update")
    public Result<Void> updateMaterial(@RequestBody Material material) {
        try {
            if (material.getId() == null) {
                return Result.fail(400, "物料ID不能为空");
            }
            // 检查编码是否与其他物料冲突
            Material exist = materialService.lambdaQuery()
                    .eq(Material::getMaterialCode, material.getMaterialCode())
                    .ne(Material::getId, material.getId())
                    .one();
            if (exist != null) {
                return Result.fail(400, "物料编码已存在");
            }
            materialService.updateMaterial(material);
            return Result.success();
        } catch (Exception e) {
            return Result.fail("更新失败：" + e.getMessage());
        }
    }

    @GetMapping("/{id}/detail")
    public Result<Material> getDetail(@PathVariable Long id) {
        Material material = materialService.getMaterialWithSuppliers(id);
        return Result.success(material);
    }

    @GetMapping("/{id}")
    public Result<Material> getMaterialById(@PathVariable Long id) { // @PathVariable接收路径中的id参数
        Material material = materialService.getMaterialFullInfo(id);
        if (material == null) {
            return Result.fail("物料不存在");
        }
        return Result.success(material);
    }

    /**
     * 导出物料列表（适配material表结构）
     * @param query 筛选条件（如分类、状态等）
     * @param response 响应对象（用于写入Excel流）
     */
    @GetMapping("/export")
    public void exportMaterialList(MaterialQueryVO query, HttpServletResponse response) {
        try {
            // 1. 配置响应头：告诉浏览器这是Excel文件下载
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("UTF-8");
            // 处理中文文件名：URLEncoder编码避免乱码
            String fileName = URLEncoder.encode("物料列表_" + System.currentTimeMillis(), "UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");
            // 禁止缓存（避免浏览器缓存旧文件）
            response.setHeader("Cache-Control", "no-store, no-cache");

            // 2. 调用服务层：查询需要导出的物料数据（忽略分页，导出全部符合条件的数据）
            query.setPageNum(1);
            query.setPageSize(Integer.MAX_VALUE); // 不分页，获取所有数据
            List<Material> materialList = materialService.getMaterialListForExport(query);

            // 3. 生成Excel文件并写入响应流
            try (XSSFWorkbook workbook = new XSSFWorkbook();
                 OutputStream outputStream = response.getOutputStream()) {

                // 3.1 创建Excel工作表（命名为“物料数据”）
                XSSFSheet sheet = workbook.createSheet("物料数据");
                // 设置列宽（适配内容，避免文字截断）
                sheet.setColumnWidth(0, 12 * 256);  // ID列
                sheet.setColumnWidth(1, 20 * 256);  // 物料编码列
                sheet.setColumnWidth(2, 25 * 256);  // 物料名称列
                sheet.setColumnWidth(3, 15 * 256);  // 分类ID列
                sheet.setColumnWidth(4, 20 * 256);  // 规格型号列
                sheet.setColumnWidth(5, 10 * 256);  // 计量单位列
                sheet.setColumnWidth(6, 15 * 256);  // 品牌列
                sheet.setColumnWidth(7, 12 * 256);  // 采购单价列
                sheet.setColumnWidth(8, 12 * 256);  // 库存数量列
                sheet.setColumnWidth(9, 12 * 256);  // 最低库存列
                sheet.setColumnWidth(10, 12 * 256); // 状态列
                sheet.setColumnWidth(11, 12 * 256); // 是否关键物料列
                sheet.setColumnWidth(12, 12 * 256); // 库存预警列
                sheet.setColumnWidth(13, 30 * 256); // 物料图片URL列
                sheet.setColumnWidth(14, 30 * 256); // 物料描述列
                sheet.setColumnWidth(15, 12 * 256); // 创建人列
                sheet.setColumnWidth(16, 20 * 256); // 创建时间列
                sheet.setColumnWidth(17, 12 * 256); // 更新人列
                sheet.setColumnWidth(18, 20 * 256); // 更新时间列
                sheet.setColumnWidth(19, 12 * 256); // 所属仓库ID列

                // 3.2 创建表头（与material表字段对应，中文注释）
                XSSFRow headerRow = sheet.createRow(0);
                String[] headers = {
                        "物料ID", "物料编码", "物料名称", "分类ID", "规格型号",
                        "计量单位", "品牌", "采购单价", "库存数量", "最低库存",
                        "状态", "是否关键物料", "库存预警", "物料图片URL", "物料描述",
                        "创建人", "创建时间", "更新人", "更新时间", "所属仓库ID"
                };
                // 填充表头
                for (int i = 0; i < headers.length; i++) {
                    headerRow.createCell(i).setCellValue(headers[i]);
                }

                // 3.3 填充物料数据（一行对应一条物料记录）
                for (int i = 0; i < materialList.size(); i++) {
                    Material material = materialList.get(i);
                    XSSFRow dataRow = sheet.createRow(i + 1); // 从第2行开始（第1行是表头）

                    // 按表头顺序填充数据（注意字段类型匹配）
                    dataRow.createCell(0).setCellValue(material.getId() != null ? material.getId() : 0); // ID（Long）
                    dataRow.createCell(1).setCellValue(material.getMaterialCode() != null ? material.getMaterialCode() : ""); // 物料编码
                    dataRow.createCell(2).setCellValue(material.getMaterialName() != null ? material.getMaterialName() : ""); // 物料名称
                    dataRow.createCell(3).setCellValue(material.getCategoryId() != null ? material.getCategoryId() : 0); // 分类ID（Long）
                    dataRow.createCell(4).setCellValue(material.getSpecification() != null ? material.getSpecification() : ""); // 规格型号
                    dataRow.createCell(5).setCellValue(material.getUnit() != null ? material.getUnit() : ""); // 计量单位
                    dataRow.createCell(6).setCellValue(material.getBrand() != null ? material.getBrand() : ""); // 品牌
                    // 采购单价（Decimal→double转换）
                    double purchasePrice = material.getPurchasePrice() != null ? material.getPurchasePrice().doubleValue() : 0.00;
                    dataRow.createCell(7).setCellValue(purchasePrice);
                    dataRow.createCell(8).setCellValue(material.getStockQuantity() != null ? material.getStockQuantity() : 0); // 库存数量（Int）
                    dataRow.createCell(9).setCellValue(material.getMinStock() != null ? material.getMinStock() : 0); // 最低库存（Int）
                    // 状态：转换为中文（active→在用，inactive→停用）
                    String statusText = "active".equals(material.getStatus()) ? "在用" : "inactive".equals(material.getStatus()) ? "停用" : "";
                    dataRow.createCell(10).setCellValue(statusText);
                    // 是否关键物料：转换为中文（0→否，1→是）
                    String criticalText = material.getIsCritical() != null ? (material.getIsCritical() == 1 ? "是" : "否") : "否";
                    dataRow.createCell(11).setCellValue(criticalText);
                    // 库存预警：转换为中文（0→否，1→是）
                    String alertText = material.getStockAlert() != null ? (material.getStockAlert() == 1 ? "是" : "否") : "否";
                    dataRow.createCell(12).setCellValue(alertText);
                    dataRow.createCell(13).setCellValue(material.getImageUrl() != null ? material.getImageUrl() : ""); // 物料图片URL
                    dataRow.createCell(14).setCellValue(material.getDescription() != null ? material.getDescription() : ""); // 物料描述
                    dataRow.createCell(15).setCellValue(material.getCreateBy() != null ? material.getCreateBy() : ""); // 创建人
                    // 创建时间：格式化日期（避免Excel显示时间戳）
                    String createTime = material.getCreateTime() != null ? new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(material.getCreateTime()) : "";
                    dataRow.createCell(16).setCellValue(createTime);
                    dataRow.createCell(17).setCellValue(material.getUpdateBy() != null ? material.getUpdateBy() : ""); // 更新人
                    // 更新时间：格式化日期
                    String updateTime = material.getUpdateTime() != null ? new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(material.getUpdateTime()) : "";
                    dataRow.createCell(18).setCellValue(updateTime);
                    dataRow.createCell(19).setCellValue(material.getWarehouseId() != null ? material.getWarehouseId() : 0); // 所属仓库ID（Long）
                }

                // 4. 将Excel写入响应流（下载开始）
                workbook.write(outputStream);
                // 刷新流，确保数据全部输出
                outputStream.flush();
            }

        } catch (Exception e) {
            // 异常处理：返回错误信息（避免前端一直等待）
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            try (OutputStream outputStream = response.getOutputStream()) {
                String errorMsg = "{\"code\":500,\"message\":\"导出失败：" + e.getMessage() + "\",\"data\":null}";
                outputStream.write(errorMsg.getBytes("UTF-8"));
                outputStream.flush();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

}
