package com.easyadmin.module.approve.controller;

import com.easyadmin.common.vo.PageResult;
import com.easyadmin.common.vo.Result;
import com.easyadmin.module.approve.entity.ApproveEntity;
import com.easyadmin.module.approve.service.ApproveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 审批申请Controller
 */
@RestController
@RequestMapping("/approve")
public class ApproveController {

    @Autowired
    private ApproveService approveService;

    /**
     * 发起审批申请
     */
    @PostMapping("/apply")
    public Result<Void> apply(@RequestBody ApproveEntity approve) {
        approveService.apply(approve);
        return Result.success();
    }

    /**
     * 待审批列表
     */
    @GetMapping("/wait/list")
    public Result<PageResult<ApproveEntity>> waitList(@RequestParam Map<String, Object> params) {
        PageResult<ApproveEntity> pageResult = approveService.waitList(params);
        return Result.success(pageResult);
    }

    /**
     * 审批操作（通过/驳回）
     */
    @PostMapping("/operate")
    public Result<Void> operate(@RequestParam Long id, @RequestParam Integer approveStatus, @RequestParam String approveOpinion) {
        approveService.operate(id, approveStatus, approveOpinion);
        return Result.success();
    }

    /**
     * 我发起的审批
     */
    @GetMapping("/my/list")
    public Result<PageResult<ApproveEntity>> myList(@RequestParam Map<String, Object> params) {
        PageResult<ApproveEntity> pageResult = approveService.myList(params);
        return Result.success(pageResult);
    }

    /**
     * 已完结审批列表
     */
    @GetMapping("/finish/list")
    public Result<PageResult<ApproveEntity>> finishList(@RequestParam Map<String, Object> params) {
        PageResult<ApproveEntity> pageResult = approveService.finishList(params);
        return Result.success(pageResult);
    }
}