package com.easyadmin.module.approve.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.easyadmin.common.constant.CommonConstant;
import com.easyadmin.common.vo.PageResult;
import com.easyadmin.module.approve.entity.ApproveEntity;
import com.easyadmin.module.approve.mapper.ApproveMapper;
import com.easyadmin.module.approve.service.ApproveService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

/**
 * 审批申请Service实现类
 */
@Service
public class ApproveServiceImpl extends ServiceImpl<ApproveMapper, ApproveEntity> implements ApproveService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean apply(ApproveEntity approve) {
        // 生成审批单号
        String approveNo = "APP" + LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + UUID.randomUUID().toString().substring(0, 8);
        approve.setApproveNo(approveNo);
        // 设置默认状态
        approve.setApproveStatus(CommonConstant.STATUS_PENDING); // 0待审核
        approve.setApplyTime(LocalDateTime.now());
        return this.save(approve);
    }

    @Override
    public PageResult<ApproveEntity> waitList(Map<String, Object> params) {
        // 构建分页参数
        int pageNum = Integer.parseInt(params.getOrDefault("pageNum", 1).toString());
        int pageSize = Integer.parseInt(params.getOrDefault("pageSize", 10).toString());
        Page<ApproveEntity> page = new Page<>(pageNum, pageSize);

        // 构建查询条件
        QueryWrapper<ApproveEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("approve_user", params.get("userId"));
        wrapper.eq("approve_status", CommonConstant.STATUS_PENDING); // 0待审核
        wrapper.orderByDesc("apply_time");

        // 分页查询
        this.page(page, wrapper);

        // 封装分页返回对象
        PageResult<ApproveEntity> pageResult = new PageResult<>();
        pageResult.setTotal(page.getTotal());
        pageResult.setRecords(page.getRecords());
        return pageResult;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean operate(Long id, Integer approveStatus, String approveOpinion) {
        ApproveEntity approve = this.getById(id);
        if (approve == null) {
            return false;
        }
        // 更新审批状态
        approve.setApproveStatus(approveStatus);
        approve.setApproveOpinion(approveOpinion);
        approve.setApproveTime(LocalDateTime.now());
        return this.updateById(approve);
    }

    @Override
    public PageResult<ApproveEntity> myList(Map<String, Object> params) {
        // 构建分页参数
        int pageNum = Integer.parseInt(params.getOrDefault("pageNum", 1).toString());
        int pageSize = Integer.parseInt(params.getOrDefault("pageSize", 10).toString());
        Page<ApproveEntity> page = new Page<>(pageNum, pageSize);

        // 构建查询条件
        QueryWrapper<ApproveEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("apply_emp_id", params.get("userId"));
        wrapper.orderByDesc("apply_time");

        // 分页查询
        this.page(page, wrapper);

        // 封装分页返回对象
        PageResult<ApproveEntity> pageResult = new PageResult<>();
        pageResult.setTotal(page.getTotal());
        pageResult.setRecords(page.getRecords());
        return pageResult;
    }

    @Override
    public PageResult<ApproveEntity> finishList(Map<String, Object> params) {
        // 构建分页参数
        int pageNum = Integer.parseInt(params.getOrDefault("pageNum", 1).toString());
        int pageSize = Integer.parseInt(params.getOrDefault("pageSize", 10).toString());
        Page<ApproveEntity> page = new Page<>(pageNum, pageSize);

        // 构建查询条件
        QueryWrapper<ApproveEntity> wrapper = new QueryWrapper<>();
        wrapper.ne("approve_status", CommonConstant.STATUS_PENDING); // 非待审核状态
        if (params.containsKey("approveStatus")) {
            wrapper.eq("approve_status", params.get("approveStatus"));
        }
        wrapper.orderByDesc("approve_time");

        // 分页查询
        this.page(page, wrapper);

        // 封装分页返回对象
        PageResult<ApproveEntity> pageResult = new PageResult<>();
        pageResult.setTotal(page.getTotal());
        pageResult.setRecords(page.getRecords());
        return pageResult;
    }
}