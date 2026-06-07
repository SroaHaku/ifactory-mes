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
 * 工单主表
 * </p>
 *
 * @author xuanyang
 * @since 2026-06-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("mes_work_order")
@ApiModel(value="MesWorkOrder对象", description="工单主表")
public class MesWorkOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "工单编号(唯一)")
    private String orderCode;

    @ApiModelProperty(value = "工单名称")
    private String orderName;

    @ApiModelProperty(value = "产出成品物料ID")
    private Long materialId;

    @ApiModelProperty(value = "关联BOM ID")
    private Long bomId;

    @ApiModelProperty(value = "绑定生产设备ID")
    private Long equipId;

    @ApiModelProperty(value = "计划生产数量")
    private BigDecimal planNum;

    @ApiModelProperty(value = "实际完成数量")
    private BigDecimal actualNum;

    @ApiModelProperty(value = "计划开始时间")
    private LocalDateTime planStartTime;

    @ApiModelProperty(value = "计划结束时间")
    private LocalDateTime planEndTime;

    @ApiModelProperty(value = "实际开始时间")
    private LocalDateTime actualStartTime;

    @ApiModelProperty(value = "实际结束时间")
    private LocalDateTime actualEndTime;

    @ApiModelProperty(value = "工单状态 0-未下达 1-生产中 2-已完成 3-暂停 4-取消")
    private Integer orderStatus;

    @ApiModelProperty(value = "优先级 1-最高 2-高 3-普通 4-低")
    private Integer priority;

    @ApiModelProperty(value = "逻辑删除")
    private Integer isDelete;

    private String createUser;

    private String updateUser;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;


}
