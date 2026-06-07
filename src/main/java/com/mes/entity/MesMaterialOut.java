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
 * 下料记录表
 * </p>
 *
 * @author xuanyang
 * @since 2026-06-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("mes_material_out")
@ApiModel(value="MesMaterialOut对象", description="下料记录表")
public class MesMaterialOut implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "下料单号(唯一)")
    private String outCode;

    @ApiModelProperty(value = "关联工单ID")
    private Long orderId;

    @ApiModelProperty(value = "产出设备ID")
    private Long equipId;

    @ApiModelProperty(value = "所属线体ID")
    private Long lineId;

    @ApiModelProperty(value = "产出物料ID(成品/半成品)")
    private Long materialId;

    @ApiModelProperty(value = "产出批次号")
    private String batchNo;

    @ApiModelProperty(value = "下料数量")
    private BigDecimal outNum;

    @ApiModelProperty(value = "合格数量")
    private BigDecimal goodNum;

    @ApiModelProperty(value = "不良数量")
    private BigDecimal defectNum;

    @ApiModelProperty(value = "入库仓库ID")
    private Long warehouseId;

    @ApiModelProperty(value = "下料类型:0-正常完工,1-半成品下线,2-返工产出")
    private Integer outType;

    @ApiModelProperty(value = "下料操作人员")
    private String outUser;

    @ApiModelProperty(value = "下料时间")
    private LocalDateTime outTime;

    @ApiModelProperty(value = "关联质检单ID")
    private Long checkId;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "逻辑删除 0-正常 1-删除")
    private Integer isDelete;

    @ApiModelProperty(value = "创建人")
    private String createUser;

    @ApiModelProperty(value = "更新人")
    private String updateUser;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;


}
