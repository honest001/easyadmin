package com.easyadmin.module.goods.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.easyadmin.common.vo.PageResult;
import com.easyadmin.module.goods.entity.GoodsEntity;
import com.easyadmin.module.goods.mapper.GoodsMapper;
import com.easyadmin.module.goods.service.GoodsService;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * 货品信息Service实现类
 */
@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, GoodsEntity> implements GoodsService {

    @Override
    public PageResult<GoodsEntity> pageList(Map<String, Object> params) {
        // 构建分页参数
        int pageNum = Integer.parseInt(params.getOrDefault("pageNum", 1).toString());
        int pageSize = Integer.parseInt(params.getOrDefault("pageSize", 10).toString());
        Page<GoodsEntity> page = new Page<>(pageNum, pageSize);

        // 构建查询条件
        QueryWrapper<GoodsEntity> wrapper = new QueryWrapper<>();
        wrapper.like(params.containsKey("goodsName"), "goods_name", params.get("goodsName"));
        wrapper.eq(params.containsKey("categoryId"), "category_id", params.get("categoryId"));
        wrapper.eq(params.containsKey("status"), "status", params.get("status"));
        wrapper.orderByDesc("create_time");

        // 分页查询
        this.page(page, wrapper);

        // 封装分页返回对象
        PageResult<GoodsEntity> pageResult = new PageResult<>();
        pageResult.setTotal(page.getTotal());
        pageResult.setRecords(page.getRecords());
        return pageResult;
    }

    @Override
    public boolean saveOrUpdateGoods(GoodsEntity goods) {
        if (goods.getId() == null) {
            goods.setCreateTime(LocalDateTime.now());
            goods.setStock(0); // 初始库存为0
        }
        return this.saveOrUpdate(goods);
    }

    @Override
    public PageResult<GoodsEntity> stockList(Map<String, Object> params) {
        // 构建分页参数
        int pageNum = Integer.parseInt(params.getOrDefault("pageNum", 1).toString());
        int pageSize = Integer.parseInt(params.getOrDefault("pageSize", 10).toString());
        Page<GoodsEntity> page = new Page<>(pageNum, pageSize);

        // 构建查询条件
        QueryWrapper<GoodsEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("status", 1); // 只查询正常状态的货品
        wrapper.orderByAsc("stock");

        // 分页查询
        this.page(page, wrapper);

        // 封装分页返回对象
        PageResult<GoodsEntity> pageResult = new PageResult<>();
        pageResult.setTotal(page.getTotal());
        pageResult.setRecords(page.getRecords());
        return pageResult;
    }
}