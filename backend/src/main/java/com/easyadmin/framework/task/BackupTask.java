package com.easyadmin.framework.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 数据备份定时任务
 */
@Component
public class BackupTask {
    private static final Logger log = LoggerFactory.getLogger(BackupTask.class);
    /**
     * 每天凌晨2点执行数据备份
     */
    @Scheduled(cron = "0 0 2 * * ?")
    public void backupData() {
        try {
            // 生成备份文件名
            String fileName = "easyadmin_backup_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + ".sql";
            // 执行备份命令
            String command = "mysqldump -uroot -proot123456 easyadmin > " + fileName;
            Runtime.getRuntime().exec(command);
            log.info("数据备份成功，文件名：{}", fileName);
        } catch (IOException e) {
            log.error("数据备份失败", e);
        }
    }
}