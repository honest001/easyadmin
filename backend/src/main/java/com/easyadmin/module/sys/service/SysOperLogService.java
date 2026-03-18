package com.easyadmin.module.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.easyadmin.common.vo.PageResult;
import com.easyadmin.module.sys.entity.SysOperLogEntity;
import java.util.Map;

/**
 * 系统操作日志Service
 */
public interface SysOperLogService extends IService<SysOperLogEntity> {
    // 分页查询操作日志
    PageResult<SysOperLogEntity> pageList(Map<String, Object> params);
    // 记录操作日志
    void saveLog(String operUser, String operModule, String operType, String operContent, String operIp);
}