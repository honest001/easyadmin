package com.easyadmin.module.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.easyadmin.common.vo.PageResult;
import com.easyadmin.module.user.entity.EmployeeEntity;
import com.easyadmin.module.user.mapper.EmployeeMapper;
import com.easyadmin.module.user.service.EmployeeService;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * 员工信息Service实现类
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, EmployeeEntity> implements EmployeeService {

    @Override
    public PageResult<EmployeeEntity> pageList(Map<String, Object> params) {
        // 构建分页参数
        int pageNum = Integer.parseInt(params.getOrDefault("pageNum", 1).toString());
        int pageSize = Integer.parseInt(params.getOrDefault("pageSize", 10).toString());
        Page<EmployeeEntity> page = new Page<>(pageNum, pageSize);

        // 构建查询条件
        QueryWrapper<EmployeeEntity> wrapper = new QueryWrapper<>();
        wrapper.like(params.containsKey("empName"), "emp_name", params.get("empName"));
        wrapper.eq(params.containsKey("deptId"), "dept_id", params.get("deptId"));
        wrapper.eq(params.containsKey("status"), "status", params.get("status"));
        wrapper.orderByDesc("create_time");

        // 分页查询
        this.page(page, wrapper);

        // 封装分页返回对象
        PageResult<EmployeeEntity> pageResult = new PageResult<>();
        pageResult.setTotal(page.getTotal());
        pageResult.setRecords(page.getRecords());
        return pageResult;
    }

    @Override
    public boolean saveOrUpdateEmployee(EmployeeEntity employee) {
        if (employee.getId() == null) {
            employee.setCreateTime(LocalDateTime.now());
        }
        return this.saveOrUpdate(employee);
    }
}