package com.easyadmin.module.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.easyadmin.module.sys.entity.SysConfigEntity;

/**
 * 系统配置Service
 */
public interface SysConfigService extends IService<SysConfigEntity> {
    // 获取系统配置
    java.util.Map<String, String> getConfigMap();
    // 更新系统配置
    boolean updateConfig(java.util.Map<String, String> configMap);
}