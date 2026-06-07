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
 * 车间信息表
 * </p>
 *
 * @author xuanyang
 * @since 2026-06-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("mes_workshop")
@ApiModel(value="MesWorkshop对象", description="车间信息表")
public class MesWorkshop implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "车间编码(唯一)")
    private String workshopCode;

    @ApiModelProperty(value = "车间名称")
    private String workshopName;

    @ApiModelProperty(value = "车间类型 0-生产车间 1-仓储车间 2-辅助车间")
    private Integer workshopType;

    @ApiModelProperty(value = "车间位置")
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
