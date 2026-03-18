package com.easyadmin.module.customer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.easyadmin.module.customer.entity.CustomerEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 客户信息Mapper
 */
@Mapper
public interface CustomerMapper extends BaseMapper<CustomerEntity> {
    // 基础CRUD直接继承BaseMapper，无需手写SQL，复杂查询可在此新增方法
}