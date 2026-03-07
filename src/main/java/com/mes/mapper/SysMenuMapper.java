package com.mes.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mes.dto.properties.SysMenu;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface SysMenuMapper extends BaseMapper<SysMenu> {
    /**
     * 根据用户ID查询有权限的菜单列表
     */
    List<SysMenu> selectMenuListByUserId(@Param("userId") Long userId);
}