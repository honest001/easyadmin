package com.easyadmin.module.sys.controller;

import com.easyadmin.common.vo.Result;
import com.easyadmin.module.sys.service.SysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 系统配置Controller
 */
@RestController
@RequestMapping("/sys/config")
public class SysConfigController {

    @Autowired
    private SysConfigService sysConfigService;

    /**
     * 系统配置查询
     */
    @GetMapping("/info")
    public Result<Map<String, String>> info() {
        Map<String, String> configMap = sysConfigService.getConfigMap();
        return Result.success(configMap);
    }

    /**
     * 系统配置修改
     */
    @PostMapping("/update")
    public Result<Void> update(@RequestBody Map<String, String> configMap) {
        sysConfigService.updateConfig(configMap);
        return Result.success();
    }
}