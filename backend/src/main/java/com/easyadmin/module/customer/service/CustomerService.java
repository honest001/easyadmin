package com.easyadmin.module.customer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.easyadmin.common.vo.PageResult;
import com.easyadmin.module.customer.entity.CustomerEntity;
import com.easyadmin.module.customer.entity.CustomerRecordEntity;
import java.util.List;
import java.util.Map;

public interface CustomerService extends IService<CustomerEntity> {
    // 分页查询客户列表
    PageResult<CustomerEntity> pageList(Map<String, Object> params);
    // 新增/编辑客户
    boolean saveOrUpdateCustomer(CustomerEntity customer);
    // 查询客户往来记录
    List<CustomerRecordEntity> getRecordListByCustomerId(Long customerId);
    // 保存客户往来记录
    boolean saveRecord(CustomerRecordEntity record);
}