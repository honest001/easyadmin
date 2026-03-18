package com.easyadmin.module.goods.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.easyadmin.module.goods.entity.GoodsCategoryEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 货品分类Mapper
 */
@Mapper
public interface GoodsCategoryMapper extends BaseMapper<GoodsCategoryEntity> {
    // 基础CRUD直接继承BaseMapper，无需手写SQL，复杂查询可在此新增方法
}