package com.easyadmin.module.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.easyadmin.module.user.entity.DeptEntity;

import java.util.List;

/**
 * 部门Service
 */
public interface DeptService extends IService<DeptEntity> {
    // 获取所有有效部门
    List<DeptEntity> listAll();
    // 新增/编辑部门
    boolean saveOrUpdateDept(DeptEntity dept);
    // 删除部门
    boolean deleteDept(Long id);
}