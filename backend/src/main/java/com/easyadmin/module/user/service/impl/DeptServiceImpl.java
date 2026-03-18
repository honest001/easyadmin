package com.easyadmin.module.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.easyadmin.common.constant.CommonConstant;
import com.easyadmin.common.exception.BusinessException;
import com.easyadmin.module.user.entity.DeptEntity;
import com.easyadmin.module.user.entity.EmployeeEntity;
import com.easyadmin.module.user.mapper.DeptMapper;
import com.easyadmin.module.user.mapper.EmployeeMapper;
import com.easyadmin.module.user.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 部门Service实现类
 */
@Service
public class DeptServiceImpl extends ServiceImpl<DeptMapper, DeptEntity> implements DeptService {

    @Autowired
    private EmployeeMapper employeeMapper;

    @Override
    public List<DeptEntity> listAll() {
        QueryWrapper<DeptEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("status", CommonConstant.STATUS_NORMAL);
        wrapper.orderByAsc("sort");
        return this.list(wrapper);
    }

    @Override
    public boolean saveOrUpdateDept(DeptEntity dept) {
        return this.saveOrUpdate(dept);
    }

    @Override
    public boolean deleteDept(Long id) {
        // 检查部门是否关联员工
        QueryWrapper<EmployeeEntity> empWrapper = new QueryWrapper<>();
        empWrapper.eq("dept_id", id);
        Long count = employeeMapper.selectCount(empWrapper);
        if (count > 0) {
            throw new BusinessException("该部门下有关联员工，无法删除");
        }
        return this.removeById(id);
    }
}