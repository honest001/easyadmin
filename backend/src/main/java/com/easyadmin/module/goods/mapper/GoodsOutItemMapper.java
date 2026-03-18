package com.easyadmin.module.goods.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.easyadmin.module.goods.entity.GoodsOutItemEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 出库明细表Mapper
 */
@Mapper
public interface GoodsOutItemMapper extends BaseMapper<GoodsOutItemEntity> {
    // 基础CRUD直接继承BaseMapper，无需手写SQL，复杂查询可在此新增方法
}