package com.easyadmin.module.goods.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.easyadmin.module.goods.entity.GoodsCategoryEntity;
import java.util.List;

/**
 * 货品分类Service
 */
public interface GoodsCategoryService extends IService<GoodsCategoryEntity> {
    // 获取所有有效分类
    List<GoodsCategoryEntity> listAll();
}