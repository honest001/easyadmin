package com.easyadmin.module.sys.controller;

import com.easyadmin.common.vo.PageResult;
import com.easyadmin.common.vo.Result;
import com.easyadmin.module.sys.entity.SysOperLogEntity;
import com.easyadmin.module.sys.service.SysOperLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 系统操作日志Controller
 */
@RestController
@RequestMapping("/sys/log")
public class SysLogController {

    @Autowired
    private SysOperLogService sysOperLogService;

    /**
     * 操作日志列表
     */
    @GetMapping("/list")
    public Result<PageResult<SysOperLogEntity>> list(@RequestParam Map<String, Object> params) {
        PageResult<SysOperLogEntity> pageResult = sysOperLogService.pageList(params);
        return Result.success(pageResult);
    }
}