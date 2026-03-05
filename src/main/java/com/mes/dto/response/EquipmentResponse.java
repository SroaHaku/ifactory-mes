package com.mes.dto.response;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("equipment")
public class EquipmentResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 设备ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 设备名称
     */
    @TableField("name")
    private String name;

    /**
     * 设备状态：运行中、停机、维护中、故障
     */
    @TableField("status")
    private String status;

    /**
     * 今日运行时间(小时)
     */
    @TableField("run_time")
    private BigDecimal runTime;

    /**
     * 最后更新时间
     */
    @TableField("last_update_time")
    private Date lastUpdateTime;

    /**
     * 更新人
     */
    @TableField("update_user")
    private String updateUser;
}
