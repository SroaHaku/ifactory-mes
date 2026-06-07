package com.mes.entity;

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
 * 工单工步关联表
 * </p>
 *
 * @author xuanyang
 * @since 2026-06-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("mes_order_step")
@ApiModel(value="MesOrderStep对象", description="工单工步关联表")
public class MesOrderStep implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "工单ID")
    private Long orderId;

    @ApiModelProperty(value = "工步ID")
    private Long stepId;

    @ApiModelProperty(value = "工步状态 0-未开始 1-进行中 2-已完成 3-异常")
    private Integer stepStatus;

    @ApiModelProperty(value = "工步开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "工步结束时间")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "逻辑删除")
    private Integer isDelete;

    private LocalDateTime createTime;


}
