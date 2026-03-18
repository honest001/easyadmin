package com.easyadmin.module.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.easyadmin.common.vo.PageResult;
import com.easyadmin.module.user.entity.EmployeeEntity;
import java.util.Map;

/**
 * 员工信息Service
 */
public interface EmployeeService extends IService<EmployeeEntity> {
    // 分页查询员工列表
    PageResult<EmployeeEntity> pageList(Map<String, Object> params);
    // 新增/编辑员工
    boolean saveOrUpdateEmployee(EmployeeEntity employee);
}