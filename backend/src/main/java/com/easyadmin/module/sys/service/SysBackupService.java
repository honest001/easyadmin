package com.easyadmin.module.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.easyadmin.module.sys.entity.SysBackupEntity;

/**
 * 系统备份Service
 */
public interface SysBackupService extends IService<SysBackupEntity> {
    // 手动备份数据
    boolean backupNow();
}