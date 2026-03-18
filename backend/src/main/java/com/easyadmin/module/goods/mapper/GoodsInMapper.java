package com.easyadmin.module.goods.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.easyadmin.module.goods.entity.GoodsInEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 入库单Mapper
 */
@Mapper
public interface GoodsInMapper extends BaseMapper<GoodsInEntity> {
    // 基础CRUD直接继承BaseMapper，无需手写SQL，复杂查询可在此新增方法
}