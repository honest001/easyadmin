package com.easyadmin.module.user.controller;

import com.easyadmin.common.vo.Result;
import com.easyadmin.module.user.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 考勤打卡Controller
 */
@RestController
@RequestMapping("/user/attendance")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    /**
     * 员工每日打卡
     */
    @PostMapping("/check")
    public Result<Void> check(HttpServletRequest request, @RequestParam Long empId) {
        String checkIp = request.getRemoteAddr();
        attendanceService.checkIn(empId, checkIp);
        return Result.success();
    }

    /**
     * 月度考勤统计
     */
    @GetMapping("/stat")
    public Result<Map<String, Object>> stat(@RequestParam String salaryMonth, @RequestParam(required = false) Long empId, @RequestParam(required = false) Long deptId) {
        Map<String, Object> statResult = attendanceService.statByMonth(salaryMonth, empId, deptId);
        return Result.success(statResult);
    }
}