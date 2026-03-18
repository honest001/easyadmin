package com.easyadmin.module.approve.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.easyadmin.common.vo.PageResult;
import com.easyadmin.module.approve.entity.ApproveEntity;
import java.util.Map;

/**
 * 审批申请Service
 */
public interface ApproveService extends IService<ApproveEntity> {
    // 发起审批申请
    boolean apply(ApproveEntity approve);
    // 待审批列表
    PageResult<ApproveEntity> waitList(Map<String, Object> params);
    // 审批操作（通过/驳回）
    boolean operate(Long id, Integer approveStatus, String approveOpinion);
    // 我发起的审批
    PageResult<ApproveEntity> myList(Map<String, Object> params);
    // 已完结审批列表
    PageResult<ApproveEntity> finishList(Map<String, Object> params);
}