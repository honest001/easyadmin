package com.easyadmin.module.goods.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.easyadmin.common.vo.PageResult;
import com.easyadmin.module.goods.entity.GoodsOutEntity;
import com.easyadmin.module.goods.entity.GoodsOutItemEntity;
import java.util.List;
import java.util.Map;

/**
 * 出库单Service
 */
public interface GoodsOutService extends IService<GoodsOutEntity> {
    // 提交出库单
    boolean saveOut(GoodsOutEntity goodsOut, List<GoodsOutItemEntity> items);
    
    // 分页查询出库记录
    PageResult<GoodsOutEntity> pageList(Map<String, Object> params);
}