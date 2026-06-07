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
 * 工步表
 * </p>
 *
 * @author xuanyang
 * @since 2026-06-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("mes_step")
@ApiModel(value="MesStep对象", description="工步表")
public class MesStep implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "工步编码")
    private String stepCode;

    @ApiModelProperty(value = "工步名称")
    private String stepName;

    @ApiModelProperty(value = "所属工序ID")
    private Long processId;

    @ApiModelProperty(value = "工步说明")
    private String stepDesc;

    @ApiModelProperty(value = "工步执行顺序")
    private Integer sortNum;

    @ApiModelProperty(value = "逻辑删除")
    private Integer isDelete;

    private String createUser;

    private String updateUser;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;


}
