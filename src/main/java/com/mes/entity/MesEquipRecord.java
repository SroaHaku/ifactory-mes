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
 * 设备运行记录表
 * </p>
 *
 * @author xuanyang
 * @since 2026-06-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("mes_equip_record")
@ApiModel(value="MesEquipRecord对象", description="设备运行记录表")
public class MesEquipRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "设备ID")
    private Long equipId;

    @ApiModelProperty(value = "设备状态 0-停机 1-运行 2-故障 3-保养")
    private Integer runStatus;

    @ApiModelProperty(value = "状态开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "状态结束时间")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "持续时长(分钟)")
    private Integer duration;

    @ApiModelProperty(value = "原因说明")
    private String reason;

    @ApiModelProperty(value = "逻辑删除")
    private Integer isDelete;

    private String createUser;

    private LocalDateTime createTime;


}
