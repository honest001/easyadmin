package com.easyadmin.module.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.easyadmin.module.user.entity.EmployeeEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 员工信息Mapper
 */
@Mapper
public interface EmployeeMapper extends BaseMapper<EmployeeEntity> {
    // 基础CRUD直接继承BaseMapper，无需手写SQL，复杂查询可在此新增方法
}