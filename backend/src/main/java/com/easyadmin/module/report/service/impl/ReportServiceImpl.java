package com.easyadmin.module.report.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.easyadmin.module.order.entity.OrderItemEntity;
import com.easyadmin.module.order.entity.OrderPayEntity;
import com.easyadmin.module.order.mapper.OrderItemMapper;
import com.easyadmin.module.order.mapper.OrderPayMapper;
import com.easyadmin.module.goods.entity.GoodsEntity;
import com.easyadmin.module.goods.entity.GoodsInEntity;
import com.easyadmin.module.goods.entity.GoodsOutEntity;
import com.easyadmin.module.goods.mapper.GoodsMapper;
import com.easyadmin.module.goods.mapper.GoodsInMapper;
import com.easyadmin.module.goods.mapper.GoodsOutMapper;
import com.easyadmin.module.user.entity.AttendanceEntity;
import com.easyadmin.module.user.entity.EmployeeEntity;
import com.easyadmin.module.user.mapper.AttendanceMapper;
import com.easyadmin.module.user.mapper.EmployeeMapper;
import com.easyadmin.module.report.service.ReportService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportServiceImpl implements ReportService {
    private static final Logger log = LoggerFactory.getLogger(ReportServiceImpl.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private OrderPayMapper orderPayMapper;

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private GoodsInMapper goodsInMapper;

    @Autowired
    private GoodsOutMapper goodsOutMapper;

    @Autowired
    private AttendanceMapper attendanceMapper;

    @Autowired
    private EmployeeMapper employeeMapper;

    @Override
    public Map<String, Object> businessReport(String statType, String statTime) {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> dataList = new ArrayList<>();

        // 构建返回数据
        result.put("orderCount", 0);
        result.put("totalAmount", java.math.BigDecimal.ZERO);
        result.put("costAmount", java.math.BigDecimal.ZERO);
        result.put("profit", java.math.BigDecimal.ZERO);
        result.put("dataList", dataList);

        return result;
    }

    @Override
    public Map<String, Object> stockReport() {
        Map<String, Object> result = new HashMap<>();

        // 统计库存总量
        QueryWrapper<GoodsEntity> goodsWrapper = new QueryWrapper<>();
        goodsWrapper.eq("status", 1);
        List<GoodsEntity> goodsList = goodsMapper.selectList(goodsWrapper);

        int totalStock = 0;
        int lowStockCount = 0;
        for (GoodsEntity goods : goodsList) {
            totalStock += goods.getStock();
            if (goods.getStock() < goods.getWarnStock()) {
                lowStockCount++;
            }
        }

        // 统计出入库次数
        int inCount = goodsInMapper.selectCount(new QueryWrapper<>()).intValue();
        int outCount = goodsOutMapper.selectCount(new QueryWrapper<>()).intValue();

        // 构建返回数据
        result.put("totalStock", totalStock);
        result.put("lowStockCount", lowStockCount);
        result.put("inCount", inCount);
        result.put("outCount", outCount);
        result.put("goodsList", goodsList);

        return result;
    }

    @Override
    public Map<String, Object> attendanceReport(String salaryMonth) {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> dataList = new ArrayList<>();

        // 查询所有在职员工
        QueryWrapper<EmployeeEntity> employeeWrapper = new QueryWrapper<>();
        employeeWrapper.eq("status", 1);
        List<EmployeeEntity> employeeList = employeeMapper.selectList(employeeWrapper);

        for (EmployeeEntity employee : employeeList) {
            Map<String, Object> empData = new HashMap<>();
            empData.put("empId", employee.getId());
            empData.put("empName", employee.getEmpName());
            empData.put("deptId", employee.getDeptId());

            // 统计考勤数据
            QueryWrapper<AttendanceEntity> attendanceWrapper = new QueryWrapper<>();
            attendanceWrapper.eq("emp_id", employee.getId());
            attendanceWrapper.like("check_date", salaryMonth);
            List<AttendanceEntity> attendanceList = attendanceMapper.selectList(attendanceWrapper);

            int normalCount = 0;
            int lateCount = 0;
            int earlyCount = 0;
            int absentCount = 0;

            for (AttendanceEntity attendance : attendanceList) {
                switch (attendance.getCheckType()) {
                    case 1: normalCount++; break;
                    case 2: lateCount++; break;
                    case 3: earlyCount++; break;
                    case 4: absentCount++; break;
                }
            }

            empData.put("normalCount", normalCount);
            empData.put("lateCount", lateCount);
            empData.put("earlyCount", earlyCount);
            empData.put("absentCount", absentCount);
            empData.put("workDays", normalCount + lateCount + earlyCount);

            dataList.add(empData);
        }

        // 构建返回数据
        result.put("dataList", dataList);
        result.put("totalEmployee", employeeList.size());

        return result;
    }

    @Override
    public String exportReport(String reportType, String statTime) {
        // 这里实现报表导出逻辑，返回文件路径
        // 实际项目中需要使用ExcelUtil工具类生成Excel文件
        String filePath = "/tmp/report_" + reportType + "_" + System.currentTimeMillis() + ".xlsx";
        
        // 模拟导出操作
        log.info("导出报表：{}", filePath);
        
        return filePath;
    }
}