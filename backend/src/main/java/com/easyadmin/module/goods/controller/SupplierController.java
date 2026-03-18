package com.easyadmin.module.goods.controller;

import com.easyadmin.common.vo.Result;
import com.easyadmin.module.goods.entity.SupplierEntity;
import com.easyadmin.module.goods.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 供应商Controller
 */
@RestController
@RequestMapping("/goods/supplier")
public class SupplierController {

    @Autowired
    private SupplierService supplierService;

    /**
     * 供应商列表
     */
    @GetMapping("/list")
    public Result<List<SupplierEntity>> list() {
        List<SupplierEntity> supplierList = supplierService.listAll();
        return Result.success(supplierList);
    }

    /**
     * 保存供应商
     */
    @PostMapping("/save")
    public Result<Void> save(@RequestBody SupplierEntity supplier) {
        supplierService.saveOrUpdate(supplier);
        return Result.success();
    }

    /**
     * 删除供应商
     */
    @PostMapping("/delete")
    public Result<Void> delete(@RequestParam Long id) {
        supplierService.removeById(id);
        return Result.success();
    }
}