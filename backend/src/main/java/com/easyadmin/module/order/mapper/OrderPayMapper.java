package com.easyadmin.module.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.easyadmin.module.order.entity.OrderPayEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单收款Mapper
 */
@Mapper
public interface OrderPayMapper extends BaseMapper<OrderPayEntity> {
    // 基础CRUD直接继承BaseMapper，无需手写SQL，复杂查询可在此新增方法
}