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
 * 质检记录表
 * </p>
 *
 * @author xuanyang
 * @since 2026-06-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("mes_quality_check")
@ApiModel(value="MesQualityCheck对象", description="质检记录表")
public class MesQualityCheck implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "工单ID")
    private Long orderId;

    @ApiModelProperty(value = "对应工步ID")
    private Long stepId;

    @ApiModelProperty(value = "检验数量")
    private BigDecimal checkNum;

    @ApiModelProperty(value = "合格数量")
    private BigDecimal goodNum;

    @ApiModelProperty(value = "不良数量")
    private BigDecimal defectNum;

    @ApiModelProperty(value = "不良类型")
    private String defectType;

    @ApiModelProperty(value = "检验结果 0-不合格 1-合格")
    private Integer checkResult;

    @ApiModelProperty(value = "检验人")
    private String checkUser;

    @ApiModelProperty(value = "检验时间")
    private LocalDateTime checkTime;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "逻辑删除")
    private Integer isDelete;


}
