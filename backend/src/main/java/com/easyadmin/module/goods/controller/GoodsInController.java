package com.easyadmin.module.goods.controller;

import com.easyadmin.common.vo.PageResult;
import com.easyadmin.common.vo.Result;
import com.easyadmin.module.goods.entity.GoodsInEntity;
import com.easyadmin.module.goods.entity.GoodsInItemEntity;
import com.easyadmin.module.goods.service.GoodsInService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 入库单Controller
 */
@RestController
@RequestMapping("/goods/in")
public class GoodsInController {

    @Autowired
    private GoodsInService goodsInService;

    /**
     * 入库单提交
     */
    @PostMapping("/save")
    public Result<Void> save(@RequestBody GoodsInRequest request) {
        goodsInService.saveIn(request.getGoodsIn(), request.getItems());
        return Result.success();
    }

    /**
     * 入库记录列表
     */
    @GetMapping("/list")
    public Result<PageResult<GoodsInEntity>> list(@RequestParam Map<String, Object> params) {
        PageResult<GoodsInEntity> pageResult = goodsInService.pageList(params);
        return Result.success(pageResult);
    }

    /**
     * 入库请求参数
     */
    static class GoodsInRequest {
        private GoodsInEntity goodsIn;
        private List<GoodsInItemEntity> items;

        public GoodsInEntity getGoodsIn() {
            return goodsIn;
        }

        public void setGoodsIn(GoodsInEntity goodsIn) {
            this.goodsIn = goodsIn;
        }

        public List<GoodsInItemEntity> getItems() {
            return items;
        }

        public void setItems(List<GoodsInItemEntity> items) {
            this.items = items;
        }
    }
}