package com.easyadmin.module.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.easyadmin.module.order.entity.OrderInfoEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单主表Mapper
 */
@Mapper
public interface OrderInfoMapper extends BaseMapper<OrderInfoEntity> {
    // 基础CRUD直接继承BaseMapper，无需手写SQL，复杂查询可在此新增方法
}