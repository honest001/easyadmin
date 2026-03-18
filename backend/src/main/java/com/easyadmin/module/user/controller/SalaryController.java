package com.easyadmin.module.user.controller;

import com.easyadmin.common.vo.Result;
import com.easyadmin.module.user.entity.SalaryEntity;
import com.easyadmin.module.user.service.SalaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 薪资核算Controller
 */
@RestController
@RequestMapping("/user/salary")
public class SalaryController {

    @Autowired
    private SalaryService salaryService;

    /**
     * 月度薪资核算
     */
    @GetMapping("/stat")
    public Result<List<SalaryEntity>> stat(@RequestParam String salaryMonth) {
        List<SalaryEntity> salaryList = salaryService.statByMonth(salaryMonth);
        return Result.success(salaryList);
    }

    /**
     * 发放薪资
     */
    @PostMapping("/pay")
    public Result<Void> pay(@RequestParam Long id) {
        salaryService.paySalary(id);
        return Result.success();
    }
}