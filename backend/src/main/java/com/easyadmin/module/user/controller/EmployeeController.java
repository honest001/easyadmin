package com.easyadmin.module.user.controller;

import com.easyadmin.common.vo.PageResult;
import com.easyadmin.common.vo.Result;
import com.easyadmin.module.user.entity.EmployeeEntity;
import com.easyadmin.module.user.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 员工信息Controller
 */
@RestController
@RequestMapping("/user/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * 员工分页列表
     */
    @GetMapping("/list")
    public Result<PageResult<EmployeeEntity>> list(@RequestParam Map<String, Object> params) {
        PageResult<EmployeeEntity> pageResult = employeeService.pageList(params);
        return Result.success(pageResult);
    }

    /**
     * 员工新增/编辑
     */
    @PostMapping("/save")
    public Result<Void> save(@RequestBody EmployeeEntity employee) {
        employeeService.saveOrUpdateEmployee(employee);
        return Result.success();
    }

    /**
     * 根据ID查询员工详情
     */
    @GetMapping("/info/{id}")
    public Result<EmployeeEntity> info(@PathVariable Long id) {
        EmployeeEntity employee = employeeService.getById(id);
        return Result.success(employee);
    }
}