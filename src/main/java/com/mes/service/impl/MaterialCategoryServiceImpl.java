package com.mes.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mes.entity.MaterialCategory;
import com.mes.mapper.MaterialCategoryMapper;
import com.mes.service.MaterialCategoryService;
import org.springframework.stereotype.Service;

@Service
public class MaterialCategoryServiceImpl extends ServiceImpl<MaterialCategoryMapper,MaterialCategory> implements MaterialCategoryService {

}
