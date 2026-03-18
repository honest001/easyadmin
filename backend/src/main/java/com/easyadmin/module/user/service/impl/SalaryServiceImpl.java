package com.easyadmin.module.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.easyadmin.module.user.entity.EmployeeEntity;
import com.easyadmin.module.user.entity.SalaryEntity;
import com.easyadmin.module.user.mapper.EmployeeMapper;
import com.easyadmin.module.user.mapper.SalaryMapper;
import com.easyadmin.module.user.service.SalaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 薪资核算Service实现类
 */
@Service
public class SalaryServiceImpl extends ServiceImpl<SalaryMapper, SalaryEntity> implements SalaryService {

    @Autowired
    private EmployeeMapper employeeMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<SalaryEntity> statByMonth(String salaryMonth) {
        // 查询所有在职员工
        QueryWrapper<EmployeeEntity> empWrapper = new QueryWrapper<>();
        empWrapper.eq("status", 1); // 1在职
        List<EmployeeEntity> employeeList = employeeMapper.selectList(empWrapper);
        
        List<SalaryEntity> salaryList = new ArrayList<>();
        
        for (EmployeeEntity employee : employeeList) {
            // 检查是否已核算
            QueryWrapper<SalaryEntity> salaryWrapper = new QueryWrapper<>();
            salaryWrapper.eq("emp_id", employee.getId());
            salaryWrapper.eq("salary_month", salaryMonth);
            SalaryEntity existingSalary = this.getOne(salaryWrapper);
            
            if (existingSalary == null) {
                // 新建薪资记录
                SalaryEntity salary = new SalaryEntity();
                salary.setEmpId(employee.getId());
                salary.setSalaryMonth(salaryMonth);
                salary.setBaseSalary(employee.getBaseSalary());
                salary.setWorkDays(22); // 假设每月22个工作日
                // 计算实发薪资（简化处理，实际应根据考勤情况调整）
                BigDecimal realSalary = employee.getBaseSalary().multiply(new BigDecimal(22)).divide(new BigDecimal(22), 2, BigDecimal.ROUND_HALF_UP);
                salary.setRealSalary(realSalary);
                salary.setPayStatus(0); // 0未发放
                salary.setCreateTime(LocalDateTime.now());
                this.save(salary);
                salaryList.add(salary);
            } else {
                salaryList.add(existingSalary);
            }
        }
        
        return salaryList;
    }

    @Override
    public boolean paySalary(Long id) {
        SalaryEntity salary = this.getById(id);
        if (salary == null) {
            return false;
        }
        salary.setPayStatus(1); // 1已发放
        return this.updateById(salary);
    }
}