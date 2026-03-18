package com.easyadmin.module.customer.controller;

import com.easyadmin.common.vo.PageResult;
import com.easyadmin.common.vo.Result;
import com.easyadmin.module.customer.entity.CustomerEntity;
import com.easyadmin.module.customer.entity.CustomerRecordEntity;
import com.easyadmin.module.customer.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

/**
 * 客户管理Controller
 */
@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    /**
     * 客户分页列表
     */
    @GetMapping("/list")
    public Result<PageResult<CustomerEntity>> list(@RequestParam Map<String, Object> params) {
        PageResult<CustomerEntity> pageResult = customerService.pageList(params);
        return Result.success(pageResult);
    }

    /**
     * 客户新增/编辑
     */
    @PostMapping("/save")
    public Result<Void> save(@RequestBody CustomerEntity customer) {
        customerService.saveOrUpdateCustomer(customer);
        return Result.success();
    }

    /**
     * 客户往来记录新增
     */
    @PostMapping("/record/save")
    public Result<Void> saveRecord(@RequestBody CustomerRecordEntity record) {
        customerService.saveRecord(record);
        return Result.success();
    }

    /**
     * 客户往来记录查询
     */
    @GetMapping("/record/list")
    public Result<List<CustomerRecordEntity>> recordList(@RequestParam Long customerId) {
        List<CustomerRecordEntity> recordList = customerService.getRecordListByCustomerId(customerId);
        return Result.success(recordList);
    }

    /**
     * 根据ID查询客户详情
     */
    @GetMapping("/info/{id}")
    public Result<CustomerEntity> info(@PathVariable Long id) {
        CustomerEntity customer = customerService.getById(id);
        return Result.success(customer);
    }

    /**
     * 禁用客户
     */
    @PostMapping("/updateStatus")
    public Result<Void> updateStatus(@RequestParam Long id, @RequestParam Integer status) {
        CustomerEntity customer = new CustomerEntity();
        customer.setId(id);
        customer.setStatus(status);
        customerService.updateById(customer);
        return Result.success();
    }

    /**
     * 删除客户
     */
    @PostMapping("/delete")
    public Result<Void> delete(@RequestParam Long id) {
        customerService.removeById(id);
        return Result.success();
    }
}