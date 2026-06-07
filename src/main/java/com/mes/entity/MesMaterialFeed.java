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
 * 上料记录表
 * </p>
 *
 * @author xuanyang
 * @since 2026-06-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("mes_material_feed")
@ApiModel(value="MesMaterialFeed对象", description="上料记录表")
public class MesMaterialFeed implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "上料单号(唯一)")
    private String feedCode;

    @ApiModelProperty(value = "关联工单ID")
    private Long orderId;

    @ApiModelProperty(value = "上料设备ID")
    private Long equipId;

    @ApiModelProperty(value = "所属线体ID")
    private Long lineId;

    @ApiModelProperty(value = "投放物料ID")
    private Long materialId;

    @ApiModelProperty(value = "物料批次号")
    private String batchNo;

    @ApiModelProperty(value = "上料数量")
    private BigDecimal feedNum;

    @ApiModelProperty(value = "上料类型:0-正常投料,1-补料,2-退料重投")
    private Integer feedType;

    @ApiModelProperty(value = "上料操作人员")
    private String feedUser;

    @ApiModelProperty(value = "上料时间")
    private LocalDateTime feedTime;

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
