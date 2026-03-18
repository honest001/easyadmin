package com.easyadmin.module.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.easyadmin.module.user.entity.SalaryEntity;
import java.util.List;

/**
 * 薪资核算Service
 */
public interface SalaryService extends IService<SalaryEntity> {
    // 月度薪资核算
    List<SalaryEntity> statByMonth(String salaryMonth);
    // 发放薪资
    boolean paySalary(Long id);
}