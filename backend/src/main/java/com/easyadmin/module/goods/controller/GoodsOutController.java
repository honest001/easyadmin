package com.easyadmin.module.goods.controller;

import com.easyadmin.common.vo.PageResult;
import com.easyadmin.common.vo.Result;
import com.easyadmin.module.goods.entity.GoodsOutEntity;
import com.easyadmin.module.goods.entity.GoodsOutItemEntity;
import com.easyadmin.module.goods.service.GoodsOutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 出库单Controller
 */
@RestController
@RequestMapping("/goods/out")
public class GoodsOutController {

    @Autowired
    private GoodsOutService goodsOutService;

    /**
     * 出库单提交
     */
    @PostMapping("/save")
    public Result<Void> save(@RequestBody GoodsOutRequest request) {
        goodsOutService.saveOut(request.getGoodsOut(), request.getItems());
        return Result.success();
    }

    /**
     * 出库记录列表
     */
    @GetMapping("/list")
    public Result<PageResult<GoodsOutEntity>> list(@RequestParam Map<String, Object> params) {
        PageResult<GoodsOutEntity> pageResult = goodsOutService.pageList(params);
        return Result.success(pageResult);
    }

    /**
     * 出库请求参数
     */
    static class GoodsOutRequest {
        private GoodsOutEntity goodsOut;
        private List<GoodsOutItemEntity> items;

        public GoodsOutEntity getGoodsOut() {
            return goodsOut;
        }

        public void setGoodsOut(GoodsOutEntity goodsOut) {
            this.goodsOut = goodsOut;
        }

        public List<GoodsOutItemEntity> getItems() {
            return items;
        }

        public void setItems(List<GoodsOutItemEntity> items) {
            this.items = items;
        }
    }
}