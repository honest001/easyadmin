package com.easyadmin.module.report.service;

import java.util.Map;

public interface ReportService {
    // 经营数据统计
    Map<String, Object> businessReport(String statType, String statTime);
    // 库存数据统计
    Map<String, Object> stockReport();
    // 考勤数据统计
    Map<String, Object> attendanceReport(String salaryMonth);
    // 报表导出
    String exportReport(String reportType, String statTime);
}