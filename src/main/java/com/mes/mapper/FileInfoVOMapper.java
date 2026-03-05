package com.mes.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mes.entity.PO.FileInfoPO;
import com.mes.entity.VO.FileInfoVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface FileInfoVOMapper extends BaseMapper<FileInfoVO> {
    @Select("SELECT id, original_name, file_size, file_type, upload_time FROM sys_file_info WHERE id = #{id}")
    FileInfoVO selectFileVOById(@Param("id") Long id);
}
