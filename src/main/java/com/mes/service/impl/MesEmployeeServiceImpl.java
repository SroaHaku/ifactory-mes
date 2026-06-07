package com.mes.service.impl;

import com.mes.entity.MesEmployee;
import com.mes.mapper.MesEmployeeMapper;
import com.mes.service.IMesEmployeeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 员工信息表 服务实现类
 * </p>
 *
 * @author xuanyang
 * @since 2026-06-07
 */
@Service
public class MesEmployeeServiceImpl extends ServiceImpl<MesEmployeeMapper, MesEmployee> implements IMesEmployeeService {

}
