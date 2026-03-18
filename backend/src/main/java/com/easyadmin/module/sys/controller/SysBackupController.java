package com.easyadmin.module.sys.controller;

import com.easyadmin.common.vo.Result;
import com.easyadmin.module.sys.service.SysBackupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统备份Controller
 */
@RestController
@RequestMapping("/sys/backup")
public class SysBackupController {

    @Autowired
    private SysBackupService sysBackupService;

    /**
     * 手动数据备份
     */
    @GetMapping("/now")
    public Result<Boolean> backupNow() {
        boolean result = sysBackupService.backupNow();
        return Result.success(result);
    }
}