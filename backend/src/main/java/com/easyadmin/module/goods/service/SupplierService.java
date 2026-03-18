package com.easyadmin.module.goods.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.easyadmin.module.goods.entity.SupplierEntity;
import java.util.List;

/**
 * 供应商Service
 */
public interface SupplierService extends IService<SupplierEntity> {
    // 获取所有有效供应商
    List<SupplierEntity> listAll();
}