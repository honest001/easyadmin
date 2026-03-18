package com.easyadmin.module.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.easyadmin.common.vo.PageResult;
import com.easyadmin.module.sys.entity.SysOperLogEntity;
import com.easyadmin.module.sys.mapper.SysOperLogMapper;
import com.easyadmin.module.sys.service.SysOperLogService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 系统操作日志Service实现类
 */
@Service
public class SysOperLogServiceImpl extends ServiceImpl<SysOperLogMapper, SysOperLogEntity> implements SysOperLogService {

    @Override
    public PageResult<SysOperLogEntity> pageList(Map<String, Object> params) {
        // 构建分页参数
        int pageNum = Integer.parseInt(params.getOrDefault("pageNum", 1).toString());
        int pageSize = Integer.parseInt(params.getOrDefault("pageSize", 10).toString());
        Page<SysOperLogEntity> page = new Page<>(pageNum, pageSize);

        // 构建查询条件
        QueryWrapper<SysOperLogEntity> wrapper = new QueryWrapper<>();
        wrapper.like(params.containsKey("operUser"), "oper_name", params.get("operUser"));
        wrapper.like(params.containsKey("operModule"), "oper_url", params.get("operModule"));
        wrapper.ge(params.containsKey("startTime"), "oper_time", params.get("startTime"));
        wrapper.le(params.containsKey("endTime"), "oper_time", params.get("endTime"));
        wrapper.orderByDesc("oper_time");

        // 分页查询
        this.page(page, wrapper);

        // 封装分页返回对象
        PageResult<SysOperLogEntity> pageResult = new PageResult<>();
        pageResult.setTotal(page.getTotal());
        pageResult.setRecords(page.getRecords());
        return pageResult;
    }

    @Override
    public void saveLog(String operUser, String operModule, String operType, String operContent, String operIp) {
        SysOperLogEntity log = new SysOperLogEntity();
        log.setOperUser(operUser);
        log.setOperUrl(operModule);
        log.setTitle(operType);
        log.setOperParam(operContent);
        log.setOperIp(operIp);
        log.setOperTime(LocalDateTime.now());
        log.setStatus(1);
        this.save(log);
    }
}