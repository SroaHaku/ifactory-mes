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
 * 设备信息表
 * </p>
 *
 * @author xuanyang
 * @since 2026-06-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("mes_equipment")
@ApiModel(value="MesEquipment对象", description="设备信息表")
public class MesEquipment implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "设备编码(唯一)")
    private String equipCode;

    @ApiModelProperty(value = "设备名称")
    private String equipName;

    @ApiModelProperty(value = "设备型号")
    private String equipModel;

    @ApiModelProperty(value = "所属线体ID")
    private Long lineId;

    @ApiModelProperty(value = "设备类型 0-加工 1-检测 2-组装 3-搬运")
    private Integer equipType;

    @ApiModelProperty(value = "状态 0-停机 1-运行 2-维修 3-待机")
    private Integer status;

    @ApiModelProperty(value = "逻辑删除")
    private Integer isDelete;

    private String createUser;

    private String updateUser;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;


}
