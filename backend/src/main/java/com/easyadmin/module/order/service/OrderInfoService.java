package com.easyadmin.module.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.easyadmin.common.vo.PageResult;
import com.easyadmin.module.order.entity.OrderInfoEntity;
import com.easyadmin.module.order.entity.OrderItemEntity;
import com.easyadmin.module.order.entity.OrderPayEntity;
import java.util.List;
import java.util.Map;

public interface OrderInfoService extends IService<OrderInfoEntity> {
    // 分页查询订单列表
    PageResult<OrderInfoEntity> pageList(Map<String, Object> params);
    // 新增/编辑订单
    boolean saveOrUpdateOrder(OrderInfoEntity order, List<OrderItemEntity> items);
    // 订单收款登记
    boolean payOrder(OrderPayEntity pay);
    // 取消订单
    boolean cancelOrder(Long id);
    // 查询订单收款记录
    List<OrderPayEntity> getPayListByOrderId(Long orderId);
}