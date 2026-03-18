package com.easyadmin.module.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.easyadmin.module.user.entity.SalaryEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 薪资核算Mapper
 */
@Mapper
public interface SalaryMapper extends BaseMapper<SalaryEntity> {
    // 基础CRUD直接继承BaseMapper，无需手写SQL，复杂查询可在此新增方法
}