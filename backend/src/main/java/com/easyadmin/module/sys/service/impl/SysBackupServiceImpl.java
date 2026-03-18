package com.easyadmin.module.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.easyadmin.module.sys.entity.SysBackupEntity;
import com.easyadmin.module.sys.mapper.SysBackupMapper;
import com.easyadmin.module.sys.service.SysBackupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 系统备份Service实现类
 */
@Service
public class SysBackupServiceImpl extends ServiceImpl<SysBackupMapper, SysBackupEntity> implements SysBackupService {
    private static final Logger log = LoggerFactory.getLogger(SysBackupServiceImpl.class);

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean backupNow() {
        try {
            // 生成备份文件名
            String fileName = "easyadmin_backup_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + ".sql";
            // 执行备份命令
            String command = "mysqldump -uroot -proot123456 easyadmin > " + fileName;
            Runtime.getRuntime().exec(command);
            
            // 记录备份日志
            SysBackupEntity backup = new SysBackupEntity();
            backup.setBackupName(fileName);
            backup.setBackupPath("./" + fileName);
            backup.setBackupType(2); // 手动备份
            backup.setBackupTime(LocalDateTime.now());
            backup.setRemark("手动备份");
            this.save(backup);
            
            log.info("手动数据备份成功，文件名：{}", fileName);
            return true;
        } catch (IOException e) {
            log.error("手动数据备份失败", e);
            return false;
        }
    }
}