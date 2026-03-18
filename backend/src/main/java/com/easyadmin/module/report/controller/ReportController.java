package com.easyadmin.module.report.controller;

import com.easyadmin.common.vo.Result;
import com.easyadmin.module.report.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

/**
 * 数据报表Controller
 */
@RestController
@RequestMapping("/report")
public class ReportController {

    @Autowired
    private ReportService reportService;

    /**
     * 经营数据统计
     */
    @GetMapping("/business")
    public Result<Map<String, Object>> business(@RequestParam String statType, @RequestParam String statTime) {
        Map<String, Object> data = reportService.businessReport(statType, statTime);
        return Result.success(data);
    }

    /**
     * 库存数据统计
     */
    @GetMapping("/stock")
    public Result<Map<String, Object>> stock() {
        Map<String, Object> data = reportService.stockReport();
        return Result.success(data);
    }

    /**
     * 考勤数据统计
     */
    @GetMapping("/attendance")
    public Result<Map<String, Object>> attendance(@RequestParam String salaryMonth) {
        Map<String, Object> data = reportService.attendanceReport(salaryMonth);
        return Result.success(data);
    }

    /**
     * 报表导出
     */
    @GetMapping("/export")
    public Result<String> export(@RequestParam String reportType, @RequestParam String statTime) {
        String filePath = reportService.exportReport(reportType, statTime);
        return Result.success(filePath);
    }
}