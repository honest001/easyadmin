package com.easyadmin.module.goods.controller;

import com.easyadmin.common.vo.Result;
import com.easyadmin.module.goods.entity.GoodsCategoryEntity;
import com.easyadmin.module.goods.service.GoodsCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 货品分类Controller
 */
@RestController
@RequestMapping("/goods/category")
public class GoodsCategoryController {

    @Autowired
    private GoodsCategoryService goodsCategoryService;

    /**
     * 货品分类列表
     */
    @GetMapping("/list")
    public Result<List<GoodsCategoryEntity>> list() {
        List<GoodsCategoryEntity> categoryList = goodsCategoryService.listAll();
        return Result.success(categoryList);
    }

    /**
     * 保存货品分类
     */
    @PostMapping("/save")
    public Result<Void> save(@RequestBody GoodsCategoryEntity category) {
        goodsCategoryService.saveOrUpdate(category);
        return Result.success();
    }

    /**
     * 删除货品分类
     */
    @PostMapping("/delete")
    public Result<Void> delete(@RequestParam Long id) {
        goodsCategoryService.removeById(id);
        return Result.success();
    }
}