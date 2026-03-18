package com.easyadmin.module.goods.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.easyadmin.common.vo.PageResult;
import com.easyadmin.module.goods.entity.GoodsInEntity;
import com.easyadmin.module.goods.entity.GoodsInItemEntity;
import java.util.List;
import java.util.Map;

/**
 * 入库单Service
 */
public interface GoodsInService extends IService<GoodsInEntity> {
    // 提交入库单
    boolean saveIn(GoodsInEntity goodsIn, List<GoodsInItemEntity> items);
    
    // 分页查询入库记录
    PageResult<GoodsInEntity> pageList(Map<String, Object> params);
}