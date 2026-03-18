package com.easyadmin.module.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.easyadmin.module.user.entity.AttendanceEntity;
import java.time.LocalDate;
import java.util.Map;

/**
 * 考勤打卡Service
 */
public interface AttendanceService extends IService<AttendanceEntity> {
    // 员工每日打卡
    boolean checkIn(Long empId, String checkIp);
    // 月度考勤统计
    Map<String, Object> statByMonth(String salaryMonth, Long empId, Long deptId);
}