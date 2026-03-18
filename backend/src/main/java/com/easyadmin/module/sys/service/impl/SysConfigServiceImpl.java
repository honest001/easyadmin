package com.easyadmin.module.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.easyadmin.module.sys.entity.SysConfigEntity;
import com.easyadmin.module.sys.mapper.SysConfigMapper;
import com.easyadmin.module.sys.service.SysConfigService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 系统配置Service实现类
 */
@Service
public class SysConfigServiceImpl extends ServiceImpl<SysConfigMapper, SysConfigEntity> implements SysConfigService {

    @Override
    public Map<String, String> getConfigMap() {
        List<SysConfigEntity> configList = this.list();
        Map<String, String> configMap = new HashMap<>();
        for (SysConfigEntity config : configList) {
            configMap.put(config.getConfigKey(), config.getConfigValue());
        }
        return configMap;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateConfig(Map<String, String> configMap) {
        for (Map.Entry<String, String> entry : configMap.entrySet()) {
            SysConfigEntity config = new SysConfigEntity();
            config.setConfigKey(entry.getKey());
            config.setConfigValue(entry.getValue());
            this.update(config, new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<SysConfigEntity>()
                    .eq("config_key", entry.getKey()));
        }
        return true;
    }
}