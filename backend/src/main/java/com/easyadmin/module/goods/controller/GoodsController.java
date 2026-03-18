package com.easyadmin.module.goods.controller;

import com.easyadmin.common.vo.PageResult;
import com.easyadmin.common.vo.Result;
import com.easyadmin.module.goods.entity.GoodsEntity;
import com.easyadmin.module.goods.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 货品信息Controller
 */
@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    /**
     * 货品分页列表
     */
    @GetMapping("/list")
    public Result<PageResult<GoodsEntity>> list(@RequestParam Map<String, Object> params) {
        PageResult<GoodsEntity> pageResult = goodsService.pageList(params);
        return Result.success(pageResult);
    }

    /**
     * 货品新增/编辑
     */
    @PostMapping("/save")
    public Result<Void> save(@RequestBody GoodsEntity goods) {
        goodsService.saveOrUpdateGoods(goods);
        return Result.success();
    }

    /**
     * 实时库存查询
     */
    @GetMapping("/stock/list")
    public Result<PageResult<GoodsEntity>> stockList(@RequestParam Map<String, Object> params) {
        PageResult<GoodsEntity> pageResult = goodsService.stockList(params);
        return Result.success(pageResult);
    }

    /**
     * 根据ID查询货品详情
     */
    @GetMapping("/info/{id}")
    public Result<GoodsEntity> info(@PathVariable Long id) {
        GoodsEntity goods = goodsService.getById(id);
        return Result.success(goods);
    }

    /**
     * 删除货品
     */
    @PostMapping("/delete")
    public Result<Void> delete(@RequestParam Long id) {
        goodsService.removeById(id);
        return Result.success();
    }
}