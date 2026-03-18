package com.easyadmin.module.order.controller;

import com.easyadmin.common.vo.PageResult;
import com.easyadmin.common.vo.Result;
import com.easyadmin.module.order.entity.OrderInfoEntity;
import com.easyadmin.module.order.entity.OrderItemEntity;
import com.easyadmin.module.order.entity.OrderPayEntity;
import com.easyadmin.module.order.service.OrderInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

/**
 * 订单管理Controller
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderInfoService orderInfoService;

    /**
     * 订单分页列表
     */
    @GetMapping("/list")
    public Result<PageResult<OrderInfoEntity>> list(@RequestParam Map<String, Object> params) {
        PageResult<OrderInfoEntity> pageResult = orderInfoService.pageList(params);
        return Result.success(pageResult);
    }

    /**
     * 订单新增/编辑
     */
    @PostMapping("/save")
    public Result<Void> save(@RequestBody OrderRequest request) {
        orderInfoService.saveOrUpdateOrder(request.getOrder(), request.getItems());
        return Result.success();
    }

    /**
     * 订单收款登记
     */
    @PostMapping("/pay")
    public Result<Void> pay(@RequestBody OrderPayEntity pay) {
        orderInfoService.payOrder(pay);
        return Result.success();
    }

    /**
     * 订单取消
     */
    @PostMapping("/cancel")
    public Result<Void> cancel(@RequestParam Long id) {
        orderInfoService.cancelOrder(id);
        return Result.success();
    }

    /**
     * 订单收款记录查询
     */
    @GetMapping("/pay/list")
    public Result<List<OrderPayEntity>> payList(@RequestParam Long orderId) {
        List<OrderPayEntity> payList = orderInfoService.getPayListByOrderId(orderId);
        return Result.success(payList);
    }

    /**
     * 订单请求对象
     */
    private static class OrderRequest {
        private OrderInfoEntity order;
        private List<OrderItemEntity> items;

        public OrderInfoEntity getOrder() {
            return order;
        }

        public void setOrder(OrderInfoEntity order) {
            this.order = order;
        }

        public List<OrderItemEntity> getItems() {
            return items;
        }

        public void setItems(List<OrderItemEntity> items) {
            this.items = items;
        }
    }
}