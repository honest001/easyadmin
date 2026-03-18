package com.easyadmin.module.goods.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.easyadmin.common.constant.CommonConstant;
import com.easyadmin.module.goods.entity.SupplierEntity;
import com.easyadmin.module.goods.mapper.SupplierMapper;
import com.easyadmin.module.goods.service.SupplierService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 供应商Service实现类
 */
@Service
public class SupplierServiceImpl extends ServiceImpl<SupplierMapper, SupplierEntity> implements SupplierService {

    @Override
    public List<SupplierEntity> listAll() {
        QueryWrapper<SupplierEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("status", CommonConstant.STATUS_NORMAL);
        wrapper.orderByAsc("id");
        return this.list(wrapper);
    }
}