package com.mes.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 物料库存表
 * </p>
 *
 * @author xuanyang
 * @since 2026-06-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("mes_inventory")
@ApiModel(value="MesInventory对象", description="物料库存表")
public class MesInventory implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "物料ID")
    private Long materialId;

    @ApiModelProperty(value = "仓库ID")
    private Long warehouseId;

    @ApiModelProperty(value = "物料号")
    private String itemNo;

    @ApiModelProperty(value = "物料批次号")
    private String batchNo;

    @ApiModelProperty(value = "原始库存数量")
    private BigDecimal originNum;

    @ApiModelProperty(value = "现有库存数量")
    private BigDecimal stockNum;

    @ApiModelProperty(value = "安全库存")
    private BigDecimal safeStock;

    @ApiModelProperty(value = "逻辑删除")
    private Integer isDelete;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;


}
