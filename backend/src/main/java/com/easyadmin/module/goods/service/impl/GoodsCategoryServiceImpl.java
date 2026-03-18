package com.easyadmin.module.goods.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.easyadmin.common.constant.CommonConstant;
import com.easyadmin.module.goods.entity.GoodsCategoryEntity;
import com.easyadmin.module.goods.mapper.GoodsCategoryMapper;
import com.easyadmin.module.goods.service.GoodsCategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 货品分类Service实现类
 */
@Service
public class GoodsCategoryServiceImpl extends ServiceImpl<GoodsCategoryMapper, GoodsCategoryEntity> implements GoodsCategoryService {

    @Override
    public List<GoodsCategoryEntity> listAll() {
        QueryWrapper<GoodsCategoryEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("status", CommonConstant.STATUS_NORMAL);
        wrapper.orderByAsc("sort");
        return this.list(wrapper);
    }
}