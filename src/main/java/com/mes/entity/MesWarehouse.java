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
 * 仓库信息表
 * </p>
 *
 * @author xuanyang
 * @since 2026-06-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("mes_warehouse")
@ApiModel(value="MesWarehouse对象", description="仓库信息表")
public class MesWarehouse implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "仓库编码(唯一)")
    private String warehouseCode;

    @ApiModelProperty(value = "仓库名称")
    private String warehouseName;

    @ApiModelProperty(value = "仓库类型 0-原料仓 1-成品仓 2-半成品仓 3-不良仓")
    private Integer warehouseType;

    @ApiModelProperty(value = "仓库地址")
    private String address;

    @ApiModelProperty(value = "状态 0-停用 1-正常")
    private Integer status;

    @ApiModelProperty(value = "逻辑删除")
    private Integer isDelete;

    private String createUser;

    private String updateUser;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;


}
