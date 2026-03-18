package com.easyadmin.module.goods.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.easyadmin.common.vo.PageResult;
import com.easyadmin.module.goods.entity.GoodsEntity;
import java.util.Map;

/**
 * 货品信息Service
 */
public interface GoodsService extends IService<GoodsEntity> {
    // 分页查询货品列表
    PageResult<GoodsEntity> pageList(Map<String, Object> params);
    // 新增/编辑货品
    boolean saveOrUpdateGoods(GoodsEntity goods);
    // 实时库存查询
    PageResult<GoodsEntity> stockList(Map<String, Object> params);
}