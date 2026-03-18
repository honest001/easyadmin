package com.easyadmin.module.customer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.easyadmin.common.vo.PageResult;
import com.easyadmin.module.customer.entity.CustomerEntity;
import com.easyadmin.module.customer.entity.CustomerRecordEntity;
import com.easyadmin.module.customer.mapper.CustomerMapper;
import com.easyadmin.module.customer.mapper.CustomerRecordMapper;
import com.easyadmin.module.customer.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, CustomerEntity> implements CustomerService {
    private static final Logger log = LoggerFactory.getLogger(CustomerServiceImpl.class);

    @Autowired
    private CustomerRecordMapper customerRecordMapper;

    @Override
    public PageResult<CustomerEntity> pageList(Map<String, Object> params) {
        // 构建分页参数
        int pageNum = Integer.parseInt(params.getOrDefault("pageNum", 1).toString());
        int pageSize = Integer.parseInt(params.getOrDefault("pageSize", 10).toString());
        Page<CustomerEntity> page = new Page<>(pageNum, pageSize);

        // 构建查询条件
        QueryWrapper<CustomerEntity> wrapper = new QueryWrapper<>();
        wrapper.like(params.containsKey("customerName"), "customer_name", params.get("customerName"));
        wrapper.like(params.containsKey("customerPhone"), "customer_phone", params.get("customerPhone"));
        wrapper.eq(params.containsKey("customerType"), "customer_type", params.get("customerType"));
        wrapper.eq("status", 1); // 只查询正常状态的客户
        wrapper.orderByDesc("create_time");

        // 分页查询
        this.page(page, wrapper);

        // 封装分页返回对象
        PageResult<CustomerEntity> pageResult = new PageResult<>();
        pageResult.setTotal(page.getTotal());
        pageResult.setRecords(page.getRecords());
        return pageResult;
    }

    @Override
    public boolean saveOrUpdateCustomer(CustomerEntity customer) {
        if (customer.getId() == null) {
            customer.setCreateTime(new Date());
            customer.setStatus(1); // 默认正常状态
        }
        return this.saveOrUpdate(customer);
    }

    @Override
    public List<CustomerRecordEntity> getRecordListByCustomerId(Long customerId) {
        QueryWrapper<CustomerRecordEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("customer_id", customerId);
        wrapper.orderByDesc("record_time");
        return customerRecordMapper.selectList(wrapper);
    }

    @Override
    public boolean saveRecord(CustomerRecordEntity record) {
        record.setRecordTime(LocalDateTime.now());
        return customerRecordMapper.insert(record) > 0;
    }
}