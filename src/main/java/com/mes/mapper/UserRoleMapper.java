package com.mes.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mes.entity.UserRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole> {
    List<String> selectRoleCodesByUserId(Long userId);

    List<String> selectRolesByUserId(@Param("userId") Long userId);

    void batchInsert(@Param("list") List<UserRole> userRoleList);

    List<Long> selectRolesIdByUserId(@Param("userId") Long userId);
}
