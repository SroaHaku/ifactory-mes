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
 * 工单用料明细表
 * </p>
 *
 * @author xuanyang
 * @since 2026-06-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("mes_order_material")
@ApiModel(value="MesOrderMaterial对象", description="工单用料明细表")
public class MesOrderMaterial implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "工单ID")
    private Long orderId;

    @ApiModelProperty(value = "物料ID")
    private Long materialId;

    @ApiModelProperty(value = "计划领用数量")
    private BigDecimal planUseNum;

    @ApiModelProperty(value = "实际耗用数量")
    private BigDecimal actualUseNum;

    @ApiModelProperty(value = "领用批次号")
    private String batchNo;

    @ApiModelProperty(value = "逻辑删除")
    private Integer isDelete;

    private LocalDateTime createTime;


}
