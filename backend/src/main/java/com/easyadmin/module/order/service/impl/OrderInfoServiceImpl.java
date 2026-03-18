package com.easyadmin.module.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.easyadmin.common.exception.BusinessException;
import com.easyadmin.common.vo.PageResult;
import com.easyadmin.module.order.entity.OrderInfoEntity;
import com.easyadmin.module.order.entity.OrderItemEntity;
import com.easyadmin.module.order.entity.OrderPayEntity;
import com.easyadmin.module.order.mapper.OrderInfoMapper;
import com.easyadmin.module.order.mapper.OrderItemMapper;
import com.easyadmin.module.order.mapper.OrderPayMapper;
import com.easyadmin.module.order.service.OrderInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfoEntity> implements OrderInfoService {
    private static final Logger log = LoggerFactory.getLogger(OrderInfoServiceImpl.class);

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private OrderPayMapper orderPayMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public PageResult<OrderInfoEntity> pageList(Map<String, Object> params) {
        // 构建分页参数
        int pageNum = Integer.parseInt(params.getOrDefault("pageNum", 1).toString());
        int pageSize = Integer.parseInt(params.getOrDefault("pageSize", 10).toString());
        Page<OrderInfoEntity> page = new Page<>(pageNum, pageSize);

        // 构建查询条件
        QueryWrapper<OrderInfoEntity> wrapper = new QueryWrapper<>();
        wrapper.like(params.containsKey("orderNo"), "order_no", params.get("orderNo"));
        wrapper.eq(params.containsKey("customerId"), "customer_id", params.get("customerId"));
        wrapper.eq(params.containsKey("payStatus"), "pay_status", params.get("payStatus"));
        wrapper.eq(params.containsKey("orderStatus"), "order_status", params.get("orderStatus"));
        if (params.containsKey("startTime")) {
            wrapper.ge("order_time", params.get("startTime"));
        }
        if (params.containsKey("endTime")) {
            wrapper.le("order_time", params.get("endTime"));
        }
        wrapper.select("id", "order_no", "customer_id", "order_time", "total_amount", "pay_status", "order_status", "remark");
        wrapper.orderByDesc("order_time");

        // 分页查询
        this.page(page, wrapper);

        // 封装分页返回对象
        PageResult<OrderInfoEntity> pageResult = new PageResult<>();
        pageResult.setTotal(page.getTotal());
        pageResult.setRecords(page.getRecords());
        return pageResult;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveOrUpdateOrder(OrderInfoEntity order, List<OrderItemEntity> items) {
        // 生成订单编号
        if (order.getId() == null) {
            String orderNo = "ORDER" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
            order.setOrderNo(orderNo);
            order.setOrderTime(LocalDateTime.now());
            order.setOrderStatus(0); // 待处理
            order.setPayStatus(0); // 未收款
        } else {
            // 已收款订单禁止编辑
            OrderInfoEntity existingOrder = this.getById(order.getId());
            if (existingOrder.getPayStatus() != 0) {
                throw new BusinessException("已收款订单禁止编辑");
            }
        }

        // 计算总金额
        java.math.BigDecimal totalAmount = java.math.BigDecimal.ZERO;
        for (OrderItemEntity item : items) {
            item.setItemAmount(item.getGoodsPrice().multiply(java.math.BigDecimal.valueOf(item.getGoodsNum())));
            totalAmount = totalAmount.add(item.getItemAmount());
        }
        order.setTotalAmount(totalAmount);

        // 保存订单主表（使用自定义SQL避免operator字段）
        if (order.getId() == null) {
            // 插入新订单
            String sql = "INSERT INTO order_info (order_no, customer_id, order_time, total_amount, pay_status, order_status, remark) VALUES (?, ?, ?, ?, ?, ?, ?)";
            jdbcTemplate.update(sql, order.getOrderNo(), order.getCustomerId(), order.getOrderTime(), order.getTotalAmount(), order.getPayStatus(), order.getOrderStatus(), order.getRemark());
        } else {
            // 更新订单
            String sql = "UPDATE order_info SET order_no = ?, customer_id = ?, order_time = ?, total_amount = ?, pay_status = ?, order_status = ?, remark = ? WHERE id = ?";
            jdbcTemplate.update(sql, order.getOrderNo(), order.getCustomerId(), order.getOrderTime(), order.getTotalAmount(), order.getPayStatus(), order.getOrderStatus(), order.getRemark(), order.getId());
        }

        // 保存订单明细
        if (order.getId() != null) {
            // 删除旧明细
            QueryWrapper<OrderItemEntity> wrapper = new QueryWrapper<>();
            wrapper.eq("order_id", order.getId());
            orderItemMapper.delete(wrapper);
        }
        // 保存新明细
        for (OrderItemEntity item : items) {
            item.setOrderId(order.getId());
            orderItemMapper.insert(item);
        }

        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean payOrder(OrderPayEntity pay) {
        // 检查订单是否存在
        OrderInfoEntity order = this.getById(pay.getOrderId());
        if (order == null) {
            throw new BusinessException("订单不存在");
        }

        // 检查订单状态
        if (order.getOrderStatus() == 2) { // 已取消
            throw new BusinessException("已取消的订单无法收款");
        }

        // 保存收款记录
        pay.setPayTime(LocalDateTime.now());
        orderPayMapper.insert(pay);

        // 计算已收款金额
        List<OrderPayEntity> payList = orderPayMapper.selectList(new QueryWrapper<OrderPayEntity>().eq("order_id", pay.getOrderId()));
        java.math.BigDecimal paidAmount = java.math.BigDecimal.ZERO;
        for (OrderPayEntity p : payList) {
            paidAmount = paidAmount.add(p.getPayAmount());
        }

        // 更新订单收款状态（使用自定义SQL避免operator字段）
        String sql;
        if (paidAmount.compareTo(order.getTotalAmount()) >= 0) {
            // 已收款，更新为已收款和已完成
            sql = "UPDATE order_info SET pay_status = 2, order_status = 1 WHERE id = ?";
        } else {
            // 部分收款，只更新收款状态
            sql = "UPDATE order_info SET pay_status = 1 WHERE id = ?";
        }
        jdbcTemplate.update(sql, pay.getOrderId());

        return true;
    }

    @Override
    public boolean cancelOrder(Long id) {
        // 检查订单是否存在
        OrderInfoEntity order = this.getById(id);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }

        // 检查是否可以取消
        if (order.getPayStatus() != 0) { // 已收款或部分收款
            throw new BusinessException("已收款订单禁止取消");
        }

        // 更新订单状态（使用自定义SQL避免operator字段）
        String sql = "UPDATE order_info SET order_status = 2 WHERE id = ?";
        int rows = jdbcTemplate.update(sql, id);
        return rows > 0;
    }

    @Override
    public List<OrderPayEntity> getPayListByOrderId(Long orderId) {
        QueryWrapper<OrderPayEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("order_id", orderId);
        wrapper.orderByDesc("pay_time");
        return orderPayMapper.selectList(wrapper);
    }

    @Override
    public OrderInfoEntity getById(Serializable id) {
        // 使用自定义SQL避免operator字段
        String sql = "SELECT id, order_no, customer_id, order_time, total_amount, pay_status, order_status, remark FROM order_info WHERE id = ?";
        List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, id);
        if (result.isEmpty()) {
            return null;
        }
        Map<String, Object> map = result.get(0);
        OrderInfoEntity order = new OrderInfoEntity();
        order.setId(Long.valueOf(map.get("id").toString()));
        order.setOrderNo(map.get("order_no").toString());
        order.setCustomerId(Long.valueOf(map.get("customer_id").toString()));
        order.setOrderTime((LocalDateTime) map.get("order_time"));
        order.setTotalAmount((java.math.BigDecimal) map.get("total_amount"));
        order.setPayStatus(Integer.valueOf(map.get("pay_status").toString()));
        order.setOrderStatus(Integer.valueOf(map.get("order_status").toString()));
        order.setRemark(map.get("remark") != null ? map.get("remark").toString() : null);
        return order;
    }
}